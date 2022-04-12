package com.fairhr.module_support.network;



import android.util.Log;

import com.fairhr.module_support.UserInfoManager;
import com.fairhr.module_support.bean.UserInfo;
import com.fairhr.module_support.tools.inter.ErsDataObserver;
import com.fairhr.module_support.tools.inter.IUserInfoProvide;
import com.fairhr.module_support.utils.ContextUtil;
import com.fairhr.module_support.utils.HardwareUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**

 * Description:网络请求管理器
 */
public class ErsNetManager {
    private ConcurrentHashMap<String, Set<Cookie>> cookieStore = new ConcurrentHashMap<>();

    private static ErsNetManager mErsNetManager;

    private ErsNetManager() {
    }


    public static ErsNetManager getInstance() {
        if (mErsNetManager == null) {
            synchronized (ErsNetManager.class) {
                if (mErsNetManager == null)
                    mErsNetManager = new ErsNetManager();
            }
        }
        return mErsNetManager;
    }

    private CookieJar mCookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null) {
                Set<Cookie> cookieSet = cookieStore.get(url.host());
                if (cookieSet == null) {
                    cookieSet = new HashSet<>();
                    cookieSet.addAll(cookies);
                    cookieStore.put(url.host(), cookieSet);
                } else {
                    cookieSet.addAll(cookies);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            Set<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? new ArrayList<Cookie>(cookies) : new ArrayList<Cookie>();
        }
    };

    /**
     * 完成数据的请求
     *
     * @param url
     * @param ersDataObserver
     */
    public void getRequest(final String url, ErsDataObserver ersDataObserver) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                Call call = OkHttpManager.getInstance().get(url);
                Response execute = call.execute();
                if (execute.code() == 200) {
                    ResponseBody body = execute.body();
                    if (body != null) {
                        emitter.onNext(body.string());
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Throwable("请求返回数据为空" + execute.code()));
                    }

                } else {
                    emitter.onError(new Throwable("请求失败：" + execute.code()));
                }


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ersDataObserver);
    }


    /**
     * 完成数据的请求
     *
     * @param url
     * @param ersDataObserver
     */
    public void getRequestSpecial(final String url, ErsDataObserver ersDataObserver) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Map<String, String> headerParams = new HashMap<>();

                Call call = OkHttpManager.getInstance().get(url, headerParams);
                Response execute = call.execute();

                ResponseBody body = execute.body();
                if (body != null) {
                    emitter.onNext(body.string());
                    emitter.onComplete();
                }


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ersDataObserver);
    }


    /**
     * 完成数据的请求
     *
     * @param url
     * @param jsonMap
     * @param ersObserver
     */
    public void postJsonRequest(final String url, final Map<String, Object> jsonMap, ErsDataObserver ersObserver) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                JSONObject jsonObject = new JSONObject();
                Set<Map.Entry<String, Object>> entries = jsonMap.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                Call post = OkHttpManager.getInstance().postJson(url, jsonObject.toString(), mCookieJar);
                Response execute = post.execute();
                if (execute.code() == 200) {
                    ResponseBody body = execute.body();
                    if (body != null) {
                        emitter.onNext(body.string());
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Throwable("请求返回数据为空" + execute.code()));
                    }

                } else {
                    emitter.onError(new Throwable("请求失败：" + execute.code()));
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ersObserver);
    }


    /**
     * 上传文件
     *
     * @param filePath
     * @return
     */
    public Observable<String> upload(String url,Map<String, String> params,String filePath) {

        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Call call = OkHttpManager.getInstance().uploadFile(url, params, filePath);
                Response response = call.execute();
                ResponseBody body = response.body();
                if (response.code() == 200) {
                    if (body == null) {
                        emitter.onError(new Throwable("上传失败"));
                    } else {
                        String s = body.string();
                        JSONObject object = new JSONObject(s);
                        int error_no = object.getInt("error_no");
                        if (error_no == 0) {
                            emitter.onNext(object.getString("download_url"));
                        } else {
                            emitter.onError(new Throwable(object.getString("error_info")));
                        }
                    }
                } else {
                    emitter.onError(new Throwable(response.code() + "" + (body == null ? "" : body.toString())));
                }
            }
        });
        return stringObservable;
    }

    /**
     * 情况cookie
     */
    public void clearCookie() {
        cookieStore.clear();
    }
}
