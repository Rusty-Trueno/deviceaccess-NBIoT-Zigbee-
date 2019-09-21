package cn.edu.bupt.actor.service;

import cn.edu.bupt.message.FromServerMsg;

/**
 * Created by Administrator on 2018/4/24.
 */
public interface RpcMsgProcessor{
    public void process(FromServerMsg msg);
}
