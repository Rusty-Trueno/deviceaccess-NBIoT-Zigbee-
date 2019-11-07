package cn.edu.bupt.service;

import cn.edu.bupt.dao.deviceCredentials.DeviceCredentialsDao;
import cn.edu.bupt.dao.exception.DataValidationException;
import cn.edu.bupt.dao.util.DataValidator;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static cn.edu.bupt.dao.util.Validator.validateId;
import static cn.edu.bupt.dao.util.Validator.validateString;

/**
 * Created by CZX on 2018/4/19.
 */
@Service
public class DeviceCredentialsServiceImpl implements DeviceCredentialsService {

    @Autowired
    private DeviceCredentialsDao deviceCredentialsDao;

    @Autowired
    private DeviceService deviceService;

    @Override
    public DeviceCredentials findDeviceCredentialsByDeviceId(UUID deviceId) {
        validateId(deviceId, "Incorrect deviceId " + deviceId);
        return deviceCredentialsDao.findByDeviceId(deviceId);
    }

    @Override
//    @Cacheable(cacheNames = DEVICE_CREDENTIALS_CACHE, unless="#result == null")
    public DeviceCredentials findDeviceCredentialsByToken(String token) {
        validateString(token, "Incorrect credentialsId " + token);
        return deviceCredentialsDao.findByToken(token);
    }

    @Override
//    @CacheEvict(cacheNames = DEVICE_CREDENTIALS_CACHE, keyGenerator="previousDeviceCredentialsId", beforeInvocation = true)
    public DeviceCredentials updateDeviceCredentials(DeviceCredentials deviceCredentials) {
        return saveOrUpdare(deviceCredentials);
    }

    @Override
    public DeviceCredentials createDeviceCredentials(DeviceCredentials deviceCredentials) {
        return saveOrUpdare(deviceCredentials);
    }

    private DeviceCredentials saveOrUpdare(DeviceCredentials deviceCredentials) {
        credentialsValidator.validate(deviceCredentials);
        return deviceCredentialsDao.save(deviceCredentials);
    }

    @Override
//    @CacheEvict(cacheNames = DEVICE_CREDENTIALS_CACHE, key="#deviceCredentials.credentialsId")
    public void deleteDeviceCredentials(DeviceCredentials deviceCredentials) {
        deviceCredentialsDao.removeById(deviceCredentials.getId());
    }

    private DataValidator<DeviceCredentials> credentialsValidator =
            new DataValidator<DeviceCredentials>() {

                @Override
                protected void validateCreate(DeviceCredentials deviceCredentials) {
                    DeviceCredentials existingCredentials = deviceCredentialsDao.findByToken(deviceCredentials.getDeviceToken());
                    if (existingCredentials != null) {
                        throw new DataValidationException("Create of existent device credentials!");
                    }
                }

                @Override
                protected void validateUpdate(DeviceCredentials deviceCredentials) {
                    DeviceCredentials existingCredentials = deviceCredentialsDao.findById(deviceCredentials.getId());
                    if (existingCredentials == null) {
                        throw new DataValidationException("Unable to update non-existent device credentials!");
                    }
                    DeviceCredentials sameCredentialsId = deviceCredentialsDao.findByToken(deviceCredentials.getDeviceToken());
                    if (sameCredentialsId != null && !sameCredentialsId.getId().equals(deviceCredentials.getId())) {
                        throw new DataValidationException("Specified credentials are already registered!");
                    }
                }

                @Override
                protected void validateDataImpl(DeviceCredentials deviceCredentials) {
                    if (deviceCredentials.getDeviceId() == null) {
                        throw new DataValidationException("Device credentials should be assigned to device!");
                    }
                    if (StringUtils.isEmpty(deviceCredentials.getDeviceToken())) {
                        throw new DataValidationException("Device credentials id should be specified!");
                    }
                    Device device = deviceService.findDeviceById(deviceCredentials.getDeviceId());
                    if (device == null) {
                        throw new DataValidationException("Can't assign device credentials to non-existent device!");
                    }
                }
            };

}
