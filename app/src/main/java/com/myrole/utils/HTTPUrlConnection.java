package com.myrole.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vikesh.kumar on 5/9/2016.
 */
public class HTTPUrlConnection {
    private static HTTPUrlConnection connection;
    String response = "";
    URL url;
    Context context;

    public static HTTPUrlConnection getInstance() {
        return new HTTPUrlConnection();
    }

    public String load(Context context,String path, HashMap<String, String> params) {
        try {
            this.context=context;
            url = new URL(path);
            Log.v("URL:", path);
            Log.e("PARAMS:", String.valueOf(params));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //Log.d("Output",br.toString());
                while ((line = br.readLine()) != null) {
                    response += line;
                    Log.d("output lines", line);
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        String userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
        String androidId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.DEVICE_TOKEN_ID);
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        if (userId!=null || !userId.equals("") ){
            result.append("&");

            result.append(URLEncoder.encode("loggedin_token", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(userId, "UTF-8"));
        }

        if (androidId!=null || !androidId.equals("") ){
            result.append("&");

            result.append(URLEncoder.encode("android_id", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(androidId, "UTF-8"));
        }

        return result.toString();
    }
}


