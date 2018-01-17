package com.ybliu.http.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ybliu.http.HttpBase;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linlinyeyu on 2018/1/17.
 */
public class HttpBaseImpl implements HttpBase {
    public JSONObject httpPost(String url, JSONObject jsonObject) {
        return httpPost(url,jsonObject,false);
    }

    public JSONObject httpPost(String url, JSONObject jsonObject, boolean noNeedResponse) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject param = null;
        if (null != jsonObject){
            StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            url = URLDecoder.decode(url,"UTF-8");
            if (response.getStatusLine().getStatusCode() == 200){
                String str = "";
                str = EntityUtils.toString(response.getEntity());
                if (noNeedResponse){
                    return null;
                }
                param = JSON.parseObject(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return param;
    }

    public JSONObject httpGet(String url) {
        return httpGet(url,null);
    }

    public JSONObject httpGet(String url,JSONObject param){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String response = "";
        CloseableHttpResponse httpResponse = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null){
                for (String key : param.keySet()){
                    builder.addParameter(key,param.get(key).toString());
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200){
                response = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(response);
    }

    public JSONObject httpPostForm(String url,JSONObject param){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String resultString = "";
        HttpPost httpPost = new HttpPost(url);
        if (param != null){
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            for (String key : param.keySet()){
                paramList.add(new BasicNameValuePair(key,param.get(key).toString()));
            }
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            httpResponse = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JSON.parseObject(resultString);
    }
}
