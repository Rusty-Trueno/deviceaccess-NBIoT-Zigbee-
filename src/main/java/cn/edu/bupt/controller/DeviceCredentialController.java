package cn.edu.bupt.controller;

import cn.edu.bupt.pojo.DeviceCredentials;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deviceaccess")
public class DeviceCredentialController extends BaseController {

    //创建
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/credential",method = RequestMethod.POST)
    public String create(@RequestBody String credentials) throws Exception {
        //提交表单转变为json

        try {
            DeviceCredentials deviceCredentials1 = JSON.parseObject(credentials, DeviceCredentials.class);
            DeviceCredentials deviceCredentials = deviceCredentialsService.createDeviceCredentials(deviceCredentials1);
            return deviceCredentials.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return onFail(e.toString());
        }
    }

    //删除
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/credential/{credential}",method = RequestMethod.DELETE)
    public void delete(@PathVariable("credential") DeviceCredentials credentials) throws Exception {
        try {
           deviceCredentialsService.deleteDeviceCredentials(credentials);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }


    //修改
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/credential/{credential}",method = RequestMethod.PUT)
    public DeviceCredentials update(@PathVariable("credential") DeviceCredentials credentials) throws Exception {
        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.updateDeviceCredentials(credentials);
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过ID查找
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/credentialbyid/{deviceId}",method = RequestMethod.GET)
    public DeviceCredentials getById(@PathVariable("deviceId") String deviceId) throws Exception {
        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(toUUID(deviceId));
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过token查找
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/crednetialbytoken/{token}",method = RequestMethod.GET)
    public DeviceCredentials getByToken(@PathVariable("token") String token) throws Exception {
        try {
            DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByToken(token);
            return deviceCredentials;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
