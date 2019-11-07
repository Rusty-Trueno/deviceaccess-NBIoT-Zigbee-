package cn.edu.bupt.actor.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.edu.bupt.actor.service.ActorSystemContext;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/4/16.
 */
public abstract class ContextAwareActor extends UntypedActor {
    public static final int ENTITY_PACK_LIMIT = 1024;

    protected final ActorSystemContext systemContext;

    public ContextAwareActor(ActorSystemContext systemContext) {
        super();
        this.systemContext = systemContext;
    }

    protected void scheduleMsgWithDelay(Object msg, long delayInMs, ActorRef target) {
      //  getScheduler().scheduleOnce(Duration.create(delayInMs, TimeUnit.MILLISECONDS), target, msg, getSystemDispatcher(), null);
        systemContext.getActorSystem().scheduler().scheduleOnce(Duration.create(delayInMs,TimeUnit.MILLISECONDS),
                target,msg,systemContext.getActorSystem().dispatcher(),null);
    }

}
