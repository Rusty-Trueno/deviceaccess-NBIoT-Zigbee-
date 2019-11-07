package cn.edu.bupt.actor.service;

import cn.edu.bupt.message.BasicFromServerMsg;

public interface FromServerMsgProcessor {
    public void process(BasicFromServerMsg msg);
}
