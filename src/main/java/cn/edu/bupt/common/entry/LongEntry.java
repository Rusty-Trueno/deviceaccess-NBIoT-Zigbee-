package cn.edu.bupt.common.entry;

import cn.edu.bupt.common.KvEntryType;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public class LongEntry extends BasicTelemetryKvEntry {
    private final Long value;

    public LongEntry(String key,long value){
        super(key);
        this.value = value;
    }
    @Override
    public Optional<Long> getLongValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public String getDataType() {
        return KvEntryType.LONG;
    }

    @Override
    public String getValueAsString(){
        return Long.toString(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
