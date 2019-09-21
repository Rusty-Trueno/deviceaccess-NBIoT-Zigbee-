package cn.edu.bupt.message;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface AdaptorToSessionActorMsg extends SessionAwareMsg {
    FromDeviceMsg getMsg();
}
