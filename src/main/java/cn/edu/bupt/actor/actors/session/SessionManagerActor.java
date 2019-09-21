package cn.edu.bupt.actor.actors.session;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.FromSessionActorToDeviceActorMsg;
import cn.edu.bupt.message.SessionAwareMsg;
import cn.edu.bupt.message.SessionCtrlMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
public class SessionManagerActor extends ContextAwareActor {

    private static final int INITIAL_SESSION_MAP_SIZE = 1024;
    private final Map<String, ActorRef> sessionActors;

    public SessionManagerActor(ActorSystemContext context){
        super(context);
        sessionActors = new HashMap<>(INITIAL_SESSION_MAP_SIZE);
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        System.out.println("SessionManager receive "+ msg);
        if(msg instanceof SessionCtrlMsg){
            onSessionCtrlMsg((SessionCtrlMsg)msg);
        }else if(msg instanceof SessionAwareMsg){
            forwardToSessionActor((SessionAwareMsg) msg);
        }else if(msg instanceof SessionTerminationMsg){
             onSessionTermination((SessionTerminationMsg)msg);
        }
    }

    private void onSessionTermination(SessionTerminationMsg msg) {
        String sessionIdStr = msg.getSessionId().toUidStr();
        ActorRef sessionActor = sessionActors.remove(sessionIdStr);
        if(sessionActor != null){
            //TODO 打log
        }else{
            //TODO 打l og
        }
    }

    private void onSessionCtrlMsg(SessionCtrlMsg msg) {
        String sessionIdstr = msg.getSessionId().toUidStr();
        ActorRef sessionActor = sessionActors.get(sessionIdstr);
        if(sessionActor!=null){
            sessionActor.tell(msg,ActorRef.noSender());
        }
    }

    private void forwardToSessionActor(SessionAwareMsg msg) {
        if (msg instanceof FromSessionActorToDeviceActorMsg) {
            try{
                getOrCreateSessionActor(msg.getSessionId()).tell(msg,self());
            }catch(Exception e){
                System.out.println("method [getOrCreateSessionActor] throws an Exception");
                e.printStackTrace();
                //TODO 待补全
            }
        }else{
            String sessionIdStr = msg.getSessionId().toUidStr();
            ActorRef sessionActor = sessionActors.get(sessionIdStr);
            if(sessionActor!=null){
                sessionActor.tell(msg,ActorRef.noSender());
            }else{
                //TODO log.debug session actor was removed

            }
        }
    }

    private ActorRef getOrCreateSessionActor(SessionId sessionId) throws Exception{
        String sessionIdStr = sessionId.toUidStr();
        ActorRef sessionActor = sessionActors.get(sessionIdStr);
        if(sessionActor == null){
            sessionActor = context().actorOf(Props.create(new SessionActor.ActorCreator(systemContext,sessionId)).
                    withDispatcher("session-dispatcher"),sessionIdStr);
            sessionActors.put(sessionIdStr, sessionActor);
        }
        return sessionActor;
    }

    public static class ActorCreator implements Creator<SessionManagerActor>{
        private static final long serialVersionUID = 1L;
        private final ActorSystemContext context;

        public ActorCreator(ActorSystemContext context) {
            this.context = context;
        }
        @Override
        public SessionManagerActor create() throws Exception {
            return new SessionManagerActor(context);
        }
    }

}
