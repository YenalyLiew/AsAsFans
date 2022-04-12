package com.fairhr.module_support.network;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.fairhr.module_support.constants.LiveEventKeys;
import com.fairhr.module_support.utils.LogUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 添加公共参数
 * 公共参数拦截器,
 * form表单转body
 *
 */

public class Form2BodyInterceptor implements Interceptor {
    private static final String TAG = "_http";
    private final MediaType mediaTypeJson = MediaType.parse("application/json;charset=UTF-8");
    private final Map<String, String> queryParamsMap = new HashMap<>();
    private final Map<String, String> paramsMap = new HashMap<>();
    private final Map<String, String> headerParamsMap = new HashMap<>();
    private final List<String> headerLinesList = new ArrayList<>();

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        //请求
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        // process header params inject
        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (headerParamsMap.size() > 0) {
            for (Map.Entry<String, String> entry : headerParamsMap.entrySet()) {
                //避免覆盖通过@Headers添加的header
                if (TextUtils.isEmpty(headerBuilder.get(entry.getKey())) && entry.getValue() != null) {
                    headerBuilder.set(entry.getKey(), entry.getValue());
                }
            }
            requestBuilder.headers(headerBuilder.build());
        }
        // process header params end

        if (headerLinesList.size() > 0) {
            for (String line : headerLinesList) {
                headerBuilder.add(line);
            }
            requestBuilder.headers(headerBuilder.build());
        }
        // process header params end

        //添加动态公共参数，这里会替换固定参数
//        String id_token = DataStoreUtil.INSTANCE.readDataSync(DataStoreKeys.ModuleSupport.ID_TOKEN, "");
//        if (TextUtils.isEmpty(id_token)) {
//            requestBuilder.removeHeader("x-id-token").addHeader("x-id-token", NetConfig.INSTANCE.getEscapeToken());
//        } else {
//            requestBuilder.removeHeader("x-id-token").addHeader("x-id-token", id_token);
//        }
//        String trade_token = DataStoreUtil.INSTANCE.readDataSync(DataStoreKeys.ModuleTrade.TRADE_TOKEN, "");
//        requestBuilder.removeHeader("x-ctp-auth");
//        if (!TextUtils.isEmpty(trade_token)) {
//            requestBuilder.removeHeader("x-ctp-auth").addHeader("x-ctp-auth", trade_token);
//        }

        // process queryParams inject whatever it's GET or POST
        if (queryParamsMap.size() > 0) {
            injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, queryParamsMap);
        }

        // process post body inject
        if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                request = buildFormBody((FormBody) request.body(), requestBuilder);
            } else if (request.body() instanceof MultipartBody) {
                request = buildMultipartBody((MultipartBody) request.body(), requestBuilder);
            } else {
                request = buildCommonParams(requestBuilder);
            }
        } else {
            request = requestBuilder.build();
        }

        //响应
        Response response = chain.proceed(request);
        if (response.code() != 200) {
            if (401 == response.code()) {
                LiveEventBus.get(LiveEventKeys.TOKEN_UNAVAILABLE).postOrderly(null);
            }
        }
        return response;
    }

    private Request buildCommonParams(Request.Builder requestBuilder) {
        String bodyJson = requestBodyToString(requestBuilder.build().body());
        if (!TextUtils.isEmpty(bodyJson)) {
            try {
                JSONObject jsonObject = new JSONObject(bodyJson);
                if (paramsMap.size() > 0) {
                    for (Map.Entry<String, String> stringStringEntry : paramsMap.entrySet()) {
                        jsonObject.put(stringStringEntry.getKey(), stringStringEntry.getValue());
                    }
                }
                RequestBody requestBody = RequestBody.create(jsonObject.toString(), mediaTypeJson);
                requestBuilder.post(requestBody);
                return requestBuilder.build();
            } catch (JSONException e) {
                LogUtil.e(TAG, "error buildCommonParams: " + bodyJson);
                e.printStackTrace();
            }
        }
        return requestBuilder.build();
    }

    private Request buildFormBody(FormBody oldFormBody, Request.Builder requestBuilder) {
        Map<String, Object> signParams = new HashMap<>();
        if (paramsMap.size() > 0) {
            for (Map.Entry<String, String> stringStringEntry : paramsMap.entrySet()) {
                signParams.put(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        int paramSize = oldFormBody.size();
        if (paramSize > 0) {
            for (int i = 0; i < paramSize; i++) {
                signParams.put(oldFormBody.name(i), oldFormBody.value(i));
            }
        }

        RequestBody requestBody = RequestBody.create(getJsonString(signParams), mediaTypeJson);
        requestBuilder.post(requestBody);
        return requestBuilder.build();
    }

    private Request buildMultipartBody(MultipartBody oldMultipartBody, Request.Builder requestBuilder) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> stringStringEntry : paramsMap.entrySet()) {
            multipartBuilder.addFormDataPart(stringStringEntry.getKey(), stringStringEntry.getValue());
        }

        List<MultipartBody.Part> oldParts = oldMultipartBody.parts();
        if (oldParts.size() > 0) {
            for (MultipartBody.Part part : oldParts) {
                multipartBuilder.addPart(part);
            }
        }

//        //提取Headers参数
//        for (int i = 0; i < oldMultipartBody.size(); i++) {
//
//            MediaType mediaType = oldMultipartBody.contentType();
//            if (mediaType == null) {
//                String normalParamKey;
//                String normalParamValue;
//
//                MultipartBody.Part part = oldMultipartBody.part(i);
//
//                normalParamValue = getParamContent(part.body());
//                Headers headers = part.headers();
//                if (!TextUtils.isEmpty(normalParamValue) && headers != null) {
//                    for (String name : headers.names()) {
//                        String headerContent = headers.get(name);
//                        if (!TextUtils.isEmpty(headerContent)) {
//                            String[] normalParamKeyContainer = headerContent.split("name=\"");
//                            if (normalParamKeyContainer.length == 2) {
//                                normalParamKey = normalParamKeyContainer[1].split("\"")[0];
//                                break;
//                            }
//                        }
//                    }
//                }
//
//            }
//        }

        requestBuilder.post(multipartBuilder.build());
        return requestBuilder.build();
    }

//    private boolean canInjectIntoBody(Request request) {
//        if (request == null) {
//            return false;
//        }
//        if (!TextUtils.equals(request.method(), "POST")) {
//            return false;
//        }
//        RequestBody body = request.body();
//        if (body == null) {
//            return false;
//        }
//        MediaType mediaType = body.contentType();
//        if (mediaType == null) {
//            return false;
//        }
//        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
//            return false;
//        }
//        return true;
//    }

    private static String requestBodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    // func to inject params into url
    private Request.Builder injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            for (Map.Entry<String, String> stringStringEntry : paramsMap.entrySet()) {
                httpUrlBuilder.addQueryParameter(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder;
        }

        return null;
    }

    private String getJsonString(Map<String, Object> paramsMap) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static class Builder {

        Form2BodyInterceptor interceptor;

        public Builder() {
            interceptor = new Form2BodyInterceptor();
        }

        public Builder addParam(String key, String value) {
            interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addHeaderParam(String key, String value) {
            interceptor.headerParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            interceptor.headerParamsMap.putAll(headerParamsMap);
            return this;
        }

        public Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            interceptor.headerLinesList.add(headerLine);
            return this;
        }

        public Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine : headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.headerLinesList.add(headerLine);
            }
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public Map<String, String> getParamsMap() {
            return interceptor.paramsMap;
        }

        public Form2BodyInterceptor build() {
            return interceptor;
        }

    }
}
