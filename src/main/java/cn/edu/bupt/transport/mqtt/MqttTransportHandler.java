package cn.edu.bupt.transport.mqtt;

import cn.edu.bupt.actor.service.SessionMsgProcessor;
import cn.edu.bupt.common.security.DeviceTokenCredentials;
import cn.edu.bupt.message.AdaptorToSessionActorMsg;
import cn.edu.bupt.message.BasicToDeviceActorMsg;
import cn.edu.bupt.message.MsgType;
import cn.edu.bupt.message.SessionCloseMsg;
import cn.edu.bupt.service.DeviceAuthService;
import cn.edu.bupt.service.DeviceService;
import cn.edu.bupt.transport.AdaptorException;
import cn.edu.bupt.transport.TransportAdaptor;
import cn.edu.bupt.transport.mqtt.session.DeviceSessionCtx;
import cn.edu.bupt.utils.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.*;
import static io.netty.handler.codec.mqtt.MqttMessageType.PINGRESP;
import static io.netty.handler.codec.mqtt.MqttQoS.*;

/**
 * Created by Administrator on 2018/4/13.
 */
public class MqttTransportHandler extends ChannelInboundHandlerAdapter implements GenericFutureListener<Future<? super Void>> {

    private static final MqttQoS MAX_SUPPORTED_QOS_LVL = AT_LEAST_ONCE;
    private volatile boolean connected;
    private SessionMsgProcessor processor;
    private DeviceService deviceService;
    private DeviceAuthService deviceAuthService;
    private DeviceSessionCtx deviceSessionCtx;
    private TransportAdaptor adaptor;

    public MqttTransportHandler(SessionMsgProcessor processor, DeviceService deviceService, DeviceAuthService deviceCredentialsService , TransportAdaptor adaptor){
        this.processor = processor;
        this.deviceService = deviceService;
        this.deviceAuthService = deviceCredentialsService;
        this.adaptor = adaptor;
        this.deviceSessionCtx = new DeviceSessionCtx(deviceCredentialsService,adaptor);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){

        if(msg instanceof MqttMessage){
            processMqttMsg(ctx,(MqttMessage)msg);
        }
    }

    private void processMqttMsg(ChannelHandlerContext ctx, MqttMessage msg) {
        if(msg.fixedHeader() == null){
            //TODO 输出异常连接消息
            processDisconnect(ctx);
        }else{
            deviceSessionCtx.setChannelHandlerContext(ctx);
            switch(msg.fixedHeader().messageType()){
                case CONNECT:
                    processConnect(ctx, (MqttConnectMessage) msg);
                    break;
                case PUBLISH:
                    processPublish(ctx, (MqttPublishMessage) msg);
                    break;
                case SUBSCRIBE:
                    processSubscribe(ctx, (MqttSubscribeMessage) msg);
                    break;
                case UNSUBSCRIBE:
                    processUnsubscribe(ctx, (MqttUnsubscribeMessage) msg);
                    break;
                case PINGREQ:
                    if (checkConnected(ctx)) {
                        ctx.writeAndFlush(new MqttMessage(new MqttFixedHeader(PINGRESP, false, AT_MOST_ONCE, false, 0)));
                    }
                    break;
                case DISCONNECT:
                    if (checkConnected(ctx)) {
                        processDisconnect(ctx);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void processSubscribe(ChannelHandlerContext ctx, MqttSubscribeMessage msg){
        if(!checkConnected(ctx)){
            return ;
        }
//        System.out.println("subscribe: " + msg);
        List<Integer> grantedQoSList = new ArrayList<>();
        for(MqttTopicSubscription subscription : msg.payload().topicSubscriptions()){
            String topicName = subscription.topicName();
            MqttQoS reqQoS = subscription.qualityOfService();
            try{
                if(topicName.equals(MqttTopics.DEVICE_RPC_REQUESTS_SUB_TOPIC)){
                    AdaptorToSessionActorMsg msg1 = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.FROM_DEVICE_RPC_SUB,msg);
                    processor.process(new BasicToDeviceActorMsg(msg1,deviceSessionCtx.getDevice()));
                    grantedQoSList.add(getMinSupportedQos(reqQoS));
                }
            }catch(Exception e){
                grantedQoSList.add(FAILURE.value());
            }
        }
        ctx.writeAndFlush(MqttMsgFactory.createSubAckMessage(msg.variableHeader().messageId(),grantedQoSList));
    }

    private void processUnsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage mqttMsg) {
        if (!checkConnected(ctx)) {
            return;
        }
        for (String topicName : mqttMsg.payload().topics()) {
            try {
                if (topicName.equals(MqttTopics.DEVICE_RPC_REQUESTS_SUB_TOPIC)) {
                    AdaptorToSessionActorMsg msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.FROM_DEVICE_RPC_UNSUB, mqttMsg);
                    processor.process(new BasicToDeviceActorMsg(msg,deviceSessionCtx.getDevice()));
                }
            } catch (AdaptorException e) {
                e.printStackTrace();
            }
        }
        ctx.writeAndFlush(MqttMsgFactory.createUnSubAckMessage(mqttMsg.variableHeader().messageId()));
    }

    private boolean checkConnected(ChannelHandlerContext ctx) {
        if(connected)
            return true;
        else
            ctx.close();
        return false;
    }

    private void processPublish(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg) {
        if(!checkConnected(ctx)){
            return;
        }
        String topicName = mqttMsg.variableHeader().topicName();
        System.out.println("MqttTransportHandler--------topicName:" + topicName);
        int msgId = mqttMsg.variableHeader().messageId();
//        System.out.println("messageId:" + msgId);
//        System.out.println("publish:" + mqttMsg.payload().toString(CharsetUtil.UTF_8));
        processDevicePublish(ctx, mqttMsg, topicName, msgId);

    }

    private void processDevicePublish(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg, String topicName, int msgId) {
        AdaptorToSessionActorMsg msg = null ;
        try{   //TODO 其他数据类型处理
            if(topicName.equals(MqttTopics.DEVICE_TELEMETRY_TOPIC)){
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.POST_TELEMETRY_REQUEST,mqttMsg);
            }else if(topicName.equals(MqttTopics.DEVICE_ATTRIBUTES_TOPIC)){
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.POST_ATTRIBUTE_REQUEST,mqttMsg);
            }else if(topicName.startsWith(MqttTopics.DEVICE_RPC_RESPONSE_TOPIC)){
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.FROM_DEVICE_RPC_RESPONSE,mqttMsg);
            }else if(topicName.startsWith(MqttTopics.DEVICE_TELEMETRY_TOPIC)) {
                deviceSessionCtx.login(new DeviceTokenCredentials(topicName.substring(24)));
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.NBIOT_TELEMETRY_REQUEST,mqttMsg);
            }else if(topicName.startsWith(MqttTopics.DEVICE_ATTRIBUTES_TOPIC)){
                deviceSessionCtx.login(new DeviceTokenCredentials(topicName.substring(25)));
                msg = adaptor.convertToActorMsg(deviceSessionCtx, MsgType.NBIOT_ATTRIBUTE_REQUEST,mqttMsg);
            } else {
                System.out.println("convert data error: unknown Type");
            }

        }catch(Exception e){
            //TODO 异常待处理
        }
        if(msg!=null){
            System.out.println(deviceSessionCtx.getDevice().toString());
            processor.process(new BasicToDeviceActorMsg(msg,deviceSessionCtx.getDevice()));
            System.out.println("received a data "+msg.getMsg().getMsgType());
        }else{

        }

    }

    private void processConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        //TODO 后期引入其他鉴权方式
        processAuthTokenConnect(ctx, msg);
    }

    private void processAuthTokenConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {
        String token = msg.payload().userName();
        System.out.println(token);
        if(StringUtil.isEmpty(token)){
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD));
            ctx.close();
        }else if("nbiot".equals(token)){
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_ACCEPTED));
            connected = true;
        }
        else if(!deviceSessionCtx.login(new DeviceTokenCredentials(msg.payload().userName())) ){
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_REFUSED_NOT_AUTHORIZED));
            ctx.close();
        }else{
            ctx.writeAndFlush(MqttMsgFactory.createMqttConnAckMsg(CONNECTION_ACCEPTED));
            connected = true;
        }
    }

    private void processDisconnect(ChannelHandlerContext ctx) {
        ctx.close();
        if(connected){
            //TODO 避免抛出异常暂时关掉
            processor.process(SessionCloseMsg.onDisconnected(deviceSessionCtx.getSessionId()));
        }
    }

    private Integer getMinSupportedQos(MqttQoS reqQoS) {
        return Math.min(reqQoS.value(), MAX_SUPPORTED_QOS_LVL.value());
    }


    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {
        processor.process(new SessionCloseMsg(deviceSessionCtx.getSessionId(),false,false));
    }
}
