package cn.edu.bupt.message;

import cn.edu.bupt.pojo.Device;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by Administrator on 2018/4/24.
 */
public class BasicFromServerRpcMsg  implements FromServerRpcMsg{

    private final int requestId;
    private final String data;
    @Getter
    private final Device device;
    @Getter
    private final DeferredResult<ResponseEntity> res;

    @Setter
    private final JsonObject service;


   public  BasicFromServerRpcMsg(int id,String data,Device device,DeferredResult<ResponseEntity> res,JsonObject service){
        this.requestId = id;
        this.data = data;
        this.device = device;
        this.res = res;
        this.service = service;
   }

    @Override
    public String getDeviceId() {
        return device.getId().toString();
    }

    public String getMsgType() {
        return MsgType.FROM_SERVER_RPC_MSG;
    }

    @Override
    public int getRpcRequestId() {
        return requestId;
    }

    @Override
    public String getRpcRequestPayLoad() {
        return data;
    }

    @Override
    public String getTenantId() {
        return device.getTenantId()+"";
    }

    public void setService(boolean required) {
       this.service.addProperty("requireResponse", required);
    }

    @Override
    public boolean requireResponse() {
       //TODO 需要根据情况更改
        if (service.has("requireResponse")){
            return service.getAsJsonPrimitive("requireResponse").getAsBoolean();
        }else{
            return false;
        }
    }
}
