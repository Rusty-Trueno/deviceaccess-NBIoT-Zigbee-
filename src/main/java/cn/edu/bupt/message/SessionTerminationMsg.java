package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;

/**
 * Created by Administrator on 2018/4/16.
 */
public class SessionTerminationMsg {
    private final SessionId sessionId;

    public SessionTerminationMsg(SessionId sessionId){
        this.sessionId = sessionId;
    }

    public SessionId getSessionId(){return sessionId;}
}
