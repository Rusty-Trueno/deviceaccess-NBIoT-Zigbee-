package cn.edu.bupt.service;

import cn.edu.bupt.dao.exception.IncorrectParameterException;
import cn.edu.bupt.dao.timeseries.TimeseriesDao;
import cn.edu.bupt.dao.util.Validator;
import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvQuery;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by CZX on 2018/4/24.
 */
@Service
@Slf4j
public class BaseTimeseriesService implements TimeseriesService{

    public static final int INSERTS_PER_ENTRY = 3;

    @Autowired
    private TimeseriesDao timeseriesDao;

    @Override
    public ListenableFuture<List<TsKvEntry>> findAll(UUID entityId, List<TsKvQuery> queries) {
        validate(entityId);
        queries.forEach(query -> validate(query));
        return timeseriesDao.findAllAsync(entityId, queries);
    }

    @Override
    public ListenableFuture<List<TsKvEntry>> findLatest(UUID entityId, Collection<String> keys) {
        validate(entityId);
        List<ListenableFuture<TsKvEntry>> futures = Lists.newArrayListWithExpectedSize(keys.size());
        keys.forEach(key -> Validator.validateString(key, "Incorrect key " + key));
        keys.forEach(key -> futures.add(timeseriesDao.findLatest(entityId, key)));
        return Futures.allAsList(futures);
    }

    @Override
    public ListenableFuture<List<TsKvEntry>> findAllLatest(UUID entityId) {
        validate(entityId);
        return timeseriesDao.findAllLatest(entityId);
    }

    @Override
    public ListenableFuture<List<String>> findAllKeys(UUID entityId) {
        validate(entityId);
        return timeseriesDao.findAllKeys(entityId);
    }

    @Override
    public ListenableFuture<List<Void>> save(UUID entityId, TsKvEntry tsKvEntry) {
        validate(entityId);
        if (tsKvEntry == null) {
            throw new IncorrectParameterException("Key value entry can't be null");
        }
        List<ListenableFuture<Void>> futures = Lists.newArrayListWithExpectedSize(INSERTS_PER_ENTRY);
        saveAndRegisterFutures(futures, entityId, tsKvEntry, 0L);
        return Futures.allAsList(futures);
    }

    @Override
    public ListenableFuture<List<Void>> save(UUID entityId, List<TsKvEntry> tsKvEntries, long ttl) {
        List<ListenableFuture<Void>> futures = Lists.newArrayListWithExpectedSize(tsKvEntries.size() * INSERTS_PER_ENTRY);
        for (TsKvEntry tsKvEntry : tsKvEntries) {
            if (tsKvEntry == null) {
                throw new IncorrectParameterException("Key value entry can't be null");
            }
            saveAndRegisterFutures(futures, entityId, tsKvEntry, ttl);
        }
        return Futures.allAsList(futures);
    }

    private void saveAndRegisterFutures(List<ListenableFuture<Void>> futures, UUID entityId, TsKvEntry tsKvEntry, long ttl) {
        futures.add(timeseriesDao.savePartition(entityId, tsKvEntry.getTs(), tsKvEntry.getKey(), ttl));
        futures.add(timeseriesDao.saveLatest(entityId, tsKvEntry));
        futures.add(timeseriesDao.save(entityId, tsKvEntry, ttl));
    }

    private static void validate(UUID entityId) {
        Validator.validateEntityId(entityId, "Incorrect entityId " + entityId);
    }

    private static void validate(TsKvQuery query) {
        if (query == null) {
            throw new IncorrectParameterException("TsKvQuery can't be null");
        } else if (isBlank(query.getKey())) {
            throw new IncorrectParameterException("Incorrect TsKvQuery. Key can't be empty");
        } else if (query.getAggregation() == null) {
            throw new IncorrectParameterException("Incorrect TsKvQuery. Aggregation can't be empty");
        }
    }
}
