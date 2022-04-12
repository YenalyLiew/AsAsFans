package com.fairhr.module_support.web;

import android.webkit.WebView;

/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksframework.webview.inter
 * Description:WebView拦截器
 */
public interface IKSWebviewInterceptor {

    boolean onWebviewInterceptor(WebView view, String url);
}
