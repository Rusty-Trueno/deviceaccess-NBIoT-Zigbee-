package cn.edu.bupt.transport.mqtt.adaptor;

import cn.edu.bupt.common.entry.*;
import cn.edu.bupt.message.AttributeUploadMsg;
import cn.edu.bupt.message.FromDeviceMsg;
import cn.edu.bupt.message.TelemetryUploadMsg;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/14.
 */
public class JsonConverter {

    public static final String CAN_T_PARSE_VALUE = "Can't parse value: ";

    public static FromDeviceMsg convertToTelemetry(JsonElement data, int msgId) {
        TelemetryUploadMsg msg = new TelemetryUploadMsg(msgId);
        long ts = System.currentTimeMillis();
        if(data.isJsonObject()){
            parseJsonObject(msg,ts,data);
        }else if(data.isJsonArray()){
            data.getAsJsonArray().forEach(e->{
                if( e.isJsonObject()){
                    parseJsonObject(msg,ts,e);
                }else{
                    throw new JsonSyntaxException(CAN_T_PARSE_VALUE + e);
                }
            });
        }else{
            throw new JsonSyntaxException(data.toString());
        }
        return msg;
    }

    public static FromDeviceMsg convertToAttribute(JsonElement data, int msgId) {
        AttributeUploadMsg msg = new AttributeUploadMsg(msgId);
        long ts = System.currentTimeMillis();
        if(data.isJsonObject()){
            for(Map.Entry<String,JsonElement> entry:data.getAsJsonObject().entrySet()){
                if(entry.getValue().isJsonPrimitive()){
                    KvEntry kv = parseAttribute(entry);
                    KvEntry e =  new BasicAttributeKvEntry(ts,kv);
                    msg.add(e);
                }else{
                    throw new JsonSyntaxException(CAN_T_PARSE_VALUE + entry);
                }
            }
        }else{
            throw new JsonSyntaxException(CAN_T_PARSE_VALUE + data);
        }
        return msg;
    }

    private static KvEntry parseAttribute(Map.Entry<String, JsonElement> entry) {
        KvEntry e = null ;
        if(entry.getValue().getAsJsonPrimitive().isNumber()){
            if(entry.getValue().getAsString().contains(".")){
                e = new DoubleEntry(entry.getKey(),entry.getValue().getAsDouble());
            }else{
                e = new LongEntry(entry.getKey(),entry.getValue().getAsLong());
            }
        }else if(entry.getValue().getAsJsonPrimitive().isString()){
            e = new StringEntry(entry.getKey(),entry.getValue().getAsString());
        }else if(entry.getValue().getAsJsonPrimitive().isBoolean()){
            e = new BooleanEntry(entry.getKey(),entry.getValue().getAsBoolean());
        }else{
             throw new JsonSyntaxException("输入的键值对不满足指定的规范");
        }
        return e;
    }

    private static void parseJsonObject(TelemetryUploadMsg msg, long ts, JsonElement data) {
        JsonObject jo = data.getAsJsonObject();
        if(jo.has("ts")&&jo.has("values")){
            parseWithTs(msg,jo.get("ts").getAsLong(),jo.get("values").getAsJsonObject());
        }else{
            parseWithTs(msg,ts,jo);
        }
    }

    private static void parseWithTs(TelemetryUploadMsg msg, long ts, JsonObject values) {
        List<KvEntry> result = new ArrayList<>();
        for(Map.Entry<String,JsonElement> entry:values.entrySet()){
            if(entry.getValue().isJsonPrimitive()){
                JsonPrimitive v = entry.getValue().getAsJsonPrimitive();
                if(v.isBoolean()){
                    result.add(new BooleanEntry(entry.getKey(),v.getAsBoolean()));
                }else if(v.isString()){
                    result.add(new StringEntry(entry.getKey(),v.getAsString()));
                }else if(v.isNumber()){
                    if(v.getAsString().contains(".")){
                        result.add(new DoubleEntry(entry.getKey(),v.getAsDouble()));
                    }else{
                        result.add(new LongEntry(entry.getKey(),v.getAsLong()));
                    }
                }else{
                    throw new JsonSyntaxException("输入的键值对不满足指定的规范");
                }
            }else{
                throw new JsonSyntaxException("输入的键值对不满足指定的规范");
            }
        }
        for(KvEntry e:result){
            msg.add(ts,e);
        }
    }

}
