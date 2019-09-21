package cn.edu.bupt.actor.actors.app;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.Creator;
import akka.japi.Function;
import cn.edu.bupt.actor.actors.ContextAwareActor;
import cn.edu.bupt.actor.actors.tenant.TenantActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.message.*;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17.
 */
public class AppActor extends ContextAwareActor {

    private final Map<String, ActorRef> tenantActors;

    private AppActor(ActorSystemContext systemContext) {
        super(systemContext);
        this.tenantActors = new HashMap<>();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof FromSessionActorToDeviceActorMsg){
            System.out.println("appactor deviceId:"+((FromSessionActorToDeviceActorMsg) msg).getDeviceId());
            System.out.println("appactor tenantId:" + ((FromSessionActorToDeviceActorMsg) msg).getTenantId());
            System.out.println("appactor sessionId:" + ((FromSessionActorToDeviceActorMsg) msg).getSessionId());
            System.out.println("appactor:" + msg.toString());
            process((FromSessionActorToDeviceActorMsg)msg);
        }else if(msg instanceof FromServerMsg){
            process((FromServerMsg)msg);
            System.out.println("FromServerMsg");
        }else if(msg instanceof BasicFromServerMsg){
            process((BasicFromServerMsg)msg);
            System.out.println("BasicFromServerMsg");
        }
    }

    private void process(FromServerMsg msg) {
        if(msg.getMsgType().equals(MsgType.FROM_SERVER_RPC_MSG)){
            String tenantId = ((BasicFromServerRpcMsg)msg).getTenantId();
            getOrCreateTenantActor(tenantId).tell(msg,ActorRef.noSender());
        }
    }

    private void process(FromSessionActorToDeviceActorMsg msg) {
        getOrCreateTenantActor(msg.getTenantId()).tell(msg,ActorRef.noSender());
    }

    private void process(BasicFromServerMsg msg) {
        String tenantId = msg .getTenantId();
        getOrCreateTenantActor(tenantId).tell(msg,ActorRef.noSender());
    }

    private ActorRef getOrCreateTenantActor(String tenantId) {
        return tenantActors.computeIfAbsent(tenantId, k -> context().actorOf(Props.create(new TenantActor.ActorCreator(systemContext, tenantId))
                .withDispatcher(DefaultActorService.CORE_DISPATCHER_NAME), tenantId));
    }

    private final SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
        @Override
        public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof RuntimeException) {
                return SupervisorStrategy.restart();
            } else {
                return SupervisorStrategy.stop();
            }
        }
    });

    public static class ActorCreator implements Creator<AppActor> {
        private static final long serialVersionUID = 1L;
        private final transient ActorSystemContext context;

        public ActorCreator(ActorSystemContext context) {
            this.context = context;
        }

        @Override
        public AppActor create() throws Exception {
            return new AppActor(context);
        }
    }
}
