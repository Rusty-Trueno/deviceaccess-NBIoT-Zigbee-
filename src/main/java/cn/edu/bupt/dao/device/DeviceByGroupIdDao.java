package cn.edu.bupt.dao.device;

import cn.edu.bupt.pojo.DeviceByGroupId;

import java.util.List;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/18.
 */
public interface DeviceByGroupIdDao {

    boolean save(DeviceByGroupId deviceByGroupId);

    boolean delete(DeviceByGroupId deviceByGroupId);

    boolean deleteAllByGroupId(UUID groupId);

    List<UUID> findDevicesByGroupId(UUID groupId);

    List<UUID> findGroupsByDeviceId(UUID deviceId);
}
