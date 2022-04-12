package com.fairhr.module_support.network;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public abstract class BaseNetworkApi{

    private OkHttpClient okHttpClient;

    public <T> T getApi(Class<T> serviceClass, String baseUrl){

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient());
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass);
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    abstract OkHttpClient.Builder setHttpClientBuilder(OkHttpClient.Builder builder);

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，Protocol
     */
    abstract Retrofit.Builder setRetrofitBuilder(Retrofit.Builder builder);

    /**
     * 配置http
     */
    private OkHttpClient getOkHttpClient(){
        if(okHttpClient == null){
            OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder());
            builder = setHttpClientBuilder(builder);
            return builder.build();
        }else{
            return okHttpClient;
        }
    }
}
