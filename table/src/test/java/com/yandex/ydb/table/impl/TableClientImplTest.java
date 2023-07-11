package com.yandex.ydb.table.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.yandex.ydb.OperationProtos;
import com.yandex.ydb.StatusCodesProtos;
import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.StatusCode;
import com.yandex.ydb.core.grpc.GrpcRequestSettings;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.TableClient;
import com.yandex.ydb.table.TableRpcStub;
import com.yandex.ydb.table.YdbTable;
import com.yandex.ydb.table.rpc.TableRpc;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author Sergey Polovko
 */
public class TableClientImplTest {
    public static <M extends Message> OperationProtos.Operation resultOperation(M message) {
        return OperationProtos.Operation.newBuilder()
            .setId("fake_id")
            .setStatus(StatusCodesProtos.StatusIds.StatusCode.SUCCESS)
            .setReady(true)
            .setResult(Any.pack(message))
            .build();
    }

    public static OperationProtos.Operation successOperation() {
        return OperationProtos.Operation.newBuilder()
            .setId("fake_id")
            .setStatus(StatusCodesProtos.StatusIds.StatusCode.SUCCESS)
            .setReady(true)
            .build();
    }

    private class FakeRpc extends TableRpcStub {
        private final Set<String> sessionIDs = new HashSet<>();
        private int counter = 0;

        public int size() {
            return sessionIDs.size();
        }

        @Override
        public CompletableFuture<Result<YdbTable.CreateSessionResponse>> createSession(
                YdbTable.CreateSessionRequest request, GrpcRequestSettings settings) {

            counter += 1;
            String id = "fake?node_id=1&id=" + counter;
            YdbTable.CreateSessionResult result = YdbTable.CreateSessionResult.newBuilder()
                    .setSessionId(id)
                    .build();
            YdbTable.CreateSessionResponse response = YdbTable.CreateSessionResponse.newBuilder()
                    .setOperation(resultOperation(result))
                    .build();

            sessionIDs.add(id);
            return CompletableFuture.completedFuture(Result.success(response));
        }

        @Override
        public CompletableFuture<Result<YdbTable.DeleteSessionResponse>> deleteSession(
                YdbTable.DeleteSessionRequest request, GrpcRequestSettings settings) {
            String id = request.getSessionId();
            YdbTable.DeleteSessionResponse response = YdbTable.DeleteSessionResponse
                    .newBuilder()
                    .setOperation(successOperation())
                    .build();

            if (sessionIDs.contains(id)) {
                sessionIDs.remove(id);
                return CompletableFuture.completedFuture(Result.success(response));
            } else {
                return CompletableFuture.completedFuture(Result.fail(StatusCode.BAD_SESSION));
            }
        }

    }

    @Test
    public void createSessionAndRelease() throws InterruptedException, ExecutionException {
        FakeRpc fakeRpc = new FakeRpc();
        try (TableClient client = TableClient.newClient(fakeRpc).build()) {
            // Test TableClient interface - without session pool
            Session session1 = client.createSession().join().expect("cannot create session");
            // Server has 1 session
            Assert.assertEquals(1, fakeRpc.size());
            // Session1 not used by pool - can't release
            Assert.assertFalse(session1.release());

            // Test SessionSupplier interface - use session pool
            Session session2 = client.getOrCreateSession(Duration.ZERO).join().expect("cannot create session");
            // Server has 2 session
            Assert.assertEquals(2, fakeRpc.size());
            // Session in pool - successful release
            Assert.assertTrue(session2.release());
            // But server still has 2 session
            Assert.assertEquals(2, fakeRpc.size());

            // Session1 not used by pool - manual close
            Assert.assertTrue(session1.close().get().isSuccess());
            // After closing server still has 1 session
            Assert.assertEquals(1, fakeRpc.size());
            // Repeat close return error
            Assert.assertFalse(session1.close().get().isSuccess());
        }

        // After closing TableClient SessionPool released all sessions
        Assert.assertEquals(0, fakeRpc.size());
    }

    @Test
    public void unavailableSessions() {
        TableRpc fakeRpc = new TableRpcStub() {
            @Override
            public CompletableFuture<Result<YdbTable.CreateSessionResponse>> createSession(
                YdbTable.CreateSessionRequest request, GrpcRequestSettings settings) {
                return CompletableFuture.completedFuture(Result.fail(StatusCode.TRANSPORT_UNAVAILABLE));
            }
        };

        try (TableClient client = TableClient.newClient(fakeRpc).build()) {
            // Test TableClient interface
            Result<Session> sessionResult = client.createSession().join();
            Assert.assertFalse(sessionResult.isSuccess());
            Assert.assertEquals(StatusCode.TRANSPORT_UNAVAILABLE, sessionResult.getCode());

            // Test SessionSupplier interface
            sessionResult = client.getOrCreateSession(Duration.ZERO).join();
            Assert.assertFalse(sessionResult.isSuccess());
            Assert.assertEquals(StatusCode.TRANSPORT_UNAVAILABLE, sessionResult.getCode());
        }
    }

    @Test
    public void testPoolPendingTasks() throws InterruptedException, ExecutionException {
        FakeRpc fakeRpc = new FakeRpc();
        try (TableClient client = TableClient.newClient(fakeRpc).sessionPoolSize(0, 5).build()) {
            int maxSize = client.getSessionPoolStats().getMaxSize();

            List<CompletableFuture<Result<Session>>> futures = new ArrayList<>();

            // create 3*maxSize requests of session creating
            for (int idx = 0; idx < maxSize * 3; idx += 1) {
                futures.add(client.getOrCreateSession(Duration.ofSeconds(5)));
            }

            Assert.assertEquals(maxSize, fakeRpc.size());
            Assert.assertEquals(maxSize, client.getSessionPoolStats().getAcquiredCount());
            Assert.assertEquals(0, client.getSessionPoolStats().getIdleCount());
            Assert.assertEquals(2 * maxSize, client.getSessionPoolStats().getPendingAcquireCount());

            Assert.assertEquals(3 * maxSize, futures.size());
            Assert.assertEquals(maxSize, futures.stream().filter(CompletableFuture::isDone).count());
            Assert.assertEquals(0, futures.stream().filter(CompletableFuture::isCancelled).count());

            // cancel half of requests
            for (int idx = maxSize; idx < maxSize * 3; idx += 2) {
                futures.get(idx).cancel(true);
            }

            Assert.assertEquals(maxSize, fakeRpc.size());
            Assert.assertEquals(maxSize, client.getSessionPoolStats().getAcquiredCount());
            Assert.assertEquals(0, client.getSessionPoolStats().getIdleCount());
            Assert.assertEquals(2 * maxSize, client.getSessionPoolStats().getPendingAcquireCount());

            // release completed and canceled futures
            List<CompletableFuture<Result<Session>>> completed = futures.stream()
                    .filter(CompletableFuture::isDone).collect(Collectors.toList());
            completed.forEach(future -> {
                if (!future.isCancelled()) {
                    future.join().expect("can't get session").release();
                }
            });
            futures.removeAll(completed);

            // After releasing next pending futures must be completed
            Assert.assertEquals(maxSize, fakeRpc.size());
            Assert.assertEquals(maxSize, client.getSessionPoolStats().getAcquiredCount());
            Assert.assertEquals(0, client.getSessionPoolStats().getIdleCount());
            Assert.assertEquals(0, client.getSessionPoolStats().getPendingAcquireCount());

            Assert.assertEquals(maxSize, futures.size());
            Assert.assertEquals(maxSize, futures.stream().filter(CompletableFuture::isDone).count());
            Assert.assertEquals(0, futures.stream().filter(CompletableFuture::isCancelled).count());

            // release completed futures
            completed = futures.stream().filter(CompletableFuture::isDone).collect(Collectors.toList());
            completed.forEach(future -> {
                if (!future.isCancelled()) {
                    future.join().expect("can't get session").release();
                }
            });
            futures.removeAll(completed);

            // all sessions are idle
            Assert.assertEquals(maxSize, fakeRpc.size());
            Assert.assertEquals(0, client.getSessionPoolStats().getAcquiredCount());
            Assert.assertEquals(maxSize, client.getSessionPoolStats().getIdleCount());
            Assert.assertEquals(0, client.getSessionPoolStats().getPendingAcquireCount());
        }

        // After closing TableClient SessionPool released all sessions
        Assert.assertEquals(0, fakeRpc.size());
    }
}
