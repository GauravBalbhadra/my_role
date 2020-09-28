package com.myrole.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;


@SuppressWarnings("deprecation")
public class RestClient {
    public static org.apache.http.client.HttpClient CLIENT = getThreadSafeClient();
    public static int responseCode;
    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;

    private String url, data = "";
    private MultipartEntity entity;
    private String message;
    private String response;

    public RestClient(String url) {
        this.url = url;
        // this.url = this.url + "/sid:"+ Config.SESSION_ID;

        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
    }

    static public String postHTPPRequest(String URL, String paramenter) {

        Log.v("parameters>>>>>>>", paramenter);
        HttpPost httppost = new HttpPost(URL);
        httppost.setHeader("Content-Type", "application/json");

        try {
            if (paramenter != null) {
                StringEntity tmp = null;

                tmp = new StringEntity(paramenter, "UTF-8");
                httppost.setEntity(tmp);
            }
            org.apache.http.HttpResponse httpResponse = null;

            httpResponse = CLIENT.execute(httppost);

            int code = httpResponse.getStatusLine().getStatusCode();
            responseCode = code;
            Log.v("response code", "" + code);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                InputStream input = null;

                input = entity.getContent();
                String res = convertStreamToString(input);
                input.close();
                Log.v("Response Data", res);
                return res;
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static DefaultHttpClient getThreadSafeClient() {

        DefaultHttpClient client = new DefaultHttpClient();


        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();

        HttpConnectionParams.setConnectionTimeout(params, 1000 * 180);
        HttpConnectionParams.setSoTimeout(params, 1000 * 180);


        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);

        return client;
    }

    public void setStringEntity(String data) {
        this.data = data;
    }

    public void setMultipartEntity(MultipartEntity entity) {
        this.entity = entity;
    }

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void AddParam(String name, String value) {
        Log.e("Param:->>" + name, value);
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value) {
        Log.e("Header:->>" + name, value);
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception {
        switch (method) {
            case GET: {
                // add parameters
                String combinedParams = "";
                if (!params.isEmpty()) {
                    combinedParams += "/";
                    for (NameValuePair p : params) {
                        Log.i("paramstring", p.getValue().replace(" ", "%20"));
                        if (combinedParams.length() > 1) {
                            combinedParams += "/"
                                    + p.getValue().replace(" ", "%20");
                        } else {
                            combinedParams += p.getValue().replace(" ", "%20");
                        }
                    }
                }

                HttpGet request = new HttpGet(url + combinedParams);
                Log.e("))))", "" + url);
                Log.e("((((", "" + url + combinedParams);

                // add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request, url);
                break;
            }
            case POST: {
                HttpPost request = new HttpPost(url);
                //add json data
                if (!this.data.isEmpty()) {
                    request.setEntity(new StringEntity(data, "UTF-8"));
                    Log.e("JSON Added >>>>>>>", data);
                }

                if (this.entity != null) {
                    request.setEntity(this.entity);
                    Log.e("Entity >>>>>>>", entity.toString());
                }

                // add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }

                if (!params.isEmpty()) {
                    ArrayList<String> parameters = new ArrayList<String>();
                    for (NameValuePair p : params) {
                        parameters.add(p.getValue());
                    }
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    private void executeRequest(HttpUriRequest request, String url) {

        Log.e("***URL***", "" + url);

        org.apache.http.HttpResponse httpResponse;

        try {
            httpResponse = CLIENT.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                Log.i("Response ", response);
                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e) {
            CLIENT.getConnectionManager().shutdown();
            CLIENT = getThreadSafeClient();
            e.printStackTrace();
        } catch (IOException e) {
            CLIENT.getConnectionManager().shutdown();
            CLIENT = getThreadSafeClient();
            e.printStackTrace();
        } catch (Exception e) {
            CLIENT.getConnectionManager().shutdown();
            CLIENT = getThreadSafeClient();
            e.printStackTrace();
        }
    }

    public enum RequestMethod {
        GET, POST
    }
}
