package cn.edu.bupt.pojo.kv;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttributeKey implements Serializable {
    private final String scope;
    private final String attributeKey;
}
