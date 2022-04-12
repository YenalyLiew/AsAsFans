package com.fairhr.module_support;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.bean.LoginEvent;
import com.fairhr.module_support.bean.LoginState;
import com.fairhr.module_support.router.RouteNavigationPath;
import com.fairhr.module_support.utils.UrlUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * Description: 阿里路由管理器
 */
public class ArouterManager {
    private final static String URL_SCHEME = "kingstar://";
    private final static String WEB_URL_HEARD = "kingstar://web/";
    private static ArouterManager sArouterManager;
    //需要登录的路由地址
    private Set<String> mNeedLoginSet = new HashSet<>();
    private Disposable mLoginDisposable;

    private ArouterManager() {
        initData();
    }

    public static ArouterManager getInstance() {
        if (sArouterManager == null) {
            synchronized (ArouterManager.class) {
                if (sArouterManager == null) {
                    sArouterManager = new ArouterManager();
                    return sArouterManager;
                }
            }
        }
        return sArouterManager;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //添加需要登录的路由地址

    }

    /**
     * url路由
     *
     * @param urlPath
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void urlArouter(Context context, String urlPath) {
        try {
            //判断是否url路由
            if (judgeNativeArouter(urlPath)) {
                if (judgeWebUrl(urlPath)) {
                    webPageArouter(context, urlPath);
                } else {
                    navitePageArouter(context, urlPath);
                }
            } else {
//                OpenPageUtils.openWebview(urlPath, false, "");
            }
        } catch (Exception e) {

        }
    }

    /**
     * 原生界面路由
     *
     * @param context
     * @param arouterUrl
     */
    private void navitePageArouter(Context context, String arouterUrl) {
        arouterUrl = arouterUrl.replaceFirst(URL_SCHEME, "/");
        if (arouterUrl.contains("?")) {
            String[] split = arouterUrl.split("\\?");
            if (split.length == 2) {
                String path = split[0];
                String paramsString = split[1];
                Bundle bundle = new Bundle();
                Map<String, String> map = UrlUtils.paramsToMap(paramsString);
                if (map.size() > 0) {
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        bundle.putString(entry.getKey(), entry.getValue());
                    }
                }
                ARouter.getInstance().build(path).with(bundle).navigation(context);
            }
        } else {
            ARouter.getInstance().build(arouterUrl).navigation(context);
        }
    }

    /**
     * H5界面路由
     *
     * @param context
     * @param arouterUrl
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void webPageArouter(Context context, String arouterUrl) {
        String httpUrl = "";
        String params = "";
        if (arouterUrl.contains("?")) {
            String[] split = arouterUrl.split("\\?");
            httpUrl = decodingH5Url(split[0].replace(WEB_URL_HEARD, ""));
            if (split.length > 1)
                params = split[1];
        } else {
            httpUrl = decodingH5Url(arouterUrl.replace(WEB_URL_HEARD, ""));
        }
        Map<String, String> map = UrlUtils.paramsToMap(params);
        boolean showTitle = map.containsKey("showTitle") && "1".equals(map.get("showTitle"));
        String title = map.containsKey("title") ? map.get("title") : "";
        if (map.containsKey("needLogin") && "1".equals(map.get("needLogin"))) {
            if (UserInfoManager.getInstance().isLogin()) {
//                OpenPageUtils.openWebview(httpUrl, showTitle, title);
            } else {
//                ARouter.getInstance().build(RouteNavigationPath.ModuleMy.MY_PAGE_LOGIN_REGISTER).navigation();
                String finalHttpUrl = httpUrl;
//                mLoginDisposable = RxBus.getDefault().toObserverable(LoginEvent.class)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<LoginEvent>() {
//                            @Override
//                            public void accept(LoginEvent loginEvent) throws Exception {
//                                if (loginEvent.getLoginState() == LoginState.LOGIN_IN) {
////                                    OpenPageUtils.openWebview(finalHttpUrl, showTitle, title);
//                                }
//                                if (mLoginDisposable != null)
//                                    mLoginDisposable.dispose();
//                                mLoginDisposable = null;
//                            }
//                        });
            }

        } else {
//            OpenPageUtils.openWebview(httpUrl, showTitle, title);
        }
    }

    /**
     * 格式化原生url
     *
     * @param nativeUrl
     * @return
     */
    public static String generateNativeUrl(String nativeUrl, String productId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", productId);
        String params = UrlUtils.getParams(paramsMap);
        if (nativeUrl.contains("?")) {
            return nativeUrl + "&" + params;
        } else {
            return nativeUrl + "?" + params;
        }
    }

    /**
     * 格式化h5 url 到h5路由url
     *
     * @param h5UrlPath
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatH5ToH5Arouter(String h5UrlPath) {
        return WEB_URL_HEARD + codingH5Url(h5UrlPath);
    }

    /**
     * 格式化h5 url 到h5路由url
     *
     * @param h5UrlPath
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatH5ToH5Arouter(String h5UrlPath, boolean isNeedLogin) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("needLogin", isNeedLogin ? "1" : "0");
        String params = UrlUtils.getParams(paramsMap);
        return WEB_URL_HEARD + codingH5Url(h5UrlPath) + "?" + params;
    }

    /**
     * 格式化h5 url 到h5路由url
     *
     * @param h5UrlPath
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatH5ToH5Arouter(String h5UrlPath, boolean isNeedLogin, boolean showTitle, String title) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("needLogin", isNeedLogin ? "1" : "0");
        paramsMap.put("showTitle", showTitle ? "1" : "0");
        paramsMap.put("title", title);
        String params = UrlUtils.getParams(paramsMap);
        return WEB_URL_HEARD + codingH5Url(h5UrlPath) + "?" + params;
    }

    /**
     * 编码H5 url
     *
     * @param url
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String codingH5Url(String url) {
        Base64.Encoder urlEncoder = Base64.getUrlEncoder();
        return new String(urlEncoder.encode(url.getBytes()));
    }

    /**
     * 解码H5url
     *
     * @param codingUrl
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decodingH5Url(String codingUrl) {
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        return new String(urlDecoder.decode(codingUrl.getBytes()));
    }

    /**
     * 判断是否为原生路由
     *
     * @param url
     * @return
     */
    private boolean judgeNativeArouter(String url) {
        return url.startsWith(URL_SCHEME);
    }


    /**
     * 判断是否web路由
     *
     * @param url
     * @return
     */
    private boolean judgeWebUrl(String url) {
        return url.startsWith(WEB_URL_HEARD);
    }

    /**
     * 是否拦截拦截
     *
     * @param aroutPath
     * @return
     */
    public synchronized boolean loginInterceptor(String aroutPath) {
        return mNeedLoginSet.contains(aroutPath);
    }
}
