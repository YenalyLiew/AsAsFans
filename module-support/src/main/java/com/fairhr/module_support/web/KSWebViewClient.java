package com.fairhr.module_support.web;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.fairhr.module_support.ThreadUtils;
import com.fairhr.module_support.utils.ContextUtil;
import com.fairhr.module_support.utils.DeviceInfo;
import com.fairhr.module_support.utils.LogUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksframework.webview
 * Description:
 */
public class KSWebViewClient extends WebViewClient {
    private final static long LOAD_TIME_OUT = 30 * 1000;
    private Context mContext;
    private Set<IKSWebviewInterceptor> mIKSWebviewInterceptors = new HashSet<>();
    private boolean mShouldClearHistory;
    private Disposable mTimeOutDisposable;

    public boolean isShouldClearHistory() {
        return mShouldClearHistory;
    }

    public void setShouldClearHistory(boolean shouldClearHistory) {
        mShouldClearHistory = shouldClearHistory;
    }

    public KSWebViewClient(Context context) {
        mContext = context;
        mIKSWebviewInterceptors.addAll(WebViewManager.getInstance().getDefaultIKSWebviewInterceptors());
    }

    public KSWebViewClient(Context context, IKSWebviewInterceptor... iksWebviewInterceptors) {
        mContext = context;
        if (iksWebviewInterceptors != null) {
            mIKSWebviewInterceptors.addAll(Arrays.asList(iksWebviewInterceptors));
        }
        mIKSWebviewInterceptors.addAll(WebViewManager.getInstance().getDefaultIKSWebviewInterceptors());
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        boolean result;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            String url = request.getUrl().toString();
            if (!url.startsWith("https:") && !url.startsWith("http:") ){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                result = true;
            }else {
                result = shouldOverrideUrlLoading(view, url);
            }

        } else {
            result = super.shouldOverrideUrlLoading(view, request);
        }
        return result;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.d(url);
        if (TextUtils.isEmpty(url)) {
            return true;
        } else {
            setCurrentUrl(url);
            if (mIKSWebviewInterceptors.size() > 0) {
                for (IKSWebviewInterceptor iksWebviewInterceptor : mIKSWebviewInterceptors) {
                    if (iksWebviewInterceptor.onWebviewInterceptor(view, url)) {
                        return true;
                    } else {
                        view.loadUrl(url);
                        return true;
                    }
                }
            } else {
                view.loadUrl(url);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        // 解决H5去请求https地址报错的问题
        handler.proceed();
    }

    // 从 API 11 引入，API 21 废弃
    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
        return super.shouldInterceptRequest(view, url);
    }

    // 从 API 21 开始引入
    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
        WebResourceResponse result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String url = request.getUrl().toString();
            result = shouldInterceptRequest(view, url);
        } else {
            // 理论上不会进入这里
            result = super.shouldInterceptRequest(view, request);
        }
        return result;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String js = String.format(BaseJsInterface.INJECT_STATUS_BAR_HEIGHT, DeviceInfo.px2dp(DeviceInfo.getStatusBarHeight(mContext)) + "");
                view.loadUrl(js);
            }
        });

        startCountdown();
    }

    /**
     * H5加载超时
     */
    private void startCountdown() {
        cancleTimeout();
        mTimeOutDisposable = Observable.timer(LOAD_TIME_OUT, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        onErrorShowDefaultView();
                    }
                });
    }

    @Override
    public void onPageFinished(final WebView view, final String url) {
        super.onPageFinished(view, url);
        cancleTimeout();
    }

    @Override
    public void onReceivedError(WebView view,
                                int errorCode,
                                String description,
                                String failingUrl) {
        // API23之前会调用
        // "不可用"的情况有可以包括有：
        // 没有网络连接/连接超时/找不到页面
        // 而下面的情况则不会被报告：
        // 网页内引用其他资源加载错误，比如图片、css不可用/js执行错误
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!onErrorShowDefaultView()) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        } else {
            // API23以及API23之后也会调用, 但是我们什么也不做 (^_^)v
        }
    }

    @TargetApi(23)
    @Override
    public void onReceivedError(WebView view,
                                WebResourceRequest request,
                                WebResourceError error) {
        // API23以及API23之后会调用
        if (request.isForMainFrame()) {
            if (!onErrorShowDefaultView()) {
                super.onReceivedError(view, request, error);
            }
        }
    }

    @Override
    public void onReceivedHttpError(WebView view,
                                    WebResourceRequest request,
                                    WebResourceResponse errorResponse) {
        // PS:由于我们不需要获取到Http状态码所以没有重写或者扩展任何这里的默认实现
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (mShouldClearHistory) {
            mShouldClearHistory = false;
            view.clearHistory();
        }
    }

    /**
     * 设置当前显示的url
     *
     * @param currentUrl
     */
    public void setCurrentUrl(String currentUrl) {

    }

    /**
     * 显示缺省视图
     *
     * @return 是否显示了缺省视图
     */
    public boolean onErrorShowDefaultView() {
        cancleTimeout();
        return false;
    }

    /**
     * 取消定时任务
     */
    public void cancleTimeout() {
        if (mTimeOutDisposable != null && !mTimeOutDisposable.isDisposed()) {
            mTimeOutDisposable.dispose();
        }
    }

}
