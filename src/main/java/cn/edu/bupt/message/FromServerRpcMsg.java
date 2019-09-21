package cn.edu.bupt.message;

/**
 * Created by Administrator on 2018/4/24.
 */
public interface FromServerRpcMsg extends FromServerMsg,DeviceAwareMsg,TenantAwareMsg {
    public int getRpcRequestId();
    public String getRpcRequestPayLoad();
    public boolean requireResponse();
}
