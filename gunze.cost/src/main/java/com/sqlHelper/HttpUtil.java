package com.sqlHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static void sendHttpRequest(final String function,
                                       final HttpCallbackListener listener,final String strParam) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String address="http://192.168.3.164:6666/api/transfer/";
                    URL url = new URL(address+function+"?"+strParam);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        //回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void sendOkHttpRequest(final String function,String json,okhttp3.Callback callback){
        String address="http://192.168.3.164:6666/api/transfer/"+function;
         MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client=new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request=new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public interface HttpCallbackListener{
        void onFinish(String response);
        void onError(Exception e);
    }

}
