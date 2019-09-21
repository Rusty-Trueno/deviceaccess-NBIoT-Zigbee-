package cn.edu.bupt.actor.actors.device;

import akka.actor.ActorRef;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.actors.tenant.DeviceTerminationMsg;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.message.*;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DeviceActor extends ContextAwareActor {

    private final String tenantId;
    private final String deviceId;
    private final DeviceActorMsgProcessor processor;

    private DeviceActor(ActorSystemContext systemContext, String tenantId, String deviceId){
        super(systemContext);
        this.tenantId = tenantId;
        this.deviceId = deviceId;
        // TODO 待修改
        this.processor  = new DeviceActorMsgProcessor(systemContext);
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof BasicToDeviceActorMsg){
            processor.process((BasicToDeviceActorMsg)msg);
        }else if(msg instanceof BasicToDeviceActorSessionMsg){
            SessionAwareMsg msg1 = ((BasicToDeviceActorSessionMsg) msg).getMsg();
            String deviceId = ((BasicToDeviceActorSessionMsg) msg).getDeviceId();
            if(msg1 instanceof SessionCloseMsg){
                if(processor.jugeWhetherDie(msg1.getSessionId())) {
                    System.out.println("kill current device actor");
                    context().parent().tell(new DeviceTerminationMsg(deviceId), ActorRef.noSender());
                    context().stop(context().self());
                }else{
                    System.out.println("should not kill current device actor");
                }
            }
        }else if(msg instanceof FromServerMsg){
            processor.process((FromServerMsg)msg);
        }
    }

    public static class ActorCreator implements Creator<DeviceActor> {
        private static final long serialVersionUID = 1L;
        private final String tenantId;
        private final String deviceId;
        private final transient ActorSystemContext context;

        public ActorCreator(ActorSystemContext context, String  tenantId, String deviceId) {
            this.context = context;
            this.tenantId = tenantId;
            this.deviceId = deviceId;
        }

        @Override
        public DeviceActor create() throws Exception {
            return new DeviceActor(context,tenantId,deviceId);
        }
    }
}
