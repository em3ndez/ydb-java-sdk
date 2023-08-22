package com.yandex.ydb.core.grpc.impl.ydb;

import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.net.HostAndPort;
import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.grpc.AsyncBidiStreamingInAdapter;
import com.yandex.ydb.core.grpc.AsyncBidiStreamingOutAdapter;
import com.yandex.ydb.core.grpc.BalancingSettings;
import com.yandex.ydb.core.grpc.ChannelSettings;
import com.yandex.ydb.core.grpc.DiscoveryMode;
import com.yandex.ydb.core.grpc.EndpointInfo;
import com.yandex.ydb.core.grpc.GrpcDiscoveryRpc;
import com.yandex.ydb.core.grpc.GrpcRequestSettings;
import com.yandex.ydb.core.grpc.GrpcTransport;
import com.yandex.ydb.core.grpc.ServerStreamToObserver;
import com.yandex.ydb.core.grpc.TransportImplType;
import com.yandex.ydb.core.grpc.UnaryStreamToBiConsumer;
import com.yandex.ydb.core.grpc.UnaryStreamToConsumer;
import com.yandex.ydb.core.grpc.UnaryStreamToFuture;
import com.yandex.ydb.core.rpc.OutStreamObserver;
import com.yandex.ydb.core.rpc.StreamControl;
import com.yandex.ydb.core.rpc.StreamObserver;
import com.yandex.ydb.core.utils.Async;
import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikolay Perfilov
 */
public class YdbTransportImpl extends GrpcTransport {
    // Interval between discovery requests when everything is ok
    private static final long DISCOVERY_PERIOD_NORMAL_SECONDS = 60;
    // Interval between discovery requests when pessimization threshold is exceeded
    private static final long DISCOVERY_PERIOD_MIN_SECONDS = 5;
    // Maximum percent of endpoints pessimized by transport errors to start recheck
    private static final long DISCOVERY_PESSIMIZATION_THRESHOLD = 50;

    private static final Logger logger = LoggerFactory.getLogger(YdbTransportImpl.class);

    private final GrpcDiscoveryRpc discoveryRpc;
    private final EndpointPool endpointPool;
    private final GrpcChannelPool channelPool;
    private final PeriodicDiscoveryTask periodicDiscoveryTask = new PeriodicDiscoveryTask();

    public YdbTransportImpl(GrpcTransport.Builder builder) {
        super(builder);

        this.discoveryRpc = createDiscoveryRpc(builder);

        BalancingSettings balancingSettings = builder.getBalancingSettings();
        if (balancingSettings == null) {
            if (builder.getLocalDc() == null) {
                balancingSettings = new BalancingSettings();
            } else {
                balancingSettings = BalancingSettings.fromLocation(builder.getLocalDc());
            }
        }
        logger.debug("creating YDB transport with {}", balancingSettings);

        this.endpointPool = new EndpointPool(
            () -> discoveryRpc.listEndpoints(
                database,
                GrpcRequestSettings.newBuilder()
                        .withDeadlineAfter(System.nanoTime() + Duration.ofSeconds(DISCOVERY_TIMEOUT_SECONDS).toNanos())
                        .build()
            ),
            balancingSettings
        );

        periodicDiscoveryTask.start();

        channelPool = new GrpcChannelPool(ChannelSettings.fromBuilder(builder), endpointPool.getRecords());
    }

    private GrpcDiscoveryRpc createDiscoveryRpc(Builder builder) {
        String endpoint = builder.getEndpoint();
        if (endpoint == null || builder.getDatabase() == null) {
            throw new IllegalArgumentException(
                    "YDB transport implementation does not support multiple hosts settings (GrpcTransport.forHosts). " +
                            "Use GrpcTransport.forEndpoint instead." +
                            " Or use Grpc transport implementation (TransportImplType.GRPC_TRANSPORT_IMPL)");
        }
        HostAndPort hostAndPort = HostAndPort.fromString(endpoint);
        GrpcTransport.Builder transportBuilder = GrpcTransport
                .forHost(hostAndPort.getHost(), hostAndPort.getPortOrDefault(DEFAULT_PORT))
                .withAuthProvider(builder.getAuthProvider())
                .withCallExecutor(builder.getCallExecutor())
                .withDataBase(builder.getDatabase())
                .withChannelInitializer(builder.getChannelInitializer())
                .withTransportImplType(TransportImplType.GRPC_TRANSPORT_IMPL)
                .withDiscoveryMode(DiscoveryMode.SYNC);

        if (builder.getUseTls()) {
            if (builder.getCert() != null) {
                transportBuilder.withSecureConnection(builder.getCert());
            } else {
                transportBuilder.withSecureConnection();
            }
        }
        GrpcTransport transport = transportBuilder.build();
        return new GrpcDiscoveryRpc(transport);
    }

    private void checkEndpointResponse(Result<?> result, String endpoint) {
        if (result.getCode().isTransportError()) {
            endpointPool.pessimizeEndpoint(endpoint);
        }
    }

    private void checkEndpointResponse(Status grpcStatus, String endpoint) {
        if (!grpcStatus.isOk()) {
            endpointPool.pessimizeEndpoint(endpoint);
        }
    }

    @Override
    public String getEndpointByNodeId(int nodeId) {
        return endpointPool.getEndpointByNodeId(nodeId);
    }

    @Override
    protected <ReqT, RespT> CompletableFuture<Result<RespT>> makeUnaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            CompletableFuture<Result<RespT>> promise) {
        EndpointInfo preferredEndpoint = settings.getPreferredEndpoint();
        GrpcChannel channel = channelPool.getChannel(endpointPool.getEndpoint(
                preferredEndpoint != null ? preferredEndpoint.getEndpoint() : null));
        ClientCall<ReqT, RespT> call = channel.channel.newCall(method, callOptions);
        if (logger.isDebugEnabled()) {
            logger.debug("Sending request to {}, method `{}', request: `{}'", channel.getEndpoint(), method,
                    request);
        }
        sendOneRequest(call, request, settings, new UnaryStreamToFuture<>(promise, settings.getTrailersHandler()));
        return promise.thenApply(result -> {
            checkEndpointResponse(result, channel.getEndpoint());
            return result;
        });
    }

    @Override
    protected <ReqT, RespT> void makeUnaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            Consumer<Result<RespT>> consumer) {
        EndpointInfo preferredEndpoint = settings.getPreferredEndpoint();
        GrpcChannel channel = channelPool.getChannel(endpointPool.getEndpoint(
                preferredEndpoint != null ? preferredEndpoint.getEndpoint() : null));
        ClientCall<ReqT, RespT> call = channel.channel.newCall(method, callOptions);
        if (logger.isDebugEnabled()) {
            logger.debug("Sending request to {}, method `{}', request: `{}'", channel.getEndpoint(), method,
                    request);
        }
        sendOneRequest(
                call,
                request,
                settings,
                new UnaryStreamToConsumer<>(
                        consumer,
                        (status) -> {
                            checkEndpointResponse(status, channel.getEndpoint());
                        },
                        settings.getTrailersHandler()
                )
        );
    }

    @Override
    protected <ReqT, RespT> void makeUnaryCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            BiConsumer<RespT, Status> consumer) {
        EndpointInfo preferredEndpoint = settings.getPreferredEndpoint();
        GrpcChannel channel = channelPool.getChannel(endpointPool.getEndpoint(
                preferredEndpoint != null ? preferredEndpoint.getEndpoint() : null));
        ClientCall<ReqT, RespT> call = channel.channel.newCall(method, callOptions);
        if (logger.isDebugEnabled()) {
            logger.debug("Sending request to {}, method `{}', request: `{}'", channel.channel.authority(),method,
                    request);
        }
        sendOneRequest(
                call,
                request,
                settings,
                new UnaryStreamToBiConsumer<>(
                        consumer,
                        (status) -> {
                            checkEndpointResponse(status, channel.getEndpoint());
                        },
                        settings.getTrailersHandler()
                )
        );
    }

    @Override
    protected <ReqT, RespT> StreamControl makeServerStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            ReqT request,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            StreamObserver<RespT> observer) {
        EndpointInfo preferredEndpoint = settings.getPreferredEndpoint();
        GrpcChannel channel = channelPool.getChannel(endpointPool.getEndpoint(
                preferredEndpoint != null ? preferredEndpoint.getEndpoint() : null));
        ClientCall<ReqT, RespT> call = channel.channel.newCall(method, callOptions);
        sendOneRequest(
                call,
                request,
                settings,
                new ServerStreamToObserver<>(
                        observer,
                        call,
                        (status) -> {
                            checkEndpointResponse(status, channel.getEndpoint());
                        }
                )
        );
        return () -> {
            // message in CancellationException prevents NPE in Issue.of
            call.cancel("Cancelled on user request", new CancellationException("Cancelled on user request"));
        };
    }

    @Override
    protected <ReqT, RespT> OutStreamObserver<ReqT> makeBidirectionalStreamCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            GrpcRequestSettings settings,
            StreamObserver<RespT> observer) {
        EndpointInfo preferredEndpoint = settings.getPreferredEndpoint();
        GrpcChannel channel = channelPool.getChannel(endpointPool.getEndpoint(
                preferredEndpoint != null ? preferredEndpoint.getEndpoint() : null));
        ClientCall<ReqT, RespT> call = channel.channel.newCall(method, callOptions);
        AsyncBidiStreamingOutAdapter<ReqT, RespT> adapter
                = new AsyncBidiStreamingOutAdapter<>(call);
        AsyncBidiStreamingInAdapter<ReqT, RespT> responseListener = new AsyncBidiStreamingInAdapter<>(
                    observer,
                    adapter,
                    (status) -> {
                        checkEndpointResponse(status, channel.getEndpoint());
                    },
                    settings.getTrailersHandler()
                );
        Metadata extra = settings.getExtraHeaders();
        call.start(responseListener, extra != null ? extra :new Metadata());
        responseListener.onStart();
        return adapter;
    }

    @Override
    public void close() {
        super.close();
        periodicDiscoveryTask.stop();
        channelPool.shutdown(WAIT_FOR_CLOSING_MS);
    }

    /**
     * PERIODIC DISCOVERY TASK
     */
    private final class PeriodicDiscoveryTask implements TimerTask {
        private volatile boolean stopped = false;
        private Timeout scheduledHandle = null;

        void stop() {
            logger.debug("stopping PeriodicDiscoveryTask");
            stopped = true;
            if (scheduledHandle != null) {
                scheduledHandle.cancel();
                scheduledHandle = null;
            }
        }

        void start() {
            CompletableFuture<EndpointPool.EndpointUpdateResultData> firstRunFuture = runDiscovery(true);
            if (firstRunFuture == null) {
                // TODO: Retry?
                throw new RuntimeException("Couldn't perform discovery on GrpcTransport start");
            }
            // Waiting for first discovery result...
            EndpointPool.EndpointUpdateResultData firstRunData = firstRunFuture.join();
            if (!firstRunData.discoveryStatus.isSuccess()) {
                throw new RuntimeException("Couldn't perform discovery on GrpcTransport start with status: "
                        + firstRunData.discoveryStatus);
            }
        }

        @Override
        public void run(Timeout timeout) {
            int pessimizationRatio = endpointPool.getPessimizationRatio();
            if (pessimizationRatio > DISCOVERY_PESSIMIZATION_THRESHOLD) {
                logger.info("launching discovery due to pessimization threshold is exceeded: {} is more than {}",
                        pessimizationRatio, DISCOVERY_PESSIMIZATION_THRESHOLD);
                runDiscovery(false);
            } else if (endpointPool.getTimeSinceLastUpdate().getSeconds() > DISCOVERY_PERIOD_NORMAL_SECONDS) {
                logger.debug("launching discovery in normal mode");
                runDiscovery(false);
            } else {
                scheduleNextDiscovery();
                logger.trace("no need to run discovery yet");
            }
        }

        private CompletableFuture<EndpointPool.EndpointUpdateResultData> runDiscovery(boolean firstRun) {
            if (stopped) {
                return null;
            }

            EndpointPool.EndpointUpdateResult updateResult = endpointPool.updateAsync();
            assert !firstRun || updateResult.discoveryWasPerformed;
            if (!updateResult.discoveryWasPerformed) {
                logger.debug("discovery was not performed: already in progress");
                scheduleNextDiscovery();
                return null;
            }

            logger.debug("discovery was requested (firstRun = {}), waiting for result...", firstRun);
            return updateResult.data
                    .thenApply(updateResultData -> {
                        if (updateResultData.discoveryStatus.isSuccess()) {
                            logger.debug("discovery was successfully performed");
                            if (channelPool != null) {
                                channelPool.removeChannels(updateResultData.removed, WAIT_FOR_CLOSING_MS);
                                logger.debug("channelPool.removeChannels executed successfully");
                            }
                        } else {
                            logger.warn("couldn't perform discovery with status: {}", updateResultData.discoveryStatus);
                        }
                        scheduleNextDiscovery();
                        return updateResultData;
                    })
                    .exceptionally(e -> {
                        logger.warn("couldn't perform discovery with exception: {}", e.toString());
                        scheduleNextDiscovery();
                        return null;
                    });
        }

        void scheduleNextDiscovery() {
            scheduledHandle = Async.runAfter(this, DISCOVERY_PERIOD_MIN_SECONDS, TimeUnit.SECONDS);
        }
    }
}
