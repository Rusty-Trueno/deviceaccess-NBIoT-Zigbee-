package cn.edu.bupt.message;

import cn.edu.bupt.common.entry.KvEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/14.
 */
public class TelemetryUploadMsg implements  FromDeviceMsg {

    private final Map<Long, List<KvEntry>> data;
    private final Integer requestId;

    public TelemetryUploadMsg(){
        this(0);
    }
    public TelemetryUploadMsg(int id){
        this.requestId = id;
        data = new HashMap<>();
    }

    public void add(long ts,KvEntry e){
        if(data.containsKey(ts)){
            data.get(ts).add(e);
        }else{
            List<KvEntry> ls = new ArrayList<KvEntry>();
            ls.add(e);
            data.put(ts,ls);
        }
    }

    public Map<Long, List<KvEntry>> getData(){return data;}

    @Override
    public String getMsgType() {
        return MsgType.POST_TELEMETRY_REQUEST;
    }
}
