package cn.edu.bupt.service;

import cn.edu.bupt.dao.page.TimePageData;
import cn.edu.bupt.dao.page.TimePageLink;
import cn.edu.bupt.pojo.event.Event;

import java.util.Optional;

/**
 * Created by CZX on 2018/5/29.
 */
public interface EventService {

    Event save(Event event);

    Optional<Event> saveIfNotExists(Event event);

//    Optional<Event> findEvent(Integer tenantId, String entityId, String eventType, String eventUid);

    TimePageData<Event> findEvents(Integer tenantId, String entityId, TimePageLink pageLink);

    TimePageData<Event> findEvents(Integer tenantId, String entityId, String eventType, TimePageLink pageLink);

}
