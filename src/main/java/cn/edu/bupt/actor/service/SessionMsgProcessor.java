package cn.edu.bupt.actor.service;

import cn.edu.bupt.message.SessionAwareMsg;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface SessionMsgProcessor {
    void process(SessionAwareMsg sessionAwareMsg);
}
