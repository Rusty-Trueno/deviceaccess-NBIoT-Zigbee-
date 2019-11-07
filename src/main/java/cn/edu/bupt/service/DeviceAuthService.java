package cn.edu.bupt.service;

import cn.edu.bupt.common.security.DeviceAuthResult;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentals;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface DeviceAuthService {
    DeviceAuthResult process(DeviceCredentals credentals);

    Optional<Device> findDeviceById(String deviceId);
}
