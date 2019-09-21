package cn.edu.bupt.common.entry;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public abstract class BasicTelemetryKvEntry implements KvEntry {

    private final String key;

    protected BasicTelemetryKvEntry(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Optional<Boolean> getBooleanValue() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<String> getStrValue() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<Long> getLongValue() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<Double> getDoubleValue() {
        return Optional.ofNullable(null);
    }

}
