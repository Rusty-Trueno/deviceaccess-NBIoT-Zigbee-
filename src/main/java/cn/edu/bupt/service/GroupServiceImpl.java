package cn.edu.bupt.service;

import cn.edu.bupt.dao.device.GroupDao;
import cn.edu.bupt.dao.exception.DataValidationException;
import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.dao.util.DataValidator;
import cn.edu.bupt.pojo.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static cn.edu.bupt.dao.util.Validator.validateId;
import static cn.edu.bupt.dao.util.Validator.validatePageLink;

/**
 * Created by CZX on 2018/4/19.
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;

    @Autowired
    private DeviceService deviceService;

    @Override
    public Group saveGroup(Group group) {
        groupValidator.validate(group);
        return groupDao.save(group);
    }

    //查找tenant下的所有设备组信息（包括从属与tenant的customer下的设备组信息）
    @Override
    public TextPageData<Group> findGroupsByTenantId(Integer tenantId, TextPageLink pageLink) {
        validateId(tenantId, "Incorrect tenantId " + tenantId);
        validatePageLink(pageLink, "Incorrect page link " + pageLink);
        List<Group> groups = groupDao.findGroupsByTenantId(tenantId, pageLink);
        return new TextPageData<>(groups, pageLink);
    }

    //查找某个customer下的所有设备组信息
    @Override
    public TextPageData<Group> findGroupsByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) {
        validateId(tenantId, "Incorrect tenantId " + tenantId);
        validateId(customerId, "Incorrect customerId " + customerId);
        validatePageLink(pageLink, "Incorrect page link " + pageLink);
        List<Group> groups = groupDao.findGroupsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
        return new TextPageData<>(groups, pageLink);
    }

    @Override
    public TextPageData<Group> findGroupsByCustomerId(Integer customerId, TextPageLink pageLink) {
        validateId(customerId, "Incorrect customerId " + customerId);
        validatePageLink(pageLink, "Incorrect page link " + pageLink);
        List<Group> groups = groupDao.findGroupsByCustomerId(customerId, pageLink);
        return new TextPageData<>(groups, pageLink);
    }

    //删除设备组，并且unassign设备组下的所有设备。
    @Override
    public void deleteGroup(UUID groupId) {
        validateId(groupId, "Incorrect groupId " + groupId);
        deviceService.unassignDevicesByGroupId(groupId);
        groupDao.removeById(groupId);
    }

    private DataValidator<Group> groupValidator =
            new DataValidator<Group>() {

                @Override
                protected void validateCreate(Group group) {
                    groupDao.findGroupByTenantAndCustomerIdAndName(group.getTenantId(), group.getCustomerId(),  group.getName()).ifPresent(
                            c -> {
                                throw new DataValidationException("Group with such name already exists!");
                            }
                    );
                }

                @Override
                protected void validateUpdate(Group group) {
                    groupDao.findGroupByTenantAndCustomerIdAndName(group.getTenantId(), group.getCustomerId(),  group.getName()).ifPresent(
                            c -> {
                                if (!c.getId().equals(group.getId())) {
                                    throw new DataValidationException("Group with such name already exists!");
                                }
                            }
                    );
                }

                @Override
                protected void validateDataImpl(Group group) {
                    if (StringUtils.isEmpty(group.getName())) {
                        throw new DataValidationException("Group name should be specified!");
                    }
                    if (group.getTenantId() == null) {
                        throw new DataValidationException("Group should be assigned to tenant!");
                    }
                    if (group.getCustomerId() == null) {
                        group.setCustomerId(1);
                    }
                }
            };
}
