package cn.edu.bupt.pojo.kv;

import lombok.Data;

@Data
public class BaseTsKvQuery implements TsKvQuery {

    private final String key;
    private final long startTs;
    private final long endTs;
    private final long interval;
    private final int limit;
    private final Aggregation aggregation;

    public BaseTsKvQuery(String key, long startTs, long endTs, long interval, int limit, Aggregation aggregation) {
        this.key = key;
        this.startTs = startTs;
        this.endTs = endTs;
        this.interval = interval;
        this.limit = limit;
        this.aggregation = aggregation;
    }

    public BaseTsKvQuery(String key, long startTs, long endTs, int interval, int limit) {
        this(key, startTs, endTs, interval, limit, Aggregation.COUNT);
    }



}
