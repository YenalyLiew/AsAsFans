package com.fairhr.module_support.utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Gson工具
 * Created by Ning on 2016-12-3.
 */
public final class GsonUtils {

    /**
     * Gson
     */
    private static Gson sGson;

    static {
        sGson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                .create();
    }

    private GsonUtils() {
    }

    /**
     * 转换为Json字符串
     *
     * @param obj 对象
     * @return Json字符串
     */
    @Nullable
    public static String toJson(@Nullable Object obj) {
        return sGson.toJson(obj);
    }

    /**
     * 解析Json字符串
     *
     * @param json Json字符串
     * @param type 类型
     * @param <T>  类型
     * @return 对象
     */
    @Nullable
    public static <T> T fromJson(@Nullable String json, Type type) {
        return sGson.fromJson(json, type);
    }

    /**
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T fromJson(@Nullable String json, Class<T> clazz) {
        return sGson.fromJson(json, clazz);
    }


    /**
     * 解析Json字符串
     *
     * @param json                 Json字符串
     * @param rawType              原始类型 List.class
     * @param genericArgumentTypes 泛型参数类型数组 new Class[]{mClazz}
     * @param <T>                  原始类型
     * @return 对象
     */
    public static <T> T fromJson(@Nullable String json,
                                 @NonNull final Type rawType,
                                 @NonNull final Type[] genericArgumentTypes) throws JsonSyntaxException {
        Type resultType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return genericArgumentTypes;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        return sGson.fromJson(json, resultType);
    }

}
