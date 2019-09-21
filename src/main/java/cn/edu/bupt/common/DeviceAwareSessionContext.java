package cn.edu.bupt.common;

import cn.edu.bupt.common.security.DeviceAuthResult;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceCredentals;
import cn.edu.bupt.service.DeviceAuthService;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/13.
 */
public abstract class DeviceAwareSessionContext implements SessionContext {
    protected DeviceAuthService authService;
    @Getter @Setter
    protected volatile Device device;

    public DeviceAwareSessionContext(DeviceAuthService authService){
        this.authService = authService;
    }

    public boolean login(DeviceCredentals credentials){
        DeviceAuthResult res =  authService.process(credentials );
        if (res.isSuccess()) {
            Optional<Device> deviceOpt = authService.findDeviceById(res.getDeviceId());
            if (deviceOpt.isPresent()) {
                device = deviceOpt.get();
            }
            return true;
        } else {
           // log.debug("Can't find device using credentials [{}] due to {}", credentials, result.getErrorMsg());
            return false;
        }
    }
}
