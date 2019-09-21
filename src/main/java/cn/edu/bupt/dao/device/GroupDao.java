package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Dao;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Group;

import java.util.List;
import java.util.Optional;

/**
 * Created by CZX on 2018/4/18.
 */
public interface GroupDao extends Dao<Group> {

    /**
     * Save or update group object
     *
     * @param group the group object
     * @return saved group object
     */
    Group save(Group group);

    /**
     * Find groups by tenant id and page link.
     *
     * @param tenantId the tenant id
     * @param pageLink the page link
     * @return the list of group objects
     */
    List<Group> findGroupsByTenantId(Integer tenantId, TextPageLink pageLink);

    List<Group> findGroupsByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) ;

    List<Group> findGroupsByCustomerId(Integer customerId, TextPageLink pageLink);

    Optional<Group> findGroupByTenantAndCustomerIdAndName(Integer tenantId, Integer customerId, String name);

}
