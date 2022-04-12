package com.fairhr.module_support.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import com.fairhr.module_support.utils.AppUtils;
import com.google.gson.GsonBuilder;



/**
 //使用方法：
 //双重校验锁式-单例 封装NetApiService 方便直接快速调用简单的接口
 val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
 NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.SERVER_URL)
 }

 //注意：
 这里使用了Form2BodyInterceptor来添加公共参数和公共header.
 同时，会把表单转json放到body里，方便定义请求方法和参数.
 如果要使用正常的表单，请使用CommonNetworkApi
 */
public class SupportNetworkApi extends BaseNetworkApi{

    private static SupportNetworkApi mSupportNetworkApi;

    public static SupportNetworkApi getInstance() {
        if (null == mSupportNetworkApi) {
            synchronized (SupportNetworkApi.class) {
                if (null == mSupportNetworkApi) {
                    mSupportNetworkApi = new SupportNetworkApi();
                }
            }
        }
        return mSupportNetworkApi;
    }
//
    private SupportNetworkApi() {
    }
//
    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    @Override
    public OkHttpClient.Builder setHttpClientBuilder(OkHttpClient.Builder builder) {

        Form2BodyInterceptor paramsInterceptor = new Form2BodyInterceptor.Builder()
//                .addParamsMap(NetConfig.paramMap)
//                .addHeaderParamsMap(NetConfig.headerMap)
                .build();
        builder.addInterceptor(paramsInterceptor);
        if (AppUtils.isDebug) {
            builder.addInterceptor(new LogInterceptor());
        }
        //超时时间 连接、读、写
        builder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        return builder;
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    @Override
    public Retrofit.Builder setRetrofitBuilder(Retrofit.Builder builder) {
        builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        return builder;
    }
}
