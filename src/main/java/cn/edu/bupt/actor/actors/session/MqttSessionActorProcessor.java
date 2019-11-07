package cn.edu.bupt.actor.actors.session;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.DeviceAwareSessionContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.*;
import cn.edu.bupt.transport.mqtt.MqttTopics;
import cn.edu.bupt.transport.mqtt.session.DeviceSessionCtx;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.mqtt.*;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/4/17.
 */
public class MqttSessionActorProcessor implements SessionActorProcessor {

    protected final ActorSystemContext systemContext;
    private boolean firstMsg = true;
    protected final SessionId sessionId;
    protected DeviceAwareSessionContext sessionCtx;
    private AtomicInteger next = new AtomicInteger(0);
    private static final ByteBufAllocator ALLOCATOR = new UnpooledByteBufAllocator(false);

    public MqttSessionActorProcessor(ActorSystemContext systemContext, SessionId sessionId, DeviceAwareSessionContext sessionCtx){
        this.systemContext = systemContext;
        this.sessionId = sessionId;
        this.sessionCtx = sessionCtx;
    }

    @Override
    public void processSessionCtrlMsg(ActorContext context, SessionCtrlMsg msg) {
        if(msg instanceof SessionCloseMsg){
            cleanupSession(msg,context);
            context.parent().tell(new SessionTerminationMsg(msg.getSessionId()), ActorRef.noSender());
            context.stop(context.self());
        }
    }

    @Override
    public void processToDeviceActorMsg(ActorContext context, FromSessionActorToDeviceActorMsg msg) {
        updateSessionCtx(msg);
        systemContext.getAppActor().tell(msg, ActorRef.noSender());
    }

    @Override
    public void processToDeviceRpcRequestMsg(int requestId, String data) {
        String topic = MqttTopics.DEVICE_RPC_REQUESTS_TOPIC+requestId;
        MqttMessage msg = createMqttPublishMsg(topic,data);
        if(sessionCtx instanceof DeviceSessionCtx){
            ((DeviceSessionCtx)sessionCtx).getChannelHandlerContext().writeAndFlush(msg);
        }
    }
    private MqttPublishMessage createMqttPublishMsg(String topic, String  data) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttPublishVariableHeader header = new MqttPublishVariableHeader(topic,next.incrementAndGet());
        ByteBuf payload = ALLOCATOR.buffer();
        payload.writeBytes(data.getBytes(Charset.forName("UTF-8")));
        return new MqttPublishMessage(mqttFixedHeader, header, payload);
    }
    private void updateSessionCtx(FromSessionActorToDeviceActorMsg msg) {
        if(msg instanceof  BasicToDeviceActorMsg){
            AdaptorToSessionActorMsg msg1 = ((BasicToDeviceActorMsg)msg).getMsg();
            if(msg1 instanceof BasicAdapterToSessionActorMsg){
                sessionCtx = ((BasicAdapterToSessionActorMsg)msg1).getContext();
            }
        }
    }

    private void cleanupSession(SessionCtrlMsg msg, ActorContext context) {
        systemContext.getAppActor().tell(new BasicToDeviceActorSessionMsg(msg,sessionCtx.getDevice()),ActorRef.noSender());
    }


}
