package cn.edu.bupt.message;

/**
 * Created by Administrator on 2018/4/16.
 */
public interface FromDeviceActorToSessionActorMsg extends SessionAwareMsg,DeviceAwareMsg,TenantAwareMsg {
    public FromServerRpcMsg getServerMsg();
}
