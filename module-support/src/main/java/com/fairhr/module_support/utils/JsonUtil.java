package com.fairhr.module_support.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @description：解析Json对象的工具类
 */
public class JsonUtil {

    private static JsonUtil sInstance;
    private Gson gson;

    public static JsonUtil getInstance() {
        if (null == sInstance) {
            synchronized (JsonUtil.class) {
                if (null == sInstance) {
                    sInstance = new JsonUtil();
                }
            }
        }
        return sInstance;
    }

    private JsonUtil(){
         gson = new Gson();
    }

    /**
     * 将[bean]转成json字符串
     */
    public <T extends Object> String toJsonString(T bean){
        return gson.toJson(bean);
    }

    /**
     * 将[jsonString]字符串转换成对应的数据类
     */
    public <T extends Object> T parseJsonToBean(String jsonString,Class<T> cls){
        return gson.fromJson(jsonString, cls);
    }

    /**
     * 将[jsonString]转成List
     */
    public <T> List<T> toList(String jsonString, Class<T> cls){
        List<T> list = new ArrayList<>();
        if(!TextUtils.isEmpty(jsonString)){
            JsonArray asJsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
            for (int i = 0; i < asJsonArray.size(); i++) {
                JsonElement elem = asJsonArray.get(i);
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

}
