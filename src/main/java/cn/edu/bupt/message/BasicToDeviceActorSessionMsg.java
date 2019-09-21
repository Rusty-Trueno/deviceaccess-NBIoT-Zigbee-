package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.pojo.Device;

/**
 * Created by Administrator on 2018/4/17.
 */
public class BasicToDeviceActorSessionMsg implements FromSessionActorToDeviceActorMsg {

    private final SessionAwareMsg msg;
    private final Device device;

    public BasicToDeviceActorSessionMsg(SessionAwareMsg msg, Device device){
        this.msg = msg;
        this.device = device;
    }

    public SessionAwareMsg getMsg(){
        return msg;
    }

    @Override
    public String getDeviceId() {
        return device.getId().toString();
    }

    @Override
    public String getTenantId() {
        return device.getTenantId().toString();
    }

    @Override
    public SessionId getSessionId() {
        return msg.getSessionId();
    }
}
