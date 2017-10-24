package com.example.eden.dict.utils;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eden on 2017/3/9.
 */

public class NetOperationHelper {
    public final static String sURL1="http://dict-co.iciba.com/api/dictionary.php?w=";
    //金山apiKey
    public final static String sURL2="&key=CA13C917035F2D10B5BC65AD57C7C685";

    public static InputStream getInputStreamByUrl(String strurl){
        InputStream tempInput=null;
        URL url=null;
        HttpURLConnection connection=null;

        try{
            url=new URL(strurl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(10000);
            Log.d("Http","Connection");
            tempInput=connection.getInputStream();

        }
        catch (Exception e){
            Log.d("Http","not get inputStream");
            e.printStackTrace();
        }
        return tempInput;
    }
}
