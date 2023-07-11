package com.yandex.ydb.table.impl;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.grpc.GrpcRequestSettings;
import com.yandex.ydb.core.grpc.YdbHeaders;
import com.yandex.ydb.core.rpc.OperationTray;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.TableClient;
import com.yandex.ydb.table.YdbTable;
import com.yandex.ydb.table.rpc.TableRpc;
import com.yandex.ydb.table.settings.CreateSessionSettings;
import com.yandex.ydb.table.stats.SessionPoolStats;
import com.yandex.ydb.table.utils.OperationParamUtils;
import com.yandex.ydb.table.utils.RequestSettingsUtils;
import io.grpc.Metadata;


/**
 * @author Sergey Polovko
 */
final class TableClientImpl implements TableClient {
    private final static String SERVER_BALANCER_HINT = "session-balancer";

    private final TableRpc tableRpc;
    @Nullable
    private final SessionPool sessionPool;
    private final OperationTray operationTray;

    private final int queryCacheSize;
    private final boolean keepQueryText;

    TableClientImpl(TableClientBuilderImpl builder) {
        this.tableRpc = builder.tableRpc;
        this.sessionPool = builder.sessionPoolOptions.getMaxSize() != 0
            ? new SessionPool(this, builder.sessionPoolOptions)
            : null;
        this.operationTray = tableRpc.getOperationTray();

        this.queryCacheSize = builder.queryCacheSize;
        this.keepQueryText = builder.keepQueryText;
    }

    @Override
    public CompletableFuture<Result<Session>> createSession(CreateSessionSettings settings) {
        return createSessionImpl(settings, null);
    }

    @Override
    public SessionPoolStats getSessionPoolStats() {
        return sessionPool.getStats();
    }

    CompletableFuture<Result<Session>> createSessionImpl(CreateSessionSettings settings, @Nullable SessionPool sessionPool) {
        YdbTable.CreateSessionRequest request = YdbTable.CreateSessionRequest.newBuilder()
            .setOperationParams(OperationParamUtils.fromRequestSettings(settings))
            .build();

        // Use server-side session balancer
        final GrpcRequestSettings grpcRequestSettings = GrpcRequestSettings.newBuilder()
                .withDeadlineAfter(RequestSettingsUtils.calculateDeadlineAfter(settings))
                .withExtraHeaders(getClientCapabilities())
                .build();

        return tableRpc.createSession(request, grpcRequestSettings)
            .thenCompose(response -> {
                if (!response.isSuccess()) {
                    return CompletableFuture.completedFuture(response.cast());
                }
                return operationTray.waitResult(
                    response.expect("createSession()").getOperation(),
                    YdbTable.CreateSessionResult.class,
                    result -> new SessionImpl(result.getSessionId(), tableRpc, sessionPool, queryCacheSize, keepQueryText),
                        grpcRequestSettings);
            });
    }

    @Override
    public CompletableFuture<Result<Session>> getOrCreateSession(Duration timeout) {
        if (sessionPool == null) {
            return createSessionImpl(new CreateSessionSettings().setTimeout(timeout), null);
        }
        return sessionPool.acquire(timeout);
    }

    @Override
    public void close() {
        if (sessionPool != null) {
            sessionPool.close();
        }
        tableRpc.close();
    }

    private Metadata getClientCapabilities() {
        Metadata metadata = new Metadata();
        metadata.put(YdbHeaders.YDB_CLIENT_CAPABILITIES, SERVER_BALANCER_HINT);
        return metadata;
    }
}
