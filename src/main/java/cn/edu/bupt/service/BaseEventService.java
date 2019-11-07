package cn.edu.bupt.service;

import cn.edu.bupt.dao.event.EventDao;
import cn.edu.bupt.dao.exception.DataValidationException;
import cn.edu.bupt.dao.page.TimePageData;
import cn.edu.bupt.dao.page.TimePageLink;
import cn.edu.bupt.dao.util.DataValidator;
import cn.edu.bupt.pojo.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by CZX on 2018/5/29.
 */
@Service
@Slf4j
public class BaseEventService implements EventService{

    @Autowired
    public EventDao eventDao;

    @Override
    public Event save(Event event) {
        eventValidator.validate(event);
        return eventDao.save(event);
    }

    @Override
    public Optional<Event> saveIfNotExists(Event event) {
        eventValidator.validate(event);
        return eventDao.saveIfNotExists(event);
    }

//    @Override
//    public Optional<Event> findEvent(TenantId tenantId, EntityId entityId, String eventType, String eventUid) {
//        if (tenantId == null) {
//            throw new DataValidationException("Tenant id should be specified!.");
//        }
//        if (entityId == null) {
//            throw new DataValidationException("Entity id should be specified!.");
//        }
//        if (StringUtils.isEmpty(eventType)) {
//            throw new DataValidationException("Event type should be specified!.");
//        }
//        if (StringUtils.isEmpty(eventUid)) {
//            throw new DataValidationException("Event uid should be specified!.");
//        }
//        Event event = eventDao.findEvent(tenantId.getId(), entityId, eventType, eventUid);
//        return event != null ? Optional.of(event) : Optional.empty();
//    }

    @Override
    public TimePageData<Event> findEvents(Integer tenantId, String entityId, TimePageLink pageLink) {
        List<Event> events = eventDao.findEvents(tenantId, entityId, pageLink);
        return new TimePageData<>(events, pageLink);
    }

    @Override
    public TimePageData<Event> findEvents(Integer tenantId, String entityId, String eventType, TimePageLink pageLink) {
        List<Event> events = eventDao.findEvents(tenantId, entityId, eventType, pageLink);
        return new TimePageData<>(events, pageLink);
    }

    private DataValidator<Event> eventValidator =
            new DataValidator<Event>() {
                @Override
                protected void validateDataImpl(Event event) {
                    if (event.getEntityId() == null) {
                        throw new DataValidationException("Entity id should be specified!.");
                    }
                    if (StringUtils.isEmpty(event.getEventType())) {
                        throw new DataValidationException("Event type should be specified!.");
                    }
                    if (event.getBody() == null) {
                        throw new DataValidationException("Event body should be specified!.");
                    }
                }
            };
}
