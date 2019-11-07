package cn.edu.bupt.common.entry;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface KvEntry{

    String getKey();

    String getDataType();

    Optional<String> getStrValue();

    Optional<Long> getLongValue();

    Optional<Boolean> getBooleanValue();

    Optional<Double> getDoubleValue();

    String getValueAsString();

    Object getValue();
}
