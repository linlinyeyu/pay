package com.ybliu.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.StringEntity;

/**
 * Created by linlinyeyu on 2018/1/17.
 */
public interface HttpBase {
    /**
     *
     * @param url
     * @param jsonObject
     * @return
     */
    JSONObject httpPost(String url,JSONObject jsonObject);

    /**
     *
     * @param url
     * @param jsonObject
     * @param noNeedResponse 不需要返回结果
     * @return
     */
    JSONObject httpPost(String url,JSONObject jsonObject,boolean noNeedResponse);

    /**
     *
     * @param url
     * @return
     */
    JSONObject httpGet(String url);

    /**
     *
     * @param url
     * @param jsonObject
     * @return
     */
    JSONObject httpGet(String url,JSONObject jsonObject);

    JSONObject httpPostForm(String url,JSONObject jsonObject);
}
