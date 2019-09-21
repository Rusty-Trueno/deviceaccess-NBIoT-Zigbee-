package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.transport.mqtt.session.MqttSessionId;

/**
 * Created by Administrator on 2018/4/25.
 */
public class BasicFromDeviceActorRpc implements FromDeviceActorToSessionActorMsg {
    private final String  sessionId;
    private final Device device;
    private final FromServerRpcMsg fromServerRpcMsg;

    public BasicFromDeviceActorRpc(String sessionId,Device device,FromServerRpcMsg fromServerRpcMsg){
        this.sessionId = sessionId;
        this.device = device;
        this.fromServerRpcMsg = fromServerRpcMsg;
    }

    @Override
    public String getDeviceId() {
        return device.getId().toString();
    }

    @Override
    public String getTenantId() {
        return device.getTenantId()+"";
    }

    @Override
    public SessionId getSessionId() {
        return new MqttSessionId(Integer.parseInt(sessionId.substring(4)));
    }

    @Override
    public FromServerRpcMsg getServerMsg() {
        return fromServerRpcMsg;
    }
}
