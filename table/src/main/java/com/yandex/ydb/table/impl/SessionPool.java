package com.yandex.ydb.table.impl;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import com.yandex.ydb.table.impl.SessionImpl.State;
import com.yandex.ydb.table.impl.pool.AsyncPool;
import com.yandex.ydb.table.impl.pool.FixedAsyncPool;
import com.yandex.ydb.table.impl.pool.PooledObjectHandler;
import com.yandex.ydb.table.settings.CreateSessionSettings;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;


/**
 * @author Sergey Polovko
 */
final class SessionPool implements PooledObjectHandler<SessionImpl> {

    private final TableClientImpl tableClient;
    private final AsyncPool<SessionImpl> pool;
    private final Timer timer;

    SessionPool(TableClientImpl tableClient, SessionPoolOptions options) {
        this.tableClient = tableClient;
        this.timer = new HashedWheelTimer(new DefaultThreadFactory("SessionPoolTimer"));
        this.pool = new FixedAsyncPool<>(
            this,
            timer,
            options.getMinSize(),
            options.getMaxSize(),
            options.getMaxSize() * 2,
            options.getKeepAliveTimeMillis(),
            options.getMaxIdleTimeMillis());
    }

    @Override
    public CompletableFuture<SessionImpl> create() {
        return tableClient.createSessionImpl(new CreateSessionSettings())
            .thenApply(r -> (SessionImpl) r.expect("cannot create session"));
    }

    @Override
    public CompletableFuture<Void> destroy(SessionImpl s) {
        return s.close()
            .thenAccept(r -> r.expect("cannot close session: " + s.getId()));
    }

    @Override
    public boolean isValid(SessionImpl s) {
        return s.getState() != State.BROKEN;
    }

    @Override
    public CompletableFuture<Void> keepAlive(SessionImpl s) {
        return s.keepAlive()
            .thenAccept(r -> r.expect("cannot keep alive session: " + s.getId()));
    }

    CompletableFuture<SessionImpl> acquire(Duration timeout) {
        return pool.acquire(timeout);
    }

    void release(SessionImpl session) {
        pool.release(session);
    }

    void close() {
        try {
            pool.close();
        } finally {
            timer.stop();
        }
    }
}
