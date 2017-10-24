package com.example.eden.dict.utils;

/**
 * Created by Eden on 2017/10/24.
 */
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zyq on 2017/10/18.
 */

public class NetWorkUtils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String bowlingJson(String Username, String Password,String Phonenumber) {
        String ss="{\"username\":\""+Username+"\",\"password\":\""+Password+"\",\"phonenumber\":\""+Phonenumber+"\"}";
        return ss;
    }
}
