/*
package cn.edu.bupt.security;

import cn.edu.bupt.security.model.Token;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

*/
/**
 * Created by Administrator on 2017/12/23.
 * 在启动的时候不能使用
 *//*

@Component
public class HttpUtil {

    private static Token token = new Token();

    @Value("${account.login_url}")
    private void getLogin(String loginUrl) {
        System.out.println(loginUrl);
        tokenurl = loginUrl ;
    }

    @Value("${account.check_url}")
    private void getCheck(String checkUrl) {
        System.out.println(checkUrl);
        checkurl = checkUrl ;
    }


    @Value("${account.internal_client_id}")
    private void getInternalClientId(String internal_client_id) {
        Internal_client_id = internal_client_id ;
    }

    @Value("${account.internal_client_secret}")
    private void getInternalClientSecret(String internal_client_secret) {
        Internal_client_secret = internal_client_secret ;
    }

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static String tokenurl;
    private static String checkurl;
    private static String Internal_client_id;
    private static String Internal_client_secret;

    public static String getAccessToken(){

        if(token.getExpires_at() < System.currentTimeMillis() / 1000) {
            Request.Builder builder = new Request.Builder()
                    .url(tokenurl + "?grant_type=client_credentials")
                    .post(RequestBody.create(null, ""));

            byte[] textByte = (Internal_client_id + ":" + Internal_client_secret).getBytes();
            String auth = encoder.encodeToString(textByte);
            builder.header("Authorization", "Basic " + auth);

            Request request = builder.build();
            try {
                // 第一次获取token
                Response response = execute(request);
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                    token.setAccess_token(obj.get("access_token").getAsString());
                    token.setExpires_at(obj.get("expires_in").getAsLong() + System.currentTimeMillis() / 1000);
                    return obj.get("access_token").getAsString();
                } else {
                    throw new Exception("the first fail!");
                }
            } catch (Exception e) {
                // 第二次获取token
                try {
                    Response response = execute(request);
                    String res = response.body().string();
                    JsonObject obj = new JsonParser().parse(res).getAsJsonObject();
                    token.setAccess_token(obj.get("access_token").getAsString());
                    token.setExpires_at(obj.get("expires_in").getAsLong() + System.currentTimeMillis() / 1000);
                    return obj.get("access_token").getAsString();
                } catch (Exception e1) {
                    return "ERROR!";
                }
            }
        }else{
            return token.getAccess_token();
        }
    }


    public static String sendPost(String url, Map<String,String> headers, JsonObject requestBody) throws Exception{
        String str ;
        if(requestBody==null){
            str = "";
        }else{
            str = requestBody.toString();
        }
        RequestBody body = RequestBody.create(JSON, str);
        Request.Builder buider = new Request.Builder()
                .url(url)
                .post(body);

        String tocken = getAccessToken();
        buider.header("Authorization","Bearer "+tocken);

        if(headers!=null){
            for(Map.Entry<String,String> entry:headers.entrySet()){
                buider.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = buider.build();

        return sendRequireToThingsboard(request);
    }

    public static String sendPut(String url, Map<String,String> headers, JsonObject requestBody) throws Exception{
        String str ;
        if(requestBody==null){
            str = "";
        }else{
            str = requestBody.toString();
        }
        RequestBody body = RequestBody.create(JSON, str);
        Request.Builder buider = new Request.Builder()
                .url(url)
                .put(body);

        String tocken = getAccessToken();
        buider.header("Authorization","Bearer "+tocken);

        if(headers!=null){
            for(Map.Entry<String,String> entry:headers.entrySet()){
                buider.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = buider.build();

        return sendRequireToThingsboard(request);
    }

    public static String sendDelete(String url) throws Exception{
        Request.Builder buider = new Request.Builder()
                .url(url)
                .delete() ;

        String tocken = getAccessToken();
        buider.header("Authorization","Bearer "+tocken);
        Request request = buider.build();

        return sendRequireToThingsboard(request);
    }

    public static String sendGet(String url, Map<String,String> headers) throws Exception{

        Request.Builder buider = new Request.Builder()
                .url(url)
                .get() ;

        String tocken = getAccessToken();
        buider.header("Authorization","Bearer "+tocken);

        if(headers!=null){
            for(Map.Entry<String,String> entry:headers.entrySet()){
                buider.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = buider.build();

        return sendRequireToThingsboard(request);
    }

    private static String sendRequireToThingsboard(Request request) throws Exception{
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    */
/**
     * 同步方法
     * @param request
     * @return
     * @throws IOException
     *//*

    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute() ;
    }

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
}
*/
