package com.fairhr.module_support.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksframework.webview
 * Description:
 */
public class KSWebChromeClient extends WebChromeClient {
    /**
     * 通知应用程序当前网页加载的进度
     *
     * @param view
     * @param progress
     */
    @Override
    public void onProgressChanged(WebView view, int progress) {
        super.onProgressChanged(view, progress);

    }

    /**
     * 获取网页title标题
     *
     * @param view
     * @param titleStr
     */
    @Override
    public void onReceivedTitle(WebView view, String titleStr) {
        super.onReceivedTitle(view, titleStr);

    }

}
