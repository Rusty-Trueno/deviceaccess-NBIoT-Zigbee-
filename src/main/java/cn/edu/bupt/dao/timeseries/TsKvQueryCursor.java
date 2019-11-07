package cn.edu.bupt.dao.timeseries;

import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvQuery;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ashvayka on 21.02.17.
 */
public class TsKvQueryCursor {
    @Getter
    private final UUID entityId;
    @Getter
    private final String key;
    @Getter
    private final long startTs;
    @Getter
    private final long endTs;
    private final List<Long> partitions;
    @Getter
    private final List<TsKvEntry> data;

    private int partitionIndex;
    private int currentLimit;

    public TsKvQueryCursor(UUID entityId, TsKvQuery baseQuery, List<Long> partitions) {
        this.entityId = entityId;
        this.key = baseQuery.getKey();
        this.startTs = baseQuery.getStartTs();
        this.endTs = baseQuery.getEndTs();
        this.partitions = partitions;
        this.partitionIndex = partitions.size() - 1;
        this.data = new ArrayList<>();
        this.currentLimit = baseQuery.getLimit();
    }

    public boolean hasNextPartition() {
        return partitionIndex >= 0;
    }

    public boolean isFull() {
        return currentLimit <= 0;
    }

    public long getNextPartition() {
        long partition = partitions.get(partitionIndex);
        partitionIndex--;
        return partition;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public void addData(List<TsKvEntry> newData) {
        currentLimit -= newData.size();
        data.addAll(newData);
    }
}
