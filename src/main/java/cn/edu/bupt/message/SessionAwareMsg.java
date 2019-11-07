package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface SessionAwareMsg {
    SessionId getSessionId();
}
