package cn.edu.bupt.dao.event;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractSearchTimeDao;
import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.dao.page.TimePageLink;
import cn.edu.bupt.pojo.event.Event;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cn.edu.bupt.dao.ModelConstants.*;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

/**
 * Created by CZX on 2018/5/28.
 */
@Component
@Slf4j
public class CassandraBaseEventDao extends CassandraAbstractSearchTimeDao<Event> implements EventDao{

//    private final TenantId systemTenantId = new TenantId(NULL_UUID);

    @Override
    protected Class<Event> getColumnFamilyClass() {
        return Event.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return EVENT_COLUMN_FAMILY_NAME;
    }

    @Override
    public Event save(Event event) {
        log.debug("Save event [{}] ", event);
        if (event.getId() == null) {
            event.setId(UUIDs.timeBased());
        }
        return save(event, false).orElse(null);
    }

    @Override
    public Optional<Event> saveIfNotExists(Event event) {
        if (event.getId() == null) {
            event.setId(UUIDs.timeBased());
        }
        return save(event, true);
    }


    @Override
    public List<Event> findEvents(Integer tenantId, String entityId, TimePageLink pageLink) {
        log.trace("Try to find events by tenant [{}], entity [{}]and pageLink [{}]", tenantId, entityId, pageLink);
        List<Event> entities = findPageWithTimeSearch(EVENT_BY_ID_VIEW_NAME,
                Arrays.asList(eq(ModelConstants.EVENT_TENANT_ID_PROPERTY, tenantId),
                        eq(ModelConstants.EVENT_ENTITY_ID_PROPERTY, entityId)),
                pageLink);
        log.trace("Found events by tenant [{}], entity [{}] and pageLink [{}]", tenantId, entityId, pageLink);
        return entities;
    }

    @Override
    public List<Event> findEvents(Integer tenantId, String entityId, String eventType, TimePageLink pageLink) {
        log.trace("Try to find events by tenant [{}], entity [{}], type [{}] and pageLink [{}]", tenantId, entityId, eventType, pageLink);
        List<Event> entities = findPageWithTimeSearch(EVENT_BY_TYPE_AND_ID_VIEW_NAME,
                Arrays.asList(eq(ModelConstants.EVENT_TENANT_ID_PROPERTY, tenantId),
                        eq(ModelConstants.EVENT_ENTITY_ID_PROPERTY, entityId),
                        eq(ModelConstants.EVENT_TYPE_PROPERTY, eventType)),
                pageLink.isAscOrder() ? QueryBuilder.asc(ModelConstants.EVENT_TYPE_PROPERTY) :
                        QueryBuilder.desc(ModelConstants.EVENT_TYPE_PROPERTY),
                pageLink);
        log.trace("Found events by tenant [{}], entity [{}], type [{}] and pageLink [{}]", tenantId, entityId, eventType, pageLink);
        return entities;
    }

    private Optional<Event> save(Event entity, boolean ifNotExists) {
        if (entity.getId() == null) {
            entity.setId(UUIDs.timeBased());
        }
        Insert insert = QueryBuilder.insertInto(getColumnFamilyName())
                .value(ModelConstants.ID_PROPERTY, entity.getId())
                .value(ModelConstants.EVENT_TENANT_ID_PROPERTY, entity.getTenantId())
                .value(ModelConstants.EVENT_ENTITY_TYPE_PROPERTY, entity.getEntityType())
                .value(ModelConstants.EVENT_ENTITY_ID_PROPERTY, entity.getEntityId())
                .value(ModelConstants.EVENT_TYPE_PROPERTY, entity.getEventType())
                .value(ModelConstants.EVENT_BODY_PROPERTY, entity.getBody());
        if (ifNotExists) {
            insert = insert.ifNotExists();
        }
        ResultSet rs = executeWrite(insert);
        if (rs.wasApplied()) {
            return Optional.of(entity);
        } else {
            return Optional.empty();
        }
    }
}
