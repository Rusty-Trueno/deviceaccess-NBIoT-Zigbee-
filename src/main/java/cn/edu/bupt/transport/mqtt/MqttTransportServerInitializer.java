package cn.edu.bupt.transport.mqtt;

import cn.edu.bupt.actor.service.SessionMsgProcessor;
import cn.edu.bupt.service.DeviceAuthService;
import cn.edu.bupt.service.DeviceService;
import cn.edu.bupt.transport.TransportAdaptor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;


/**
 * Created by Administrator on 2018/4/13.
 */
public class MqttTransportServerInitializer extends ChannelInitializer<SocketChannel>{

    private static final int MAX_PAYLOAD_SIZE = 64 * 1024 * 1024;

    private final SessionMsgProcessor processor;
    private final DeviceService deviceService;
    private final DeviceAuthService authService;
    private final TransportAdaptor adaptor;

    public MqttTransportServerInitializer(SessionMsgProcessor processor, DeviceService deviceService, DeviceAuthService authService, TransportAdaptor adaptor) {
        this.processor = processor;
        this.deviceService = deviceService;
        this.authService = authService;
        this.adaptor = adaptor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("decoder", new MqttDecoder(MAX_PAYLOAD_SIZE));
        pipeline.addLast("encoder", MqttEncoder.INSTANCE);

        MqttTransportHandler handler = new MqttTransportHandler(processor, deviceService, authService,adaptor);
        pipeline.addLast(handler);
        socketChannel.closeFuture().addListener(handler);
    }
}
