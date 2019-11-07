package cn.edu.bupt.service;

import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvQuery;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/24.
 */
public interface TimeseriesService {

    ListenableFuture<List<TsKvEntry>> findAll(UUID entityId, List<TsKvQuery> queries);

    ListenableFuture<List<TsKvEntry>> findLatest(UUID entityId, Collection<String> keys);

    ListenableFuture<List<TsKvEntry>> findAllLatest(UUID entityId);

    ListenableFuture<List<String>> findAllKeys(UUID entityId);

    ListenableFuture<List<Void>> save(UUID entityId, TsKvEntry tsKvEntry);

    ListenableFuture<List<Void>> save(UUID entityId, List<TsKvEntry> tsKvEntry, long ttl);

}
