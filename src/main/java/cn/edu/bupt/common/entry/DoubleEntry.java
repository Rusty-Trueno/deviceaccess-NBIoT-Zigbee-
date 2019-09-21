package cn.edu.bupt.common.entry;

import cn.edu.bupt.common.KvEntryType;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public class DoubleEntry extends BasicTelemetryKvEntry {
    private final Double value ;

    public DoubleEntry(String key,double value){
        super(key);
        this.value = value;
    }

    @Override
    public Optional<Double> getDoubleValue() {
        return Optional.of(value);
    }

    @Override
    public String getDataType() {
        return KvEntryType.DOUBLE;
    }

    @Override
    public String getValueAsString() {
        return Double.toString(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
