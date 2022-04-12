package com.fairhr.module_support.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fairhr.module_support.ModuleConfig;
import com.fairhr.module_support.R;
import com.fairhr.module_support.base.FrameFragment;
import com.fairhr.module_support.utils.DeviceUtil;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.webview.agent.AgentWeb;
import com.fairhr.module_support.webview.agent.WebChromeClient;
import com.fairhr.module_support.webview.agent.WebViewClient;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

/**
 * Describe:
 */
public class WebBaseFragment extends FrameFragment {
    public AgentWeb mAgentWeb;
    private final WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }
    };
    protected FrameLayout mInflate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mInflate == null)
            mInflate = (FrameLayout) inflater.inflate(R.layout.support_fragment_base_web, container, false);
        return mInflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mInflate, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.support_web_error_page, R.id.retryLoad)
                .setMainFrameNetErrorView(R.layout.support_web_nonet_page, R.id.retryLoad)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .get();
        addJsInterfaces();
    }

    @Override
    public void initBundleData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initServiceData() {

    }


    private void addJsInterfaces() {
        try {
            Set<Map.Entry<String, String>> set = ModuleConfig.getJS_INTERFACES().entrySet();
            for (Map.Entry<String, String> entry : set) {
                Class<?> cls = Class.forName(entry.getValue());
                Constructor<?> constructor = cls.getConstructor(Activity.class, AgentWeb.class);
                mAgentWeb.getJsInterfaceHolder().addJavaObject(entry.getKey(), constructor.newInstance(mAttachActivity, mAgentWeb));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(AgentWeb.TAG, "addJsInterfaces Fail: " + e.toString());
        }
    }


    private final WebViewClient mWebViewClient = new WebViewClient() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    public void setWebViewHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mInflate.getLayoutParams();
        layoutParams.height = DeviceUtil.dp2px(mAttachActivity.getApplicationContext(), height);
        mInflate.setLayoutParams(layoutParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
