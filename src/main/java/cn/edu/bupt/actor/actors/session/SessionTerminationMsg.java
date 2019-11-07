package cn.edu.bupt.actor.actors.session;

import cn.edu.bupt.common.SessionId;
import lombok.Getter;

/**
 * Created by Administrator on 2018/4/17.
 */
public class SessionTerminationMsg {
    @Getter
    private final SessionId sessionId;

    public SessionTerminationMsg(SessionId sessionId){
        this.sessionId = sessionId;
    }
}
