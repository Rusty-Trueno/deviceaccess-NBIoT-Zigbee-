package cn.edu.bupt.message;

import cn.edu.bupt.common.SessionId;
import lombok.Data;

/**
 * Created by Administrator on 2018/4/16.
 */
@Data
public class SessionTimeoutMsg {
    private static final long serialVersionUID = 1L;

    private final SessionId sessionId;
}
