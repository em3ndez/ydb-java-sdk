package com.yandex.ydb.core.grpc;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Strings;
import com.google.common.net.HostAndPort;
import com.google.common.util.concurrent.MoreExecutors;
import com.yandex.ydb.core.Issue;
import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.StatusCode;
import com.yandex.ydb.core.auth.AuthProvider;
import com.yandex.ydb.core.auth.NopAuthProvider;
import com.yandex.ydb.core.grpc.impl.grpc.GrpcTransportImpl;
import com.yandex.ydb.core.grpc.impl.ydb.YdbTransportImpl;
import com.yandex.ydb.core.rpc.OperationTray;
import com.yandex.ydb.core.rpc.OutStreamObserver;
import com.yandex.ydb.core.rpc.RpcTransport;
import com.yandex.ydb.core.rpc.RpcTransportBuilder;
import com.yandex.ydb.core.rpc.StreamControl;
import com.yandex.ydb.core.rpc.StreamObserver;
import com.yandex.ydb.core.utils.Version;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ConnectivityState;
import io.grpc.ExperimentalApi;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;


/**
 * @author Sergey Polovko
 * @author Evgeniy Pshenitsin
 * @author Nikolay Perfilov
 */
public abstract class GrpcTransport implements RpcTransport {

    public static final int DEFAULT_PORT = 2135;
    public static final long WAIT_FOR_CONNECTION_MS = 10000;
    public static final long WAIT_FOR_CLOSING_MS = 1000;
    public static final long DISCOVERY_TIMEOUT_SECONDS = 10;

    private static final Logger logger = LoggerFactory.getLogger(GrpcTransport.class);

    protected static final EnumSet<ConnectivityState> TEMPORARY_STATES = EnumSet.of(
        ConnectivityState.IDLE,
        ConnectivityState.CONNECTING
    );

    private final CallOptions callOptions;
    protected final String database;
    private final GrpcOperationTray operationTray;
    private final long defaultReadTimeoutMillis;
    protected final DiscoveryMode discoveryMode;
    private volatile boolean shutdown = false;

    protected GrpcTransport(Builder builder) {
        this.callOptions = createCallOptions(builder);
        this.defaultReadTimeoutMillis = builder.getReadTimeoutMillis();
        this.database = Strings.nullToEmpty(builder.getDatabase());
        this.operationTray = new GrpcOperationTray(this);
        this.discoveryMode = builder.getDiscoveryMode();
    }

    public static Builder forHost(String host, int port) {
        return new Builder(null, null, singletonList(HostAndPort.fromParts(host, port)));
    }

    public static Builder forHosts(HostAndPort... hosts) {
        checkNotNull(hosts, "hosts is null");
        checkArgument(hosts.length > 0, "empty hosts array");
        return new Builder(null, null, Arrays.asList(hosts));
    }

    public static Builder forHosts(List<HostAndPort> hosts) {
        checkNotNull(hosts, "hosts is null");
        checkArgument(!hosts.isEmpty(), "empty hosts list");
        return new Builder(null, null, hosts);
    }

    public static Builder forEndpoint(String endpoint, String database) {
        checkNotNull(endpoint, "endpoint is null");
        checkNotNull(database, "database is null");
        return new Builder(endpoint, database, null);
    }

    // [<protocol>://]<host>[:<port>]/?database=<database-path>
    public static Builder forConnectionString(String connectionString) {
        checkNotNull(connectionString, "connection string is null");
        String endpoint;
        String database;
        String scheme;
        try {
            URI uri = new URI(connectionString.contains("://") ? connectionString : "grpc://" + connectionString);
            endpoint = uri.getAuthority();
            checkNotNull(endpoint, "no endpoint in connection string");
            Map<String, String> params = getQueryMap(uri.getQuery());
            database = params.get("database");
            checkNotNull(endpoint, "no database in connection string");
            scheme = uri.getScheme();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse connection string '" + connectionString +
                    "'. Expected format: [<protocol>://]<host>[:<port>]/?database=<database-path>", e);
        }
        Builder builder = new Builder(endpoint, database, null);
        if (scheme.equals("grpcs")) {
            builder.withSecureConnection();
        } else if (!scheme.equals("grpc")) {
            throw new IllegalArgumentException("Unknown protocol '" + scheme + "' in connection string");
        }
        return builder;
    }

    private <RespT> Result<RespT> CancelResultDueToShutdown() {
        Issue issue = Issue.of("Request was not sent: transport is shutting down", Issue.Severity.ERROR);
        return Result.fail(StatusCode.CLIENT_CANCELLED, issue);
    }

    public abstract String getEndpointByNodeId(int nodeId);

    public <ReqT, RespT> CompletableFuture<Result<RespT>> unaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            long deadlineAfter) {
        return unaryCall(method, request, GrpcRequestSettings.newBuilder()
                .withDeadlineAfter(deadlineAfter)
                .build());
    }

    public <ReqT, RespT> CompletableFuture<Result<RespT>> unaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            GrpcRequestSettings settings) {
        CallOptions callOptions = this.callOptions;
        if (settings.getDeadlineAfter() > 0) {
            final long now = System.nanoTime();
            if (now >= settings.getDeadlineAfter()) {
                return completedFuture(deadlineExpiredResult(method));
            }
            callOptions = this.callOptions.withDeadlineAfter(settings.getDeadlineAfter() - now, TimeUnit.NANOSECONDS);
        } else if (defaultReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defaultReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        CompletableFuture<Result<RespT>> promise = new CompletableFuture<>();

        if (!shutdown) {
            return makeUnaryCall(method, request, callOptions, settings, promise);
        } else {
            promise.complete(CancelResultDueToShutdown());
        }
        return promise;
    }

    protected abstract <ReqT, RespT> CompletableFuture<Result<RespT>> makeUnaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            CompletableFuture<Result<RespT>> promise);

    public <ReqT, RespT> void unaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            Consumer<Result<RespT>> consumer,
            long deadlineAfter) {
        unaryCall(method, request, consumer, GrpcRequestSettings.newBuilder()
                .withDeadlineAfter(deadlineAfter)
                .build());
    }

    public <ReqT, RespT> void unaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            Consumer<Result<RespT>> consumer,
            GrpcRequestSettings settings) {
        CallOptions callOptions = this.callOptions;
        if (settings.getDeadlineAfter() > 0) {
            final long now = System.nanoTime();
            if (now >= settings.getDeadlineAfter()) {
                consumer.accept(deadlineExpiredResult(method));
                return;
            }
            callOptions = this.callOptions.withDeadlineAfter(settings.getDeadlineAfter() - now, TimeUnit.NANOSECONDS);
        } else if (defaultReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defaultReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        if (!shutdown) {
            makeUnaryCall(method, request, callOptions, settings, consumer);
        } else {
            consumer.accept(CancelResultDueToShutdown());
        }
    }

    protected abstract <ReqT, RespT> void makeUnaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            Consumer<Result<RespT>> consumer);

    public <ReqT, RespT> void unaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            BiConsumer<RespT, Status> consumer,
            long deadlineAfter) {
        unaryCall(method, request, consumer, GrpcRequestSettings.newBuilder()
                .withDeadlineAfter(deadlineAfter)
                .build());
    }

    public <ReqT, RespT> void unaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            BiConsumer<RespT, Status> consumer,
            GrpcRequestSettings settings) {
        CallOptions callOptions = this.callOptions;
        if (settings.getDeadlineAfter() > 0) {
            final long now = System.nanoTime();
            if (now >= settings.getDeadlineAfter()) {
                consumer.accept(null, deadlineExpiredStatus(method));
                return;
            }
            callOptions = this.callOptions.withDeadlineAfter(settings.getDeadlineAfter() - now, TimeUnit.NANOSECONDS);
        } else if (defaultReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defaultReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        if (!shutdown) {
            makeUnaryCall(method, request, callOptions, settings, consumer);
        } else {
            consumer.accept(null, Status.CANCELLED);
        }
    }

    protected abstract <ReqT, RespT> void makeUnaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            BiConsumer<RespT, Status> consumer);

    public <ReqT, RespT> StreamControl serverStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            StreamObserver<RespT> observer,
            long deadlineAfter) {
        return serverStreamCall(method, request, observer,
                GrpcRequestSettings.newBuilder()
                        .withDeadlineAfter(deadlineAfter)
                        .build());
    }

    public <ReqT, RespT> StreamControl serverStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            StreamObserver<RespT> observer,
            GrpcRequestSettings settings) {
        CallOptions callOptions = this.callOptions;
        if (settings.getDeadlineAfter() > 0) {
            final long now = System.nanoTime();
            if (now >= settings.getDeadlineAfter()) {
                observer.onError(GrpcStatuses.toStatus(deadlineExpiredStatus(method)));
                return () -> {};
            }
            callOptions = this.callOptions.withDeadlineAfter(settings.getDeadlineAfter() - now, TimeUnit.NANOSECONDS);
        } else if (defaultReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defaultReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        if (!shutdown) {
            return makeServerStreamCall(method, request, callOptions, settings, observer);
        } else {
            observer.onError(CancelResultDueToShutdown().toStatus());
            return () -> {};
        }
    }

    protected abstract <ReqT, RespT> StreamControl makeServerStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            StreamObserver<RespT> observer);

    private <ReqT> OutStreamObserver<ReqT> makeEmptyObserverStub() {
        return new OutStreamObserver<ReqT>() {
            @Override
            public void onNext(ReqT value) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };
    }

    public <ReqT, RespT> OutStreamObserver<ReqT> bidirectionalStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            StreamObserver<RespT> observer,
            long deadlineAfter) {
        return bidirectionalStreamCall(method, observer,
                GrpcRequestSettings.newBuilder()
                        .withDeadlineAfter(deadlineAfter)
                        .build());
    }

    public <ReqT, RespT> OutStreamObserver<ReqT> bidirectionalStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            StreamObserver<RespT> observer,
            GrpcRequestSettings settings) {
        CallOptions callOptions = this.callOptions;
        if (settings.getDeadlineAfter() > 0) {
            final long now = System.nanoTime();
            if (now >= settings.getDeadlineAfter()) {
                observer.onError(GrpcStatuses.toStatus(deadlineExpiredStatus(method)));
                return makeEmptyObserverStub();
            }
            callOptions = this.callOptions.withDeadlineAfter(settings.getDeadlineAfter() - now, TimeUnit.NANOSECONDS);
        } else if (defaultReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defaultReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        if (!shutdown) {
            return makeBidirectionalStreamCall(method, callOptions, settings, observer);
        } else {
            observer.onError(CancelResultDueToShutdown().toStatus());
            return makeEmptyObserverStub();
        }
    }

    protected abstract <ReqT, RespT> OutStreamObserver<ReqT> makeBidirectionalStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            StreamObserver<RespT> observer);

    protected static <ReqT, RespT> void sendOneRequest(
            ClientCall<ReqT, RespT> call,
            ReqT request,
            GrpcRequestSettings settings,
            ClientCall.Listener<RespT> listener) {
        try {
            Metadata headers = settings.getExtraHeaders();
            call.start(listener, headers != null ? headers : new Metadata());
            call.request(1);
            call.sendMessage(request);
            call.halfClose();
        } catch (Throwable t) {
            try {
                call.cancel(null, t);
            } catch (Throwable ex) {
                logger.error
                        ("Exception encountered while closing the call", ex);
            }
            listener.onClose(Status.INTERNAL.withCause(t), null);
        }
    }

    protected static <ReqT, RespT> OutStreamObserver<ReqT> asyncBidiStreamingCall(
            ClientCall<ReqT, RespT> call,
            GrpcRequestSettings settings,
            StreamObserver<RespT> responseObserver) {
        AsyncBidiStreamingOutAdapter<ReqT, RespT> adapter
                = new AsyncBidiStreamingOutAdapter<>(call);
        AsyncBidiStreamingInAdapter<ReqT, RespT> responseListener
                = new AsyncBidiStreamingInAdapter<>(responseObserver, adapter, null, settings.getTrailersHandler());
        Metadata extra = settings.getExtraHeaders();
        call.start(responseListener, extra != null ? extra :new Metadata());
        responseListener.onStart();
        return adapter;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public OperationTray getOperationTray() {
        return operationTray;
    }

    @Override
    public void close() {
        shutdown = true;
        operationTray.close();
    }

    protected static Channel interceptChannel(ManagedChannel realChannel, ChannelSettings channelSettings) {
        if (channelSettings.getDatabase() == null) {
            return realChannel;
        }

        Metadata extraHeaders = new Metadata();
        extraHeaders.put(YdbHeaders.DATABASE, channelSettings.getDatabase());
        extraHeaders.put(YdbHeaders.BUILD_INFO, channelSettings.getVersion());
        ClientInterceptor interceptor = MetadataUtils.newAttachHeadersInterceptor(extraHeaders);
        return ClientInterceptors.intercept(realChannel, interceptor);
    }

    private static CallOptions createCallOptions(Builder builder) {
        CallOptions callOptions = CallOptions.DEFAULT;
        AuthProvider authProvider = builder.getAuthProvider();
        if (authProvider != NopAuthProvider.INSTANCE) {
            callOptions = callOptions.withCallCredentials(new YdbCallCredentials(authProvider));
        }
        if (builder.getCallExecutor() != MoreExecutors.directExecutor()) {
            callOptions = callOptions.withExecutor(builder.getCallExecutor());
        }
        return callOptions;
    }

    private static <T> Result<T> deadlineExpiredResult(MethodDescriptor<?, T> method) {
        String message = "deadline expired before calling method " + method.getFullMethodName();
        return Result.fail(StatusCode.CLIENT_DEADLINE_EXPIRED, Issue.of(message, Issue.Severity.ERROR));
    }

    private static Status deadlineExpiredStatus(MethodDescriptor<?, ?> method) {
        String message = "deadline expired before calling method " + method.getFullMethodName();
        return Status.DEADLINE_EXCEEDED.withDescription(message);
    }

    private static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    /**
     * BUILDER
     */
    @ParametersAreNonnullByDefault
    public static final class Builder extends RpcTransportBuilder<GrpcTransport, Builder> {
        private final String endpoint;
        private String database;
        private final List<HostAndPort> hosts;
        private byte[] cert = null;
        private boolean useTLS = false;
        private Consumer<NettyChannelBuilder> channelInitializer = (cb) -> {
        };
        private String localDc;
        private Duration endpointsDiscoveryPeriod = Duration.ofSeconds(60);
        private DiscoveryMode discoveryMode = DiscoveryMode.SYNC;
        private TransportImplType transportImplType = TransportImplType.GRPC_TRANSPORT_IMPL;
        private BalancingSettings balancingSettings;
        /**
         * can cause leaks
         * https://github.com/grpc/grpc-java/issues/9340
         */
        private boolean enableRetry = false;

        private Builder(@Nullable String endpoint, @Nullable String database, @Nullable List<HostAndPort> hosts) {
            this.endpoint = endpoint;
            this.database = database;
            this.hosts = hosts;
        }

        @Nullable
        public List<HostAndPort> getHosts() {
            return hosts;
        }

        @Nullable
        public byte[] getCert() {
            return cert;
        }

        public boolean getUseTls() {
            return useTLS;
        }

        @Nullable
        public String getEndpoint() {
            return endpoint;
        }

        public Duration getEndpointsDiscoveryPeriod() {
            return endpointsDiscoveryPeriod;
        }

        @Nullable
        public String getDatabase() {
            return database;
        }

        public Builder withDataBase(String dataBase) {
            this.database = dataBase;
            return this;
        }

        public String getVersionString() {
            return Version.getVersion()
                    .map(version -> "ydb-java-sdk/" + version)
                    .orElse("unknown-version");
        }

        public Consumer<NettyChannelBuilder> getChannelInitializer() {
            return channelInitializer;
        }

        public String getLocalDc() {
            return localDc;
        }

        public DiscoveryMode getDiscoveryMode() {
            return discoveryMode;
        }

        public BalancingSettings getBalancingSettings() {
            return balancingSettings;
        }

        public Builder withChannelInitializer(Consumer<NettyChannelBuilder> channelInitializer) {
            this.channelInitializer = checkNotNull(channelInitializer, "channelInitializer is null");
            return this;
        }

        public Builder withLocalDataCenter(String dc) {
            this.localDc = dc;
            return this;
        }

        public Builder withEndpointsDiscoveryPeriod(Duration period) {
            this.endpointsDiscoveryPeriod = period;
            return this;
        }

        public Builder withSecureConnection(byte[] cert) {
            this.cert = cert.clone();
            this.useTLS = true;
            return this;
        }

        public Builder withSecureConnection() {
            this.useTLS = true;
            return this;
        }

        public Builder withDiscoveryMode(DiscoveryMode discoveryMode) {
            this.discoveryMode = discoveryMode;
            return this;
        }

        public Builder withTransportImplType(TransportImplType transportImplType) {
            this.transportImplType = transportImplType;
            return this;
        }

        public Builder withBalancingSettings(BalancingSettings balancingSettings) {
            this.balancingSettings = balancingSettings;
            return this;
        }

        public boolean isEnableRetry() {
            return enableRetry;
        }

        @ExperimentalApi("https://github.com/grpc/grpc-java/issues/9340")
        public Builder enableRetry() {
            this.enableRetry = true;
            return this;
        }

        @ExperimentalApi("https://github.com/grpc/grpc-java/issues/9340")
        public Builder disableRetry() {
            this.enableRetry = false;
            return this;
        }

        @Override
        public GrpcTransport build() {
            switch (transportImplType) {
                case YDB_TRANSPORT_IMPL:
                    return new YdbTransportImpl(this);
                case GRPC_TRANSPORT_IMPL:
                default:
                    return new GrpcTransportImpl(this);
            }
        }
    }
}
