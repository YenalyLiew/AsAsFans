package com.fairhr.module_support.web;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fairhr.module_support.base.BaseApplication;
import com.fairhr.module_support.utils.ContextUtil;


/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksframework.webview
 * Description:金仕达webview
 */
public class KSWebView extends WebView implements IWebViewInit {
    private String mCurrentUrl;

    public String getCurrentUrl() {
        return mCurrentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        mCurrentUrl = currentUrl;
    }

    public KSWebView(Context context) {
        super(context);
    }

    public KSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KSWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void initWebView(WebView webView) {
        String databasePath = BaseApplication.sApplication.getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setJavaScriptEnabled(true);
        // H5内容过大时, 缩放至屏幕大小
        webView.getSettings().setLoadWithOverviewMode(true);
        // H5 viewport标签支持
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings()
                .setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCachePath(databasePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setGeolocationDatabasePath(databasePath);
        webView.getSettings().setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        webView.getSettings().setTextZoom(100);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDatabasePath(databasePath);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // 用于H5获取载入的是android平台还是ios平台，并且区分是浏览器载入还是框架的webview载入
        String userAgent = webView.getSettings().getUserAgentString();
        String version = "/version_" + Build.VERSION.SDK_INT;
        webView.getSettings().setUserAgentString(
                userAgent + "Android" + version);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.setVerticalScrollBarEnabled(false);
        // 解决在android5.0以上webview cookie丢失问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        BaseJsInterface jsInterface = new BaseJsInterface(BaseApplication.sApplication, webView);
        webView.addJavascriptInterface(jsInterface, BaseJsInterface.JS_INTERFACE_NAME);
        webView.getSettings().setUserAgentString(userAgent + "Android");
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
    }

    /**
     * 重新加载url
     */
    public void reloadUrl() {
        reload();
    }
}
