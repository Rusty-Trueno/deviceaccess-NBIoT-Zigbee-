package cn.edu.bupt.websocket;

import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.service.BaseTimeseriesService;
import cn.edu.bupt.service.DeviceService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
@ServerEndpoint(value = "/api/v1/deviceaccess/websocket")
@Component
public class WebSocketServer /*extends WebSocketBaseServer*/{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    protected BaseTimeseriesService baseTimeseriesService;

    private static DeviceService deviceStaticService;

    private static BaseTimeseriesService baseTimeseriesStaticService;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    public String deviceId = null;
    //public static Set<Session> sessions= new HashSet<>();
    public static Map<String,Set<Session>> map = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @PostConstruct
    public void init(){
        deviceStaticService = deviceService;
        baseTimeseriesStaticService = baseTimeseriesService;
    }


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        addOnlineCount();           //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
       /* try {
            sendMessage(this.hashCode()+"",this.session);
        } catch (IOException e) {
            log.error("websocket IO异常");
        }*/
    }

    public void sendMessage(String message,Session session) throws IOException {
        System.out.println(message);
        session.getBasicRemote().sendText(message);
    }

    //  //连接打开时执行
    //  @OnOpen
    //  public void onOpen(@PathParam("user") String user, Session session) {
    //      currentUser = user;
    //      System.out.println("Connected ... " + session.getId());
    //  }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //webSocketSet.remove(this);  //从set中删除
        map.get(deviceId).remove(this.session);
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, ExecutionException, InterruptedException {
        log.info("来自客户端的消息:" + message);

        if(message.equals("@heart")){
            //sendMessage("@heart",this.session);
        }else{
            JsonObject jsonObj = (JsonObject)new JsonParser().parse(message);
            String deviceId = jsonObj.get("deviceId").getAsString();
            this.deviceId = deviceId;

/*        String data = sendGETAllData(this.deviceId);
        String deviceStr = getDeviceId(this.deviceId);*/

            ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesStaticService.findAllLatest(toUUID(deviceId));
            List<TsKvEntry> ls = tskventry.get();
            Gson gson = new Gson();
            String data = gson.toJson(ls);

            //Gson gson = new Gson();
            /*Device device = JSON.parseObject(deviceStr, Device.class);*/
            Device device = deviceStaticService.findDeviceById(toUUID(this.deviceId));

            JsonObject jsonObject =  encodeJson(device,data);
            sendMessage(jsonObject.toString(),this.session);

            if(map.containsKey(deviceId)){
                map.get(deviceId).add(session);
            }else{
                Set<Session> s = new HashSet<>();
                s.add(this.session);
                map.put(deviceId,s);
            }
        }

//        getSubscribeDevices().add(deviceId);
//        System.out.println(getSubscribeDevices());
        //群发消息
        /*for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }


    /**
     * 群发自定义消息
     * */
    /*
    public static void sendInfo(String message) throws IOException {
        log.info(message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }
*/

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public String sendGETAllData(String deviceId) throws IOException {

 /*       String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
*/
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder()
                .url("http://39.104.189.84:30080/api/v1/deviceaccess/data/alllatestdata/"+deviceId);

/*        String token = HttpUtil.getAccessToken();
        builder.header("Authorization","Bearer "+token);*/

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }

    public String getDeviceId(String deviceId) throws IOException {

/*        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e);
        }*/

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder()
                .url("http://39.104.189.84:30080/api/v1/deviceaccess/device/"+deviceId);

/*        String token = HttpUtil.getAccessToken();
        builder.header("Authorization","Bearer "+token);*/

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }

    public static JsonObject encodeJson (Device device, String data){
        JsonObject obj =  new JsonObject();
        obj.addProperty("deviceId",device.getId().toString());
        obj.addProperty("tenantId",device.getTenantId());
        obj.addProperty("deviceType",device.getDeviceType());

        JsonArray array = new JsonArray();

        JsonArray dataArray = new JsonParser().parse(data).getAsJsonArray();
        for(JsonElement elementData : dataArray ){
            JsonObject objData = elementData.getAsJsonObject();

            JsonObject jsonObj = new JsonObject();

            jsonObj.addProperty("ts",objData.get("ts").getAsLong());
            JsonObject kv = objData.get("kv").getAsJsonObject();
            jsonObj.addProperty("key",kv.get("key").getAsString());
            jsonObj.addProperty("value",kv.get("value").getAsString());
            /*switch(objData.get("dataType").getAsString()){
                case "string":
                    jsonObj.addProperty("value",objData.get("value").getAsString());
                    break;
                case "boolean":
                    jsonObj.addProperty("value",objData.get("value").getAsBoolean());
                    break;
                case "long":
                    jsonObj.addProperty("value",objData.get("value").getAsLong());
                    break;
                case "double":
                    jsonObj.addProperty("value",objData.get("value").getAsDouble());
                    break;*/
           //}
            array.add(jsonObj);
        }
        obj.add("data",array);
        return obj;
    }

    UUID toUUID(String id) {
        if(id==null) {
            return null;
        }else {
            return UUID.fromString(id);
        }
    }
}
