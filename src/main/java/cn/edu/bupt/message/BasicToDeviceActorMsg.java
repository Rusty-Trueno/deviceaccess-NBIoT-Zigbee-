package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.pojo.Device;
import lombok.Getter;

/**
 * Created by Administrator on 2018/4/17.
 */
public class BasicToDeviceActorMsg implements FromSessionActorToDeviceActorMsg {

    AdaptorToSessionActorMsg msg;
    @Getter
    Device device;
    public BasicToDeviceActorMsg(AdaptorToSessionActorMsg msg, Device device){
        this.msg = msg;
        this.device = device;
    }
    public AdaptorToSessionActorMsg getMsg(){
        return msg;
    }

    @Override
    public String getDeviceId() {
        return device.getId().toString();
    }
    @Override
    public String getTenantId() {
        System.out.println("basicTodevice:"+device);
        return device.getTenantId().toString();
    }
    @Override
    public SessionId getSessionId() {
        return msg.getSessionId();
    }
}
