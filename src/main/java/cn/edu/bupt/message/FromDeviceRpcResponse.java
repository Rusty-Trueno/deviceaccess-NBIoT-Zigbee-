package cn.edu.bupt.message;

import lombok.Getter;

/**
 * Created by Administrator on 2018/4/16.
 */
public class FromDeviceRpcResponse implements FromDeviceMsg {
    @Getter
    private final int requestId;
    @Getter
    private final String data;
    public FromDeviceRpcResponse(int id, String data){
        requestId = id;
        this.data = data;
    }
    @Override
    public String getMsgType() {
        return MsgType.FROM_DEVICE_RPC_RESPONSE;
    }
}
