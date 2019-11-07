package cn.edu.bupt.transport;

import cn.edu.bupt.common.SessionContext;
import cn.edu.bupt.message.AdaptorToSessionActorMsg;
import cn.edu.bupt.message.SessionActorToAdaptorMsg;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface TransportAdaptor<C extends SessionContext, T, V>  {
    AdaptorToSessionActorMsg convertToActorMsg(C ctx, String msgType, T inbound) throws AdaptorException;

    Optional<V> convertToAdaptorMsg(C ctx, SessionActorToAdaptorMsg msg) throws AdaptorException;
}
