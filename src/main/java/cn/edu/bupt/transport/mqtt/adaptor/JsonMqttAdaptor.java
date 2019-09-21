package cn.edu.bupt.transport.mqtt.adaptor;

import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.message.*;
import cn.edu.bupt.transport.AdaptorException;
import cn.edu.bupt.transport.TransportAdaptor;
import cn.edu.bupt.transport.mqtt.MqttTopics;
import cn.edu.bupt.transport.mqtt.session.DeviceSessionCtx;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;

import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public class JsonMqttAdaptor implements TransportAdaptor<DeviceSessionCtx,MqttMessage,MqttMessage>{
    private static final Gson GSON = new Gson();
    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public AdaptorToSessionActorMsg convertToActorMsg(DeviceSessionCtx ctx, String msgType, MqttMessage inbound) throws AdaptorException {
        FromDeviceMsg msg = null;
        switch(msgType){
            case MsgType.POST_TELEMETRY_REQUEST:
                msg = convertToTelemetryUploadRequest(ctx,(MqttPublishMessage) inbound);
                break;
            case MsgType.POST_ATTRIBUTE_REQUEST:
                msg = convertToUpdateAttributesRequest(ctx, (MqttPublishMessage) inbound);
                break;
            case MsgType.FROM_DEVICE_RPC_SUB:
                msg = convertToRpcSubRequest(ctx,(MqttSubscribeMessage)inbound);
                break;
            case MsgType.FROM_DEVICE_RPC_UNSUB:
                msg = convertToRpcUnSubRequest(ctx,(MqttUnsubscribeMessage)inbound);
                break;
            case MsgType.FROM_DEVICE_RPC_RESPONSE:
                msg = convertToRpcResponseRequest(ctx,(MqttPublishMessage) inbound);
                break;
            case MsgType.NBIOT_TELEMETRY_REQUEST:
                System.out.println("JsonMqtt telemetry");
                msg = convertToTelemetryUploadRequest(ctx,(MqttPublishMessage) inbound);
                break;
            case MsgType.NBIOT_ATTRIBUTE_REQUEST:
                msg= convertToUpdateAttributesRequest(ctx,(MqttPublishMessage) inbound);
                break;
        }
        return new BasicAdapterToSessionActorMsg(ctx,msg);
    }

    @Override
    public Optional<MqttMessage> convertToAdaptorMsg(DeviceSessionCtx ctx, SessionActorToAdaptorMsg msg) throws AdaptorException {
        return null;
    }

    private FromDeviceMsg convertToRpcUnSubRequest(DeviceSessionCtx ctx, MqttUnsubscribeMessage inbound) {
        return new RpcUnSubscribeMsg();
    }

    private FromDeviceMsg convertToRpcSubRequest(DeviceSessionCtx ctx, MqttSubscribeMessage inbound) {
        return new RpcSubscribeMsg();
    }

    private FromDeviceMsg convertToRpcResponseRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException{
        String topicName = inbound.variableHeader().topicName();
        try{
            int requestId = Integer.valueOf(topicName.substring(MqttTopics.DEVICE_RPC_RESPONSE_TOPIC.length()));
            String payload = inbound.payload().toString(UTF8);
            return new FromDeviceRpcResponse(requestId,payload);
        }catch(Exception e){
            throw new AdaptorException(e.toString());
        }
    }


    private FromDeviceMsg convertToUpdateAttributesRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException {
        String payLoad = validatePayload(ctx.getSessionId(),inbound.payload());
        System.out.println("payLoad:" + payLoad);
        try{
            return JsonConverter.convertToAttribute(new JsonParser().parse(payLoad),inbound.variableHeader().messageId());
        }catch(Exception e){
            throw new AdaptorException(e.toString());
        }
    }

    private FromDeviceMsg convertToTelemetryUploadRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException  {
        String payLoad = validatePayload(ctx.getSessionId(),inbound.payload());
        try {
            return JsonConverter.convertToTelemetry(new JsonParser().parse(payLoad),inbound.variableHeader().messageId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AdaptorException(e.toString());
        }
    }

    private String validatePayload(SessionId sessionId, ByteBuf payloadData) throws AdaptorException {
        try{
            String payload = payloadData.toString(UTF8);
            if(payload==null){
                throw new AdaptorException("payload is empty");
            }
            return payload;
        }finally {
            payloadData.release();
        }
    }
}
