package cn.edu.bupt.actor.actors.session;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.DeviceAwareSessionContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.*;
import scala.concurrent.duration.Duration;

/**
 * Created by Administrator on 2018/4/16.
 */
public class SessionActor extends ContextAwareActor{
    private final SessionId sessionId;
    private SessionActorProcessor  processor;

    private SessionActor(ActorSystemContext context,SessionId sessionId){
        super(context);
        this.sessionId = sessionId;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(-1, Duration.Inf(),
                throwable -> {
                    if (throwable instanceof Error) {
                        return OneForOneStrategy.escalate();
                    } else {
                        return OneForOneStrategy.resume();
                    }
                });
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        System.out.println("session receive "+ msg);
        if(msg instanceof SessionCtrlMsg){
            processSessionCtrlMsg((SessionCtrlMsg)msg);
        }else if(msg instanceof FromDeviceActorToSessionActorMsg){
            processFromDeviceToSessionActorMsg((FromDeviceActorToSessionActorMsg)msg);
        }else if(msg instanceof FromSessionActorToDeviceActorMsg){
            processFromSessionActorToDeviceActorMsg((FromSessionActorToDeviceActorMsg)msg);
        }else if(msg instanceof SessionTimeoutMsg){
            System.out.println("Session " + ((SessionTimeoutMsg) msg).getSessionId() + " Time out");
        }
    }

    private void processFromDeviceToSessionActorMsg(FromDeviceActorToSessionActorMsg msg) {
        FromServerRpcMsg msg1 = (FromServerRpcMsg)msg.getServerMsg();
        int requestId = msg1.getRpcRequestId();
        String data = msg1.getRpcRequestPayLoad();
        processor.processToDeviceRpcRequestMsg(requestId,data);
    }

    private void processFromSessionActorToDeviceActorMsg(FromSessionActorToDeviceActorMsg msg) {
        initProcessor(msg);
        processor.processToDeviceActorMsg(context(), msg);
    }

    private void initProcessor(FromSessionActorToDeviceActorMsg msg) {
        if (processor == null) {
            DeviceAwareSessionContext context = ((BasicAdapterToSessionActorMsg)(((BasicToDeviceActorMsg)msg).getMsg())).getContext();
            processor = new MqttSessionActorProcessor(systemContext,msg.getSessionId(),context);
        }
    }

    private void processSessionCtrlMsg(SessionCtrlMsg msg) {
        if(processor != null){
            processor.processSessionCtrlMsg(context(),msg);
        }else{
            if(msg instanceof SessionCloseMsg){
                context().parent().tell(new SessionTerminationMsg(msg.getSessionId()), ActorRef.noSender());
                context().stop(context().self());
            }
            // TODO 打日志
        }
    }

    public static class ActorCreator implements Creator<SessionActor> {
        private static final long serialVersionUID = 1L;
        private final SessionId sessionId;
        private final transient ActorSystemContext context;

        public ActorCreator(ActorSystemContext context, SessionId sessionId) {
            this.context = context;
            this.sessionId = sessionId;
        }

        @Override
        public SessionActor create() throws Exception {
            return new SessionActor(context, sessionId);
        }
    }
}
