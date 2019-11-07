package cn.edu.bupt.pojo.kv;

import java.util.Optional;

public class BasicAdaptorAttributeKvEntry implements AttributeKvEntry {

    private final cn.edu.bupt.common.entry.KvEntry kv;
    private final long ts;

    public BasicAdaptorAttributeKvEntry(cn.edu.bupt.common.entry.KvEntry kv,long ts) {
        this.kv = kv;
        this.ts = ts;
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
    public long getLastUpdateTs() {
        return ts;
    }
}
