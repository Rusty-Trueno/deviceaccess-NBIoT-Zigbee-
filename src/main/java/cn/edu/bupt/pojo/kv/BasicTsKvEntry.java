package cn.edu.bupt.pojo.kv;

import java.util.Objects;
import java.util.Optional;

public class BasicTsKvEntry implements TsKvEntry {

    private final long ts;
    private final KvEntry kv;

    public BasicTsKvEntry(long ts, KvEntry kv) {
        this.ts = ts;
        this.kv = kv;
    }

    @Override
    public String getKey() {
        return kv.getKey();
    }

    @Override
    public DataType getDataType() {
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
    public Object getValue() {
        return kv.getValue();
    }

    @Override
    public long getTs() {
        return ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicTsKvEntry)) return false;
        BasicTsKvEntry that = (BasicTsKvEntry) o;
        return getTs() == that.getTs() &&
                Objects.equals(kv, that.kv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTs(), kv);
    }

    @Override
    public String toString() {
        return "BasicTsKvEntry{" +
                "ts=" + ts +
                ", kv=" + kv +
                '}';
    }

    @Override
    public String getValueAsString() {
        return kv.getValueAsString();
    }
}
