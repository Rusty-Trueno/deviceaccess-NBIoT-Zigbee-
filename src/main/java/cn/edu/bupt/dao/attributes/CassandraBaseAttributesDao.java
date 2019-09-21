package cn.edu.bupt.dao.attributes;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractAsyncDao;
import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.dao.timeseries.CassandraBaseTimeseriesDao;
import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import cn.edu.bupt.pojo.kv.BaseAttributeKvEntry;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.bupt.dao.ModelConstants.*;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * Created by CZX on 2018/4/24.
 */
@Component
@Slf4j
public class CassandraBaseAttributesDao extends CassandraAbstractAsyncDao implements AttributesDao {

    private PreparedStatement saveStmt;

    @PostConstruct
    public void init() {
        super.startExecutor();
    }

    @PreDestroy
    public void stop() {
        super.stopExecutor();
    }

    @Override
    public ListenableFuture<Optional<AttributeKvEntry>> find(UUID entityId, String attributeKey) {
        Select.Where select = select().from(ATTRIBUTES_KV_CF)
                .where(eq(ENTITY_ID_COLUMN, entityId))
                .and(eq(ATTRIBUTE_KEY_COLUMN, attributeKey));
        log.trace("Generated query [{}] for entityId {} and key {}", select, entityId, attributeKey);
        return Futures.transform(executeAsyncRead(select), (Function<? super ResultSet, ? extends Optional<AttributeKvEntry>>) input ->
                        Optional.ofNullable(convertResultToAttributesKvEntry(attributeKey, input.one()))
                , readResultsProcessingExecutor);
    }

    @Override
    public ListenableFuture<List<AttributeKvEntry>> find(UUID entityId, Collection<String> attributeKeys) {
        List<ListenableFuture<Optional<AttributeKvEntry>>> entries = new ArrayList<>();
        attributeKeys.forEach(attributeKey -> entries.add(find(entityId, attributeKey)));
        return Futures.transform(Futures.allAsList(entries), (Function<List<Optional<AttributeKvEntry>>, ? extends List<AttributeKvEntry>>) input -> {
            List<AttributeKvEntry> result = new ArrayList<>();
            input.stream().filter(opt -> opt.isPresent()).forEach(opt -> result.add(opt.get()));
            return result;
        }, readResultsProcessingExecutor);
    }


    @Override
    public ListenableFuture<List<AttributeKvEntry>> findAll(UUID entityId) {
        Select.Where select = select().from(ATTRIBUTES_KV_CF)
                .where(eq(ENTITY_ID_COLUMN, entityId));
        log.trace("Generated query [{}] for entityId {}", select, entityId);
        return Futures.transform(executeAsyncRead(select), (Function<? super ResultSet, ? extends List<AttributeKvEntry>>) input ->
                        convertResultToAttributesKvEntryList(input)
                , readResultsProcessingExecutor);
    }

    @Override
    public ListenableFuture<Void> save(UUID entityId, AttributeKvEntry attribute) {
        BoundStatement stmt = getSaveStmt().bind();
        stmt.setUUID(0, entityId);
        stmt.setString(1, attribute.getKey());
        stmt.setLong(2, attribute.getLastUpdateTs());
        stmt.setString(3, attribute.getStrValue().orElse(null));
        Optional<Boolean> booleanValue = attribute.getBooleanValue();
        if (booleanValue.isPresent()) {
            stmt.setBool(4, booleanValue.get());
        } else {
            stmt.setToNull(4);
        }
        Optional<Long> longValue = attribute.getLongValue();
        if (longValue.isPresent()) {
            stmt.setLong(5, longValue.get());
        } else {
            stmt.setToNull(5);
        }
        Optional<Double> doubleValue = attribute.getDoubleValue();
        if (doubleValue.isPresent()) {
            stmt.setDouble(6, doubleValue.get());
        } else {
            stmt.setToNull(6);
        }
        log.trace("Generated save stmt [{}] for entityId {} and attribute{}", stmt, entityId, attribute);
        return getFuture(executeAsyncWrite(stmt), rs -> null);
    }

    @Override
    public ListenableFuture<List<Void>> removeAll(UUID entityId, List<String> keys) {
        List<ListenableFuture<Void>> futures = keys
                .stream()
                .map(key -> delete(entityId, key))
                .collect(Collectors.toList());
        return Futures.allAsList(futures);
    }

    private ListenableFuture<Void> delete(UUID entityId, String key) {
        Statement delete = QueryBuilder.delete().all().from(ModelConstants.ATTRIBUTES_KV_CF)
                .where(eq(ENTITY_ID_COLUMN, entityId))
                .and(eq(ATTRIBUTE_KEY_COLUMN, key));
        log.debug("Remove request: {}", delete.toString());
        return getFuture(getSession().executeAsync(delete), rs -> null);
    }

    private PreparedStatement getSaveStmt() {
        if (saveStmt == null) {
            saveStmt = getSession().prepare("INSERT INTO " + ModelConstants.ATTRIBUTES_KV_CF +
                    "(" + ENTITY_ID_COLUMN +
                    "," + ATTRIBUTE_KEY_COLUMN +
                    "," + LAST_UPDATE_TS_COLUMN +
                    "," + ModelConstants.STRING_VALUE_COLUMN +
                    "," + ModelConstants.BOOLEAN_VALUE_COLUMN +
                    "," + ModelConstants.LONG_VALUE_COLUMN +
                    "," + ModelConstants.DOUBLE_VALUE_COLUMN +
                    ")" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?)");
        }
        return saveStmt;
    }

    private AttributeKvEntry convertResultToAttributesKvEntry(String key, Row row) {
        AttributeKvEntry attributeEntry = null;
        if (row != null) {
            long lastUpdateTs = row.get(LAST_UPDATE_TS_COLUMN, Long.class);
            attributeEntry = new BaseAttributeKvEntry(CassandraBaseTimeseriesDao.toKvEntry(row, key), lastUpdateTs);
        }
        return attributeEntry;
    }

    private List<AttributeKvEntry> convertResultToAttributesKvEntryList(ResultSet resultSet) {
        List<Row> rows = resultSet.all();
        List<AttributeKvEntry> entries = new ArrayList<>(rows.size());
        if (!rows.isEmpty()) {
            rows.forEach(row -> {
                String key = row.getString(ModelConstants.ATTRIBUTE_KEY_COLUMN);
                AttributeKvEntry kvEntry = convertResultToAttributesKvEntry(key, row);
                if (kvEntry != null) {
                    entries.add(kvEntry);
                }
            });
        }
        return entries;
    }

}
