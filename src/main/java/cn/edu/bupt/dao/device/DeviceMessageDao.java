package cn.edu.bupt.dao.device;

import cn.edu.bupt.pojo.DeviceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rongshuai on 2019/11/5 20:31
 */
@Service
public class DeviceMessageDao {
    @Autowired
    private DeviceMessageMapper deviceMessageMapper;

    public Boolean addDeviceMessage(DeviceMessage deviceMessage){
        //添加新的设备报警电话
        Integer i = deviceMessageMapper.addDeviceMessage(deviceMessage);
        return i==1;
    }

    public List<DeviceMessage> getDeviceMessageById(String deviceId){//根据设备的id查找设备对应的所有报警电话
        return deviceMessageMapper.getDeviceMessageById(deviceId);
    }

    public Boolean deleteDeviceMessage(String deviceId,String phoneNumber){//根据设备的id和手机号删除设备对应设备的对应电话号码
        Integer i = deviceMessageMapper.deleteDeviceMessage(deviceId,phoneNumber);
        return i==1;
    }
}
