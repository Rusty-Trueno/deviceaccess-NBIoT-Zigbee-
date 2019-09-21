package cn.edu.bupt.actor.actors.device;

import akka.actor.ActorRef;
import cn.edu.bupt.actor.service.ActorSystemContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.common.entry.BasicAttributeKvEntry;
import cn.edu.bupt.common.entry.StringEntry;
import cn.edu.bupt.message.*;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import cn.edu.bupt.pojo.kv.BasicAdaptorAttributeKvEntry;
import cn.edu.bupt.pojo.kv.BasicAdaptorTsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.service.BaseAttributesService;
import cn.edu.bupt.service.BaseTimeseriesService;
import cn.edu.bupt.utils.KafkaUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DeviceActorMsgProcessor {

    //cong之前的set改为map，键为sessionid，值为订阅次数（这里不确定短线重连是否会复用前一个绘话，如果会复用，那这样写没问题）
    //如果不复用，这么些也没问题
    private final Map<String,Integer> subscriptions;
    private final  Map<Integer,DeferredResult<ResponseEntity>> rpcRequests;
    private final ActorSystemContext actorSystemContext;
    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    public  DeviceActorMsgProcessor(ActorSystemContext actorSystemContext){
        rpcRequests  = new HashMap<>();
        subscriptions = new HashMap<>();
        this.actorSystemContext = actorSystemContext;
    }

    public void process(BasicToDeviceActorMsg msg) throws IOException {
        BasicToDeviceActorMsg msg1 = (BasicToDeviceActorMsg)msg;
        AdaptorToSessionActorMsg adptorMsg = msg1.getMsg();
        FromDeviceMsg fromDeviceMsg = adptorMsg.getMsg();
        switch(fromDeviceMsg.getMsgType()){
            case MsgType.POST_TELEMETRY_REQUEST:
                handleTelemetryUploadRequest((TelemetryUploadMsg)fromDeviceMsg, msg1);
                System.err.println("receive a telemetry msg");
                break;
            case MsgType.POST_ATTRIBUTE_REQUEST:
                handleAttributeUploadRequest((AttributeUploadMsg)fromDeviceMsg, msg1);
                System.err.println("receive a attribute msg");
                break;
            case MsgType.NBIOT_TELEMETRY_REQUEST:
                handleTelemetryUploadRequest((TelemetryUploadMsg)fromDeviceMsg, msg1);
                System.err.println("receive a nbiot telemetry msg");
                break;
            case MsgType.NBIOT_ATTRIBUTE_REQUEST:
                handleAttributeUploadRequest((AttributeUploadMsg)fromDeviceMsg, msg1);
                System.err.println("receive a attribute msg");
                break;
            case MsgType.FROM_DEVICE_RPC_SUB:
                System.err.println("receive a rpc sub");
                if(subscriptions.containsKey(adptorMsg.getSessionId().toUidStr())){
                    String uidStr = adptorMsg.getSessionId().toUidStr();
                    subscriptions.put(uidStr, subscriptions.get(uidStr)+1);
                }else {
                    subscriptions.put(adptorMsg.getSessionId().toUidStr(), 1);
                }
                break;
            case MsgType.FROM_DEVICE_RPC_UNSUB:
                System.err.println("receive a rpc unsub");
                subscriptions.remove(adptorMsg.getSessionId().toUidStr());
                break;
            case MsgType.FROM_DEVICE_RPC_RESPONSE:
                System.err.println("receive a rpc response");
                int requestId = ((FromDeviceRpcResponse)fromDeviceMsg).getRequestId();
                DeferredResult result = rpcRequests.get(requestId);
                if(result == null){
                    //TODO 记录一下rpc超时
                    System.err.println("request [" + requestId + "] rpc overtime");
                }else{
                    result.setResult(((FromDeviceRpcResponse)fromDeviceMsg).getData());
                    rpcRequests.remove(requestId);
                }
                break;
            default:
                System.err.println("unKnow msg type");
                break;
        }
    }

    public void process(FromServerMsg msg) {
        if(msg.getMsgType().equals(MsgType.FROM_SERVER_RPC_MSG)){
            BasicFromServerRpcMsg msg1 = (BasicFromServerRpcMsg)(msg);
            int requestId = msg1.getRpcRequestId();
            if(msg1.requireResponse()){
                rpcRequests.put(requestId, msg1.getRes());
                msg1.getRes().onTimeout(()->{
                    rpcRequests.remove(requestId);
                });
                subscriptions.keySet().forEach(sessionId->{
                    actorSystemContext.getSessionManagerActor().tell(
                            new BasicFromDeviceActorRpc(sessionId,msg1.getDevice(),msg1),
                            ActorRef.noSender()
                    );
                });
            }else{
                msg1.getRes().setResult(new ResponseEntity(HttpStatus.OK));
                subscriptions.keySet().forEach(sessionId->{
                    actorSystemContext.getSessionManagerActor().tell(
                            new BasicFromDeviceActorRpc(sessionId,msg1.getDevice(),msg1),
                            ActorRef.noSender()
                    );
                });
            }
        }
    }

    public boolean jugeWhetherDie(SessionId id){
        System.out.println("start to judge whether die and session = " +id);
        subscriptions.forEach((k,v) -> {
            System.out.println(k+"=>"+v);
        });
        if(subscriptions.isEmpty()){
            return true;
        }

        String idstr = id.toUidStr();

        if(!subscriptions.containsKey(idstr)){
            return false;
        }

        if(subscriptions.get(idstr)>1){
            subscriptions.put(idstr,subscriptions.get(idstr)-1);
            return false;
        }

        subscriptions.remove(idstr);

        if(subscriptions.isEmpty()){
            return true;
        }

        return false;
    }


     public void handleTelemetryUploadRequest(TelemetryUploadMsg msg, BasicToDeviceActorMsg msg1) throws IOException {

        Map<Long, List<cn.edu.bupt.common.entry.KvEntry>> data = msg.getData();

        for( long ts : data.keySet()){
            UUID entityId = UUID.fromString(msg1.getDeviceId());
            List<cn.edu.bupt.common.entry.KvEntry> KvEntry = data.get(ts);
            List<TsKvEntry> ls = new ArrayList<>();
            for(cn.edu.bupt.common.entry.KvEntry en : KvEntry){
                if(en.getKey().equals("picture")){
                    if(en instanceof StringEntry){
                        // 图片二进制转为图片，将路径存入
                        //String[] pathWords = {"root", "iot", "treatment", "img"} ;
                        String[] pathWords = {"D:\\pic", "oup"} ;
                        String path = String.join(File.separator, Arrays.asList(pathWords));
                        String pathname = path +"\\" + System.currentTimeMillis() +".jpg";
                        BufferedImage img = BinaryToImage(en.getValueAsString());
                        saveImage(img, pathname);
                        //设置路径存入
                        ((StringEntry) en).setValue(pathname);
                        ls.add(new BasicAdaptorTsKvEntry(ts, en));
                    }
                }else{
                    ls.add(new BasicAdaptorTsKvEntry(ts, en));
                }
            }
          /*  KvEntry.forEach(entry->{
                ls.add(new BasicAdaptorTsKvEntry(ts,entry));
            });*/
            BaseTimeseriesService baseTimeseriesService = actorSystemContext.getBaseTimeseriesService();
            baseTimeseriesService.save(entityId, ls, 0);
        }
        sendDataToKafka(msg1.getDevice(),data);
    }


    public void handleAttributeUploadRequest(AttributeUploadMsg msg, BasicToDeviceActorMsg msg1){
        Set<cn.edu.bupt.common.entry.KvEntry> atts = msg.getData();
        List<AttributeKvEntry> attributes = new ArrayList<>();
        atts.forEach(entry->{
            attributes.add(new BasicAdaptorAttributeKvEntry(entry,((BasicAttributeKvEntry)entry).getLastUpdateTs()));
        });
        UUID entityId = UUID.fromString(msg1.getDeviceId());
        BaseAttributesService baseAttributesService = actorSystemContext.getBaseAttributesService();
        baseAttributesService.save(entityId, attributes);
    }

    private void sendDataToKafka(Device device,Map<Long, List<cn.edu.bupt.common.entry.KvEntry>> data) throws IOException {
        JsonObject obj =  new JsonObject();
        obj.addProperty("deviceId",device.getId().toString());
        obj.addProperty("tenantId",device.getTenantId());
        obj.addProperty("gatewayId",device.getParentDeviceId());
        obj.addProperty("name",device.getName());
        obj.addProperty("manufacture",device.getManufacture());
        obj.addProperty("deviceType",device.getDeviceType());
        obj.addProperty("model",device.getModel());
        JsonArray array = new JsonArray();
        for(Map.Entry<Long,List<cn.edu.bupt.common.entry.KvEntry>> entry:data.entrySet()){
            long ts = entry.getKey();
            for(cn.edu.bupt.common.entry.KvEntry en:entry.getValue()){
                JsonObject temp = new JsonObject();
                temp.addProperty("ts",ts);
                temp.addProperty("key",en.getKey());
                switch(en.getDataType()){
                    case "string":
                        temp.addProperty("value",en.getStrValue().get());
                        break;
                    case "boolean":
                        temp.addProperty("value",en.getBooleanValue().get());
                        break;
                    case "long":
                        temp.addProperty("value",en.getLongValue().get());
                        break;
                    case "double":
                        temp.addProperty("value",en.getDoubleValue().get());
                        break;
                }
                array.add(temp);
            }
        }
        obj.add("data",array);

        //System.out.println(actorSystemContext.getWebSocketServer().map);
        if(actorSystemContext.getWebSocketServer().map.containsKey(device.getId().toString())) {
            for(Session session : actorSystemContext.getWebSocketServer().map.get(device.getId().toString()) )
                actorSystemContext.getWebSocketServer().sendMessage(obj.toString(),session);
        }
        if(actorSystemContext.getNewWebSocketServer().gatewaySessionMap.containsKey(device.getParentDeviceId()) && !device.getDeviceType().equals("temperature") && !device.getDeviceType().equals("Gateway") && !device.getDeviceType().equals("PM2.5") && !device.getDeviceType().equals("lightSensor")){
            for(Session session: actorSystemContext.getNewWebSocketServer().gatewaySessionMap.get(device.getParentDeviceId()))
                actorSystemContext.getNewWebSocketServer().sendMessage(obj.toString(),session);
        }

        if(device.getDeviceType().equals("IASZone") || device.getDeviceType().equals("PM2.5") || device.getDeviceType().equals("lightSensor") || device.getDeviceType().equals("temperature") || device.getDeviceType().equals("lock")){
            KafkaUtil.send("",obj.toString());
        }

    }



    public BufferedImage BinaryToImage(String binary) {
        try{
            byte[] bytes1 = decoder.decodeBuffer(binary);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage image = ImageIO.read(bais);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveImage(BufferedImage bufferedImage, String pathname) throws IOException {

        ImageIO.write(bufferedImage,"jpg", new File(pathname));
    }




}
