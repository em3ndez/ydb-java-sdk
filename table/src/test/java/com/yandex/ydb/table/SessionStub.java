package com.yandex.ydb.table;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.Status;
import com.yandex.ydb.table.description.TableDescription;
import com.yandex.ydb.table.query.DataQuery;
import com.yandex.ydb.table.query.DataQueryResult;
import com.yandex.ydb.table.query.ExplainDataQueryResult;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ReadTableMeta;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.settings.AlterTableSettings;
import com.yandex.ydb.table.settings.BeginTxSettings;
import com.yandex.ydb.table.settings.BulkUpsertSettings;
import com.yandex.ydb.table.settings.CloseSessionSettings;
import com.yandex.ydb.table.settings.CommitTxSettings;
import com.yandex.ydb.table.settings.CopyTableSettings;
import com.yandex.ydb.table.settings.CreateTableSettings;
import com.yandex.ydb.table.settings.DescribeTableSettings;
import com.yandex.ydb.table.settings.DropTableSettings;
import com.yandex.ydb.table.settings.ExecuteDataQuerySettings;
import com.yandex.ydb.table.settings.ExecuteScanQuerySettings;
import com.yandex.ydb.table.settings.ExecuteSchemeQuerySettings;
import com.yandex.ydb.table.settings.ExplainDataQuerySettings;
import com.yandex.ydb.table.settings.KeepAliveSessionSettings;
import com.yandex.ydb.table.settings.PrepareDataQuerySettings;
import com.yandex.ydb.table.settings.ReadTableSettings;
import com.yandex.ydb.table.settings.RollbackTxSettings;
import com.yandex.ydb.table.transaction.Transaction;
import com.yandex.ydb.table.transaction.TransactionMode;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.utils.Async;
import com.yandex.ydb.table.values.ListValue;


/**
 * @author Sergey Polovko
 */
public class SessionStub implements Session {

    private final String id;

    public SessionStub(String id) {
        this.id = id;
    }

    public SessionStub() {
        this(UUID.randomUUID().toString());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CompletableFuture<Status> createTable(
        String path, TableDescription tableDescriptions, CreateTableSettings settings)
    {
        return notImplemented("createTable()");
    }

    @Override
    public CompletableFuture<Status> dropTable(String path, DropTableSettings settings) {
        return notImplemented("dropTable()");
    }

    @Override
    public CompletableFuture<Status> alterTable(String path, AlterTableSettings settings) {
        return notImplemented("alterTable()");
    }

    @Override
    public CompletableFuture<Status> copyTable(String src, String dst, CopyTableSettings settings) {
        return notImplemented("copyTable()");
    }

    @Override
    public CompletableFuture<Result<TableDescription>> describeTable(String path, DescribeTableSettings settings) {
        return notImplemented("describeTable()");
    }

    @Override
    public CompletableFuture<Result<DataQueryResult>> executeDataQuery(
        String query, TxControl txControl, Params params, ExecuteDataQuerySettings settings)
    {
        return notImplemented("executeDataQuery()");
    }

    @Override
    public CompletableFuture<Result<DataQuery>> prepareDataQuery(String query, PrepareDataQuerySettings settings) {
        return notImplemented("prepareDataQuery()");
    }

    @Override
    public CompletableFuture<Status> executeSchemeQuery(String query, ExecuteSchemeQuerySettings settings) {
        return notImplemented("executeSchemeQuery()");
    }

    @Override
    public CompletableFuture<Result<ExplainDataQueryResult>> explainDataQuery(
        String query, ExplainDataQuerySettings settings)
    {
        return notImplemented("explainDataQuery()");
    }

    @Override
    public CompletableFuture<Result<Transaction>> beginTransaction(
        TransactionMode transactionMode, BeginTxSettings settings)
    {
        return notImplemented("beginTransaction()");
    }

    @Override
    public CompletableFuture<Status> readTable(String tablePath, ReadTableSettings settings, Consumer<ResultSetReader> fn) {
        return notImplemented("readTable()");
    }

    @Override
    public CompletableFuture<Status> readTable(String tablePath, ReadTableSettings settings, BiConsumer<ResultSetReader, ReadTableMeta> fn) {
        return notImplemented("readTable()");
    }

    @Override
    public CompletableFuture<Status> executeScanQuery(String query, Params params, ExecuteScanQuerySettings settings, Consumer<ResultSetReader> fn) {
        return notImplemented("executeScanQuery");
    }

    @Override
    public CompletableFuture<Status> commitTransaction(String txId, CommitTxSettings settings) {
        return notImplemented("commitTransaction()");
    }

    @Override
    public CompletableFuture<Status> rollbackTransaction(String txId, RollbackTxSettings settings) {
        return notImplemented("rollbackTransaction()");
    }

    @Override
    public CompletableFuture<Result<SessionStatus>> keepAlive(KeepAliveSessionSettings settings) {
        return notImplemented("keepAlive()");
    }

    @Override
    public CompletableFuture<Status> executeBulkUpsert(String tablePath, ListValue rows, BulkUpsertSettings settings) {
        return notImplemented("bulkUpsert()");
    }

    @Override
    public void invalidateQueryCache() {
        throw new UnsupportedOperationException("invalidateQueryCache() not implemented");
    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public CompletableFuture<Status> close(CloseSessionSettings settings) {
        return notImplemented("close()");
    }

    private static <U> CompletableFuture<U> notImplemented(String method) {
        return Async.failedFuture(new UnsupportedOperationException(method + " not implemented"));
    }
}
