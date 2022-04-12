package com.fairhr.module_support.web;

import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import com.fairhr.module_support.utils.AppUtils;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksframework.webview
 * Description:WebView管理工具
 */
public class WebViewManager {
    private static WebViewManager sWebViewManager;
    //用于查找插件使用
    private Set<String> mPluginsPackageNames = new HashSet<>();
    //默认H5界面拦截器
    private Set<IKSWebviewInterceptor> mDefaultIKSWebviewInterceptors = new HashSet<>();
    //是否执行了启用远程调试
    private boolean mEnableRemoteDebuggingPerformed;
    //所有创建的webView
    private Set<KSWebView> mAllKSWebView = new HashSet<>();

    public Set<KSWebView> getAllKSWebView() {
        return mAllKSWebView;
    }

    public Set<String> getPluginsPackageNames() {
        return mPluginsPackageNames;
    }

    public Set<IKSWebviewInterceptor> getDefaultIKSWebviewInterceptors() {
        return mDefaultIKSWebviewInterceptors;
    }

    private WebViewManager() {
    }


    public static WebViewManager getInstance() {
        if (sWebViewManager == null) {
            synchronized (WebViewManager.class) {
                if (sWebViewManager == null) {
                    sWebViewManager = new WebViewManager();
                }
            }
        }
        return sWebViewManager;
    }

    /**
     * 添加插件
     *
     * @param pluginsPackageName
     */
    public void addPluginsPackageName(String pluginsPackageName) {
        mPluginsPackageNames.add(pluginsPackageName);
    }

    /**
     * 添加默认webview拦截器
     *
     * @param iksWebviewInterceptors
     */
    public void addDefaultKSWebviewInterceptor(IKSWebviewInterceptor... iksWebviewInterceptors) {
        if (iksWebviewInterceptors != null) {
            mDefaultIKSWebviewInterceptors.addAll(Arrays.asList(iksWebviewInterceptors));
        }
    }

    /**
     * 创建一个webview
     *
     * @param context
     */
    public KSWebView createWebView(Context context) {
        KSWebView ksWebView = new KSWebView(context);
        ksWebView.initWebView(ksWebView);
        mAllKSWebView.add(ksWebView);
        return ksWebView;
    }


    /**
     * 创建自定义设置的webview
     *
     * @param context
     * @param iWebViewInit
     * @return
     */
    public KSWebView createWebView(Context context, IWebViewInit iWebViewInit) {
        KSWebView ksWebView = new KSWebView(context);
        ksWebView.initWebView(ksWebView);
        iWebViewInit.initWebView(ksWebView);
        mAllKSWebView.add(ksWebView);
        return ksWebView;
    }

    /**
     * 创建自定义设置的webview
     *
     * @param context
     * @param iWebViewInit
     * @return
     */
    public KSWebView createDefaultWebView(Context context, IWebViewInit iWebViewInit) {
        KSWebView ksWebView = new KSWebView(context);
        ksWebView.initWebView(ksWebView);
        iWebViewInit.initWebView(ksWebView);
        mAllKSWebView.add(ksWebView);
        return ksWebView;
    }


    /**
     * 尝试启用远程调试网页视图
     */
    private void tryEnableRemoteDebugging() {
        if (!mEnableRemoteDebuggingPerformed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (AppUtils.isDebug) {
                    WebView.setWebContentsDebuggingEnabled(true);
                } else {
                    WebView.setWebContentsDebuggingEnabled(false);
                }
            }
            mEnableRemoteDebuggingPerformed = true;
        }
    }

    /**
     * 加载url
     *
     * @param ksWebView
     * @param url
     */
    public void loadUrl(KSWebView ksWebView, String url) {
        ksWebView.loadUrl(url);
    }

    /**
     * 加载HTML字符串
     *
     * @param ksWebView
     * @param data
     */
    public void loadData(KSWebView ksWebView, String data) {
        ksWebView.loadData(data, "text/html", "utf-8");
    }

    /**
     * 重新加载url
     *
     * @param ksWebView
     */
    public void reloadUrl(KSWebView ksWebView) {
        ksWebView.reloadUrl();
    }

    /**
     * 移除webview
     *
     * @param ksWebView
     */
    public void removeKSWebView(KSWebView ksWebView) {
        if (mAllKSWebView.contains(ksWebView))
            mAllKSWebView.remove(ksWebView);
    }
}
