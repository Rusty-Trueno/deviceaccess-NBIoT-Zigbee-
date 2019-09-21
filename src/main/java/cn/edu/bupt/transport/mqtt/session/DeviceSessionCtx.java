package cn.edu.bupt.transport.mqtt.session;

import cn.edu.bupt.common.DeviceAwareSessionContext;
import cn.edu.bupt.common.SessionId;
import cn.edu.bupt.service.DeviceAuthService;
import cn.edu.bupt.transport.TransportAdaptor;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/4/13.
 */
public class DeviceSessionCtx extends DeviceAwareSessionContext {

    private TransportAdaptor transportAdaptor;
    private final MqttSessionId sessionId;
    private AtomicInteger msgIdSeq = new AtomicInteger(0);
    @Getter@Setter
    private  ChannelHandlerContext channelHandlerContext;

    public DeviceSessionCtx(DeviceAuthService authService, TransportAdaptor transportAdaptor){
        super(authService);
        this.transportAdaptor = transportAdaptor;
        sessionId = new MqttSessionId();
    }
    @Override
    public SessionId getSessionId() {
        return sessionId;
    }
}
