package cn.edu.bupt.dao.event;

import cn.edu.bupt.dao.Dao;
import cn.edu.bupt.dao.page.TimePageLink;
import cn.edu.bupt.pojo.event.Event;

import java.util.List;
import java.util.Optional;

/**
 * Created by CZX on 2018/5/28.
 */
public interface EventDao extends Dao<Event> {

    /**
     * Save or update event object
     *
     * @param event the event object
     * @return saved event object
     */
    Event save(Event event);

    /**
     * Save event object if it is not yet saved
     *
     * @param event the event object
     * @return saved event object
     */
    Optional<Event> saveIfNotExists(Event event);

    /**
     * Find event by tenantId, entityId and eventUid.
     *
     * @param tenantId the tenantId
     * @param entityId the entityId
     * @param eventType the eventType
     * @param eventUid the eventUid
     * @return the event
     */
//    Event findEvent(Integer tenantId, String entityId, String eventType, String eventUid);

    /**
     * Find events by tenantId, entityId and pageLink.
     *
     * @param tenantId the tenantId
     * @param entityId the entityId
     * @param pageLink the pageLink
     * @return the event list
     */
    List<Event> findEvents(Integer tenantId, String entityId, TimePageLink pageLink);

    /**
     * Find events by tenantId, entityId, eventType and pageLink.
     *
     * @param tenantId the tenantId
     * @param entityId the entityId
     * @param eventType the eventType
     * @param pageLink the pageLink
     * @return the event list
     */
    List<Event> findEvents(Integer tenantId, String entityId, String eventType, TimePageLink pageLink);

}
