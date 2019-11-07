package cn.edu.bupt.common;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface SessionId extends Serializable {
    String toUidStr();
}
