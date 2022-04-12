package com.fairhr.module_support.network;

import androidx.annotation.NonNull;

public class NetConfig {

    private static String mServiceUrl;

    private static String mHomeServiceUrl;

    private static String mH5ServiceUrl;
    private static String mSecondServiceUrl;

    public static String getDefaultHomeServiceUrl(){
        return mHomeServiceUrl;
    }

    public static String getDefaultServiceUrl(){
        return mServiceUrl;
    }

    public static String getSecondDefaultServiceUrl(){
        return mSecondServiceUrl;
    }

    public static void setDefaultServiceUrl(String serviceUrl){
        mServiceUrl = serviceUrl;
    }

    public static void setSecondDefaultServiceUrl(String serviceUrl){
        mSecondServiceUrl = serviceUrl;
    }

    public static void setDefaultHomeServiceUrl(String serviceUrl){
        mHomeServiceUrl = serviceUrl;
    }

    public static String getH5ServiceUrl() {
        return mH5ServiceUrl;
    }

    public static void setH5ServiceUrl(String h5ServiceUrl) {
        mH5ServiceUrl = h5ServiceUrl;
    }
}
