package cn.edu.bupt.message;

import cn.edu.bupt.common.entry.KvEntry;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/14.
 */
public class AttributeUploadMsg implements FromDeviceMsg {

    private final Set<KvEntry> data;
    private final Integer requestId;

    public AttributeUploadMsg(){this(0);}

    public AttributeUploadMsg(int requestId){
        this.requestId = requestId;
        this.data = new HashSet<>();
    }

    public void add(KvEntry e){
        data.add(e);
    }

    @Override
    public String getMsgType(){
        return MsgType.POST_ATTRIBUTE_REQUEST;
    }

    public Set<KvEntry> getData(){
        return data;
    }
}
