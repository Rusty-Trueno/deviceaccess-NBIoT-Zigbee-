package cn.edu.bupt.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2018/5/23.
 */
public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static void main(String[] args)throws Exception{
        JsonObject obj = getDeviceServiceDes("bupt","switch","1","control switch");
        System.out.println(obj);
    }
    public static JsonObject getDeviceServiceDes(String manufacture, String deviceType, String model,String serviceName) throws IOException{
//        String url = "http://172.24.32.167:8000/api/v1/ability/"+manufacture+"/"+deviceType+"/"+model;
//        String url = "http://servicemanagement:8000/api/v1/servicemanagement/ability/"+manufacture+"/"+deviceType+"/"+model;
        String url = "http://smart.gantch.cn:30080/api/v1/servicemanagement/ability/"+manufacture+"/"+deviceType+"/"+model;
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get() ;
        Request request =  builder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            String str = response.body().string();
            JsonArray obj = new JsonParser().parse(str).getAsJsonArray();
            for(JsonElement ele:obj){
               String str1 =  ele.getAsJsonObject().get("abilityDes").getAsString();
               JsonObject o = new  JsonParser().parse(str1).getAsJsonObject();

               if (serviceName.equals(o.get("serviceName").getAsString())){
                    return o;
               }
            }
            return null;
        }else{
            return null;
        }
    }
}
