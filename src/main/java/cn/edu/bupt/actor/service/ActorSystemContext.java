package cn.edu.bupt.actor.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import cn.edu.bupt.service.*;
import cn.edu.bupt.websocket.NewWebSocketServer;
import cn.edu.bupt.websocket.WebSocketServer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/4/16.
 */

@Component
public class ActorSystemContext {

    private static final String AKKA_CONF_FILE_NAME = "actor-system.conf";
    @Autowired
    @Getter
    private DeviceAuthService deviceAuthService;

    @Autowired
    @Getter private DeviceService deviceService;

    @Autowired
    @Getter private BaseTimeseriesService baseTimeseriesService;

    @Autowired
    @Getter private BaseAttributesService baseAttributesService;

    @Autowired
    @Getter private BaseEventService baseEventService;

    @Autowired
    @Getter private WebSocketServer webSocketServer;

    @Autowired
    @Getter private NewWebSocketServer newWebSocketServer;

    @Autowired
    @Getter private ModelService modelService;

//    @Autowired
//    @Getter private TenantService tenantService;

//    @Autowired
//    @Getter private TimeseriesService tsService;
//
//    @Autowired
//    @Getter private AttributesService attributesService;

    @Getter @Setter
    private ActorSystem actorSystem;

    @Getter @Setter private ActorRef appActor;
    @Getter @Setter private ActorRef sessionManagerActor;

    @Getter private final Config config;

    public ActorSystemContext(){
        config = ConfigFactory.parseResources(AKKA_CONF_FILE_NAME).withFallback(ConfigFactory.load());
    }

    public ActorRef getAppActor() {
        return appActor;
    }
}
