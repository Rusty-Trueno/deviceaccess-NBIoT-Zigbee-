package cn.edu.bupt.message;

/**
 * Created by Administrator on 2018/4/16.
 */
public class RpcSubscribeMsg implements FromDeviceMsg {
    @Override
    public String getMsgType() {
        return MsgType.FROM_DEVICE_RPC_SUB;
    }
}
