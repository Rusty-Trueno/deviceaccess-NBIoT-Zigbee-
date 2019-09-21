package cn.edu.bupt.websocket;

import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.service.BaseTimeseriesService;
import cn.edu.bupt.service.DeviceService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/api/v1/deviceaccess/websocket/device")
@Component
public class NewWebSocketServer{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private BaseTimeseriesService baseTimeseriesService;

    private static DeviceService deviceStaticService ;

    private static BaseTimeseriesService baseTimeseriesStaticService;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static Integer onlineCount = 0;

    public static Map<String,Set<Session>> gatewaySessionMap = new ConcurrentHashMap<>();  // 以上都是单例成员

    public String gatewayId = null;

    private static Logger logger = LoggerFactory.getLogger(NewWebSocketServer.class);

    private Session session;

    @PostConstruct
    public void init(){
        deviceStaticService = deviceService;
        baseTimeseriesStaticService = baseTimeseriesService;
    }

    /**
     * 建立连接
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        addOnlineCount();           //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        gatewaySessionMap.get(gatewayId).remove(this.session);
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /***
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session)throws Exception{
        logger.info("客户端消息: " + message);

        if(message.equals("@heart")){
            //sendMessage("@heart",this.session);
        }else{
            // 获取 customerId
            JsonObject jsonObj = (JsonObject)new JsonParser().parse(message);
            String gatewayId= jsonObj.get("gatewayId").getAsString();
            this.gatewayId = gatewayId;
//        for(int i = 0; i < customerIdArray.size(); i++){
//            customerIdList.add(customerIdArray.get(i).getAsInt());
//        }
            // 查数据库设备信息
            TextPageLink pageLink = new TextPageLink(1000, null, null, null);
            List<Device> devices = deviceStaticService.findDeviceByParentDeviceId(gatewayId, pageLink);
            // 遍历查询设备最新数据
            for(Device device:devices){
                if(!device.getDeviceType().equals("temperature") && !device.getDeviceType().equals("Gateway") && !device.getDeviceType().equals("PM2.5") && !device.getDeviceType().equals("lightSensor")){
                    ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesStaticService.findAllLatest(device.getId());
                    List<TsKvEntry> ls = tskventry.get();
                    Gson gson = new Gson();
                    String data = gson.toJson(ls);
                    JsonObject jsonObject =  WebSocketServer.encodeJson(device,data);
                    sendMessage(jsonObject.toString(),this.session);
                }
            }

            // 保存 Session
            if(gatewaySessionMap.containsKey(gatewayId)){
                gatewaySessionMap.get(gatewayId).add(session);
            }else{
                Set<Session> s = new HashSet<>();
                s.add(this.session);
                gatewaySessionMap.put(gatewayId,s);
            }
        }
    }

    /**
     * 查找数据库中设备的最新信息
     * @param
     * @return
     */
    /*public List<TextPageData<Device>> getAllDeviceInfo(List<Integer> customerIdList) throws Exception{
        List<TextPageData<Device>> deviceInfoList = new ArrayList<>();
        for (Integer customerId : customerIdList) {
            TextPageData<Device> deviceInfo = getInstance().deviceService.findDevicesByTenantIdAndCustomerId(2, customerId, new TextPageLink(1000));
            deviceInfoList.add(deviceInfo);
        }
        return deviceInfoList;
    }*/

    public void sendMessage(java.lang.String message, Session session) throws IOException {
        System.out.println(message);
        session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        NewWebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        NewWebSocketServer.onlineCount--;
    }

}
