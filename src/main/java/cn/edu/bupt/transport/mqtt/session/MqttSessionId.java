package cn.edu.bupt.transport.mqtt.session;

import cn.edu.bupt.common.SessionId;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2018/4/14.
 */
public class MqttSessionId implements SessionId {
    private static final AtomicLong idSeq = new AtomicLong();
    private final long id;

    public MqttSessionId(){
        this.id = idSeq.incrementAndGet();
    }
    public MqttSessionId(long id){
        this.id = id;
    }
    @Override
    public String toUidStr() {
        return "mqtt"+id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MqttSessionId that = (MqttSessionId) o;

        return id == that.id;

    }
}
