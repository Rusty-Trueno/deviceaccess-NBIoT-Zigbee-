package cn.edu.bupt.actor.actors.tenant;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.actors.device.DeviceActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.message.*;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17.
 */
public class TenantActor extends ContextAwareActor {

    private final Map<String, ActorRef> deviceActors;
    private final String tenantId;
    private final String DEVICE_OFFLINE  = "device is offline";

    public TenantActor(ActorSystemContext systemContext,String tenantId){
        super(systemContext);
        this.deviceActors = new HashMap<>();
        this.tenantId = tenantId;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof BasicToDeviceActorMsg){
            getOrCreateDeviceActor(((BasicToDeviceActorMsg) msg).getDeviceId()).tell(msg,ActorRef.noSender());
        }else if(msg instanceof BasicToDeviceActorSessionMsg){
            //TODO 待完成
            ActorRef ref = deviceActors.get(((BasicToDeviceActorSessionMsg)msg).getDeviceId());
            if(ref != null){
                //TODO 延时时间改为通过配置文件注入
                scheduleMsgWithDelay(msg,0,ref);
            }
        }else if(msg instanceof  DeviceTerminationMsg){
            ActorRef ref =  deviceActors.remove(((DeviceTerminationMsg)msg).getDeviceId());
            if(ref!=null){
                //TODO 打日志
            }else{
                //TODO 打日志
            }
        }else if(msg instanceof FromServerMsg){
            process((FromServerMsg)msg);
        }else if(msg instanceof BasicFromServerMsg) {
            process((BasicFromServerMsg)msg);
        }
    }

    private void process(FromServerMsg msg) {
        if(msg.getMsgType().equals(MsgType.FROM_SERVER_RPC_MSG)){
            String deviceId = ((BasicFromServerRpcMsg)msg).getDeviceId();
            if(deviceActors.containsKey(deviceId)){
                deviceActors.get(deviceId).tell(msg,ActorRef.noSender());
            }else{
                ((BasicFromServerRpcMsg)msg).getRes().setResult(new ResponseEntity(DEVICE_OFFLINE,HttpStatus.OK));
            }
        }
    }

    private void process(BasicFromServerMsg msg){
        JsonObject jsonObject = new JsonObject();
        List<String> deviceIds= msg.getDeviceIds();
        for(String deviceId:deviceIds){
            if(deviceActors.containsKey(deviceId)){
                jsonObject.addProperty(deviceId,"online");
            }else {
                jsonObject.addProperty(deviceId,"offline");
            }
        }
        msg.getRes().setResult(new ResponseEntity(jsonObject.toString(),HttpStatus.OK));
    }

    private ActorRef getOrCreateDeviceActor(String deviceId) {
        return deviceActors.computeIfAbsent(deviceId, k -> context().actorOf(Props.create(new DeviceActor.ActorCreator(systemContext, tenantId, deviceId))
                .withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME), deviceId.toString()));
    }

    public static class ActorCreator implements Creator<TenantActor> {
        private static final long serialVersionUID = 1L;
        private final transient ActorSystemContext context;
        private final String tenantId;

        public ActorCreator(ActorSystemContext context,String tenantId) {
            this.context = context;
            this.tenantId = tenantId;
        }

        @Override
        public TenantActor create() throws Exception {
            return new TenantActor(context,tenantId);
        }
    }
}
