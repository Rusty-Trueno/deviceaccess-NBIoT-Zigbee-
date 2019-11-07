package cn.edu.bupt.service;

import cn.edu.bupt.common.security.DeviceAuthResult;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentals;
import cn.edu.bupt.pojo.DeviceCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/14.
 */
@Service
public class DeviceAuthServiceImpl implements DeviceAuthService {

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceCredentialsService deviceCredentialsService;

    @Override
    public DeviceAuthResult process(DeviceCredentals credentals) {
        //需要根据具体逻辑修改，此处为了测试通过仅仅做了简单的返回
//        return DeviceAuthResult.of(true, UUID.randomUUID().toString(),"errorMsg");
        DeviceCredentials crede= deviceCredentialsService.findDeviceCredentialsByToken(credentals.getCredentialsId());

        if(crede!=null && !crede.getSuspended()){
           return  DeviceAuthResult.of(true,crede.getDeviceId().toString(),"success");
        }else if(crede!=null && crede.getSuspended()) {
            return DeviceAuthResult.of(false, null, "this device is suspended!");
        }else {
            return  DeviceAuthResult.of(false,null,"wrong token");
        }
    }

    @Override
    public Optional<Device> findDeviceById(String deviceId) {
        //
        Device device = deviceService.findDeviceById(UUID.fromString(deviceId));
        return Optional.of(device);
    }
}
