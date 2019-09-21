package cn.edu.bupt.service;

import cn.edu.bupt.pojo.DeviceCredentials;

import java.util.UUID;

/**
 * Created by CZX on 2018/4/19.
 */
public interface DeviceCredentialsService {

    DeviceCredentials findDeviceCredentialsByDeviceId(UUID deviceId);

    DeviceCredentials updateDeviceCredentials(DeviceCredentials deviceCredentials);

    DeviceCredentials createDeviceCredentials(DeviceCredentials deviceCredentials);

    void deleteDeviceCredentials(DeviceCredentials deviceCredentials);

    DeviceCredentials findDeviceCredentialsByToken(String token);
}
