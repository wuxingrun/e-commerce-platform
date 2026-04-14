package com.example.utils;

import cn.hutool.json.JSONObject;
import okhttp3.*;
import java.util.concurrent.TimeUnit;
import java.io.*;


public class SentimentUtils {
    public static final String API_KEY = "7rqTymMy7xmkANHLaM1UiHtR";
    public static final String SECRET_KEY = "WbXLAFd0G2mR6kNQrJBQzlus59osUGlk";

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    // 缓存access_token，避免每次请求都获取
    private static String cachedAccessToken = null;
    private static long tokenExpireTime = 0;

    /**
     * 对文本进行情感分析
     * @param text 待分析的文本
     * @return 情感标签：positive（正面）2、neutral（中性）1、negative（负面）0
     */
    public static int analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 1; // neutral
        }

        try {
            String accessToken = getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                return -1; // API调用失败返回-1
            }

            MediaType mediaType = MediaType.parse("application/json");
            // 构建请求体
            String jsonBody = String.format("{\"text\":\"%s\"}", text.replace("\"", "\\\""));
            RequestBody body = RequestBody.create(mediaType, jsonBody);

            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?charset=UTF-8&access_token=" + accessToken)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();

            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() == null) {
                return -1; // API调用失败返回-1
            }
            String responseBody = response.body().string();

            // 解析返回结果
            JSONObject jsonObject = new JSONObject(responseBody);
            if (jsonObject.containsKey("items") && !jsonObject.getJSONArray("items").isEmpty()) {
                JSONObject item = jsonObject.getJSONArray("items").getJSONObject(0);
                int sentiment = item.getInt("sentiment", 1); // 0:负面, 1:中性, 2:正面
                switch (sentiment) {
                    case 0:
                        return 0; // negative
                    case 1:
                        return 1; // neutral
                    case 2:
                        return 2; // positive
                    default:
                        return 1; // neutral
                }
            }
            return -1; // API调用失败返回-1
        } catch (Exception e) {
            // 静默处理异常，不打印堆栈，避免日志过多
            return -1; // API调用失败返回-1
        }
    }

    public static void main(String []args) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?charset=UTF-8&access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }


    /**
     * 获取Access Token，使用缓存机制避免频繁请求
     *
     * @return 鉴权签名（Access Token）
     */
    private static synchronized String getAccessToken() {
        // 检查缓存是否有效（有效期通常为30天，这里简化处理，缓存10天）
        long now = System.currentTimeMillis();
        if (cachedAccessToken != null && tokenExpireTime > now) {
            return cachedAccessToken;
        }

        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                    + "&client_secret=" + SECRET_KEY);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() == null) {
                return cachedAccessToken; // 返回缓存的token
            }

            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            String token = jsonObject.getStr("access_token");

            if (token != null && !token.isEmpty()) {
                cachedAccessToken = token;
                // 设置缓存有效期为10天
                tokenExpireTime = now + (10 * 24 * 60 * 60 * 1000L);
                return token;
            }
        } catch (Exception e) {
            // 静默处理异常，返回缓存的token
        }

        return cachedAccessToken;
    }

}