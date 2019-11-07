package cn.edu.bupt.pojo.kv;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/25.
 */
public class BasicAdaptorTsKvEntry implements TsKvEntry{

    private final long ts;
    private final cn.edu.bupt.common.entry.KvEntry kv;

    public BasicAdaptorTsKvEntry(long ts, cn.edu.bupt.common.entry.KvEntry kv){
        this.ts = ts;
        this.kv = kv;
    }

    @Override
    public String getKey() {
        return kv.getKey();
    }

    @Override
    public DataType getDataType() {
        switch(kv.getDataType()){
            case "string":
                return DataType.STRING;
            case "long":
                return DataType.LONG;
            case "boolean":
                return DataType.BOOLEAN;
            case "double":
                return DataType.DOUBLE;
        }
        return null;
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

    @Override
    public long getTs() {
        return ts;
    }
}
