package cn.edu.bupt.message;

import cn.edu.bupt.common.entry.KvEntry;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface TelemetryUploadRequest extends FromDeviceMsg {
    Map<Long,List<KvEntry>> getData();
}
