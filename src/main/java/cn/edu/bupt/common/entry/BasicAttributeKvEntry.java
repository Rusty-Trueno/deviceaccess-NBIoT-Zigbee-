package cn.edu.bupt.common.entry;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public class BasicAttributeKvEntry implements KvEntry {

    private final long lastUpdateTs;
    private final KvEntry kv;

    public BasicAttributeKvEntry(long ts,KvEntry kv){
        this.lastUpdateTs = ts;
        this.kv = kv;
    }

    public long getLastUpdateTs(){return lastUpdateTs;}

    @Override
    public String getKey() {
        return kv.getKey();
    }

    @Override
    public String getDataType() {
        return kv.getDataType();
    }

    @Override
    public Optional<String> getStrValue() {
        return kv.getStrValue();
    }

    @Override
    public Optional<Long> getLongValue() {
        return kv.getLongValue();
    }

    @Override
    public Optional<Boolean> getBooleanValue() {
        return kv.getBooleanValue();
    }

    @Override
    public Optional<Double> getDoubleValue() {
        return kv.getDoubleValue();
    }

    @Override
    public String getValueAsString() {
        return kv.getValueAsString();
    }

    @Override
    public Object getValue() {
        return kv.getValue();
    }
}
