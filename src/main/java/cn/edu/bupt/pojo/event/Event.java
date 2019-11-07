package cn.edu.bupt.pojo.event;

import cn.edu.bupt.dao.BaseData;
import cn.edu.bupt.dao.BaseEntity;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static cn.edu.bupt.dao.ModelConstants.*;

/**
 * Created by CZX on 2018/5/28.
 */
@Data
@NoArgsConstructor
@Table(name = EVENT_COLUMN_FAMILY_NAME)
public class Event extends BaseData implements BaseEntity {

    @ClusteringColumn(value = 1)
    @Column(name = ID_PROPERTY)
    private UUID id;

    @PartitionKey()
    @Column(name = EVENT_TENANT_ID_PROPERTY)
    private Integer tenantId;

    @Column(name = EVENT_ENTITY_TYPE_PROPERTY, codec = EntityTypeCodec.class)
    private EntityType entityType;

    @PartitionKey(value = 1)
    @Column(name = EVENT_ENTITY_ID_PROPERTY)
    private String entityId;

    @ClusteringColumn()
    @Column(name = EVENT_TYPE_PROPERTY)
    private String eventType;

    @Column(name = EVENT_BODY_PROPERTY)
    private String body;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append("\""+id+"\"");
        sb.append(",\"tenantId\":")
                .append(tenantId);
        sb.append(",\"entityType\":")
                .append(entityType);
        sb.append(",\"entityId\":")
                .append(entityId);
        sb.append(",\"eventType\":\"")
                .append(eventType).append('\"');
        sb.append(",\"body\":\"")
                .append(body).append('\"');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public long getCreatedTime(){
        Long createdTime = id.timestamp();
        createdTime = createdTime/10000000L - 12219292800L;
        return createdTime;
    }
}
