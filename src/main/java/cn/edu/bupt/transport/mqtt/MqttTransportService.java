package cn.edu.bupt.transport.mqtt;

import cn.edu.bupt.actor.service.SessionMsgProcessor;
import cn.edu.bupt.service.DeviceAuthService;
import cn.edu.bupt.service.DeviceService;
import cn.edu.bupt.transport.TransportAdaptor;
import cn.edu.bupt.transport.mqtt.adaptor.JsonMqttAdaptor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Administrator on 2018/4/13.
 */
@Service("mqttTransportService")
public class MqttTransportService {

    @Autowired(required = false)
    private ApplicationContext appContext;

    @Autowired(required = false)
    private SessionMsgProcessor processor;

    @Autowired(required = false)
    private DeviceService deviceService;

    @Autowired(required = false)
    private DeviceAuthService deviceAuthService;

    private TransportAdaptor adaptor;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Value("${mqtt.bind_address}")
    private String host;
    @Value("${mqtt.bind_port}")
    private Integer port;
//    @Value("${mqtt.adaptor}")
//    private String adaptorName;

    @Value("${mqtt.netty.leak_detector_level}")
    private String leakDetectorLevel;
    @Value("${mqtt.netty.boss_group_thread_count}")
    private Integer bossGroupThreadCount;
    @Value("${mqtt.netty.worker_group_thread_count}")
    private Integer workerGroupThreadCount;

    @PostConstruct
    public void init() throws Exception{
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(leakDetectorLevel.toUpperCase()));
       // this.adaptor = (TransportAdaptor) appContext.getBean(adaptorName);
        this.adaptor = new JsonMqttAdaptor();

        bossGroup = new NioEventLoopGroup(bossGroupThreadCount);
        workerGroup = new NioEventLoopGroup(workerGroupThreadCount);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MqttTransportServerInitializer(processor, deviceService, deviceAuthService,adaptor));

        serverChannel = b.bind(port).sync().channel();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        try {
            serverChannel.close().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
