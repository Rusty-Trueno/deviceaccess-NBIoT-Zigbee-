package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;

/**
 * Created by Administrator on 2018/4/13.
 */
public class SessionCloseMsg implements SessionCtrlMsg{

    private final SessionId sessionId;
    private final boolean revoked;
    private final boolean timeout;

    public SessionCloseMsg(SessionId sessionId, boolean unauthorized, boolean timeout) {
        super();
        this.sessionId = sessionId;
        this.revoked = unauthorized;
        this.timeout = timeout;
    }
    public static SessionCloseMsg onDisconnected(SessionId sessionId){
        return new SessionCloseMsg(sessionId,false,false);
    }

    @Override
    public SessionId getSessionId() {
        return sessionId;
    }
}
