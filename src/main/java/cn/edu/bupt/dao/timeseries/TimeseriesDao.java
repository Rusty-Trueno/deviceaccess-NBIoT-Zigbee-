package cn.edu.bupt.dao.timeseries;

import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvQuery;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/20.
 */
public interface TimeseriesDao {

    ListenableFuture<List<TsKvEntry>> findAllAsync(UUID entityId, List<TsKvQuery> queries);

    ListenableFuture<TsKvEntry> findLatest(UUID entityId, String key);

    ListenableFuture<List<TsKvEntry>> findAllLatest(UUID entityId);

    ListenableFuture<List<String>> findAllKeys(UUID entityId);

    ListenableFuture<Void> save(UUID entityId, TsKvEntry tsKvEntry, long ttl);

    ListenableFuture<Void> savePartition(UUID entityId, long tsKvEntryTs, String key, long ttl);

    ListenableFuture<Void> saveLatest(UUID entityId, TsKvEntry tsKvEntry);

}
