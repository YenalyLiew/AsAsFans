package com.fairhr.module_support.webview.biz;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.fairhr.module_support.ModuleConfig;
import com.fairhr.module_support.R;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.router.RouteUtils;
import com.fairhr.module_support.utils.AppUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.utils.StatusBarUtil;
import com.fairhr.module_support.utils.SystemUtil;
import com.fairhr.module_support.webview.agent.AgentWeb;
import com.fairhr.module_support.webview.agent.AgentWebConfig;
import com.fairhr.module_support.webview.agent.WebViewClient;
import com.fairhr.module_support.webview.video.VideoEnabledWebChromeClient;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;


/**
 * class description
 *
 * @author ysw
 * @version 1.0.0
 * @date 2021-01-18
 */
public abstract class WebBaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected TextView titleTv;
    protected ImageView titleMore;
    protected AgentWeb mAgentWeb;
    RelativeLayout titleBar;
    LinearLayout webLl;
    private RelativeLayout videoLayout;
//    private ImageView closeIv;
//    private View lineView;

    private PopupMenu mPopupMenu;
    protected boolean showWebTitle;

//    private final WebChromeClient mWebChromeClient = new WebChromeClient() {
//        @Override
//        public void onReceivedTitle(WebView view, String title) {
//            super.onReceivedTitle(view, title);
//            LogUtil.i(AgentWeb.TAG, "title = " + title + ", url = " + view.getUrl());
//            if (showWebTitle && !"Webpage not available".equalsIgnoreCase(title) && !URLUtil.isValidUrl(title)) {
//                titleTv.setText(title);
//            }
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_webbase_layout);
        titleTv = findViewById(R.id.toolbar_title);
        titleMore = findViewById(R.id.iv_more);
        titleBar = findViewById(R.id.titleBar);
        webLl = findViewById(R.id.webll);
//        closeIv = findViewById(R.id.iv_finish);
//        lineView = findViewById(R.id.view_line);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_finish).setOnClickListener(this);
        videoLayout = findViewById(R.id.videoLayout);
        titleMore.setOnClickListener(this);
        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(webLl, videoLayout);
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.Callback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {

            }

            @Override
            public void onReceivedTitle(String title) {
                if (showWebTitle && !"Webpage not available".equalsIgnoreCase(title) && !URLUtil.isValidUrl(title)) {
                    int height = titleTv.getHeight();
                    titleTv.setText(title);
                }
            }
        });
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webLl, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebChromeClient(webChromeClient)
                .setWebViewClient(mWebViewClient)
                .setAgentWebUIController(new MyUIController(this))
                .setMainFrameErrorView(R.layout.support_web_error_page, R.id.retryLoad)
                .setMainFrameNetErrorView(R.layout.support_web_nonet_page, R.id.retryLoad)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .get();
        setTitleBarHeight(false);
        StatusBarUtil.setStatusBarImmersion(this, true, false, true, null);

        addJsInterfaces();
    }

    private void setTitleBarHeight(boolean zero) {
        int height = 0;
        if (!zero) {
            height = getResources().getDimensionPixelOffset(R.dimen.titlebar_height);
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//        marginLayoutParams.topMargin = StatusBarUtil.getStatusBarHeight(WebBaseActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginLayoutParams);
        titleBar.setLayoutParams(layoutParams);
    }

    private void addJsInterfaces() {
        try {
            Set<Map.Entry<String, String>> set = ModuleConfig.getJS_INTERFACES().entrySet();
            for (Map.Entry<String, String> entry : set) {
                Class<?> cls = Class.forName(entry.getValue());
                Constructor<?> constructor = cls.getConstructor(Activity.class, AgentWeb.class);
                mAgentWeb.getJsInterfaceHolder().addJavaObject(entry.getKey(), constructor.newInstance(WebBaseActivity.this, mAgentWeb));
            }
            mAgentWeb.getJsInterfaceHolder().addJavaObject("web", new WebJsInterface(WebBaseActivity.this, mAgentWeb));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(AgentWeb.TAG, "addJsInterfaces Fail: " + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_finish) {
            finish();
        } else if (v.getId() == R.id.iv_back) {
            if (!mAgentWeb.back()) {
                finish();
            }
        }else if (v.getId() == R.id.iv_more) {
            //客服

        }
    }

    private void shareDialog() {
//        mAgentWeb.getJsAccessEntrace().quickCallJs("getShareInfo", value -> {
//            LogUtil.d("_share", value);
//            if ("null".equals(value) || value == null) {
//                ShareManager.INSTANCE.defaultShare(
//                        ShareType.WEB,
//                        WebBaseActivity.this,
//                        TextUtils.isEmpty(mAgentWeb.getWebCreator().getWebView().getTitle()) ? getString(R.string.app_name) : mAgentWeb.getWebCreator().getWebView().getTitle(),
//                        getString(R.string.share_slogan),
//                        null,
//                        null,
//                        mAgentWeb.getWebCreator().getWebView().getUrl(),
//                        new OnShareListener() {
//                            @Override
//                            public void onSuccess(SharePlatType shareType) {
//                                LogUtil.d("_share", "onSuccess " + shareType.name());
//                            }
//
//                            @Override
//                            public void onFail(String errMsg) {
//                                LogUtil.d("_share", "onFail " + errMsg);
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                LogUtil.d("_share", "onCancel ");
//                            }
//                        });
//            } else {
//                ShareH5Bean data = JsonUtil.Companion.getInstance().parseJsonToBean(value, ShareH5Bean.class);
//                if (data != null) {
//                    ShareManager.INSTANCE.defaultShare(
//                            ShareType.WEB,
//                            WebBaseActivity.this,
//                            data.getTitle(),
//                            TextUtils.isEmpty(data.getDesc()) ? getString(R.string.share_slogan) : data.getDesc(),
//                            null,
//                            data.getImageUrl(),
//                            data.getUrl(),
//                            new OnShareListener() {
//                                @Override
//                                public void onSuccess(SharePlatType shareType) {
//                                    LogUtil.d("_share", "onSuccess " + shareType.name());
//                                }
//
//                                @Override
//                                public void onFail(String errMsg) {
//                                    LogUtil.d("_share", "onFail " + errMsg);
//                                }
//
//                                @Override
//                                public void onCancel() {
//                                    LogUtil.d("_share", "onCancel ");
//                                }
//                            });
//                }
//            }
//        });
    }

    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this, view);
            mPopupMenu.inflate(R.menu.support_web_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }

    private final PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_refresh) {
                if (mAgentWeb != null) {
                    mAgentWeb.getUrlLoader().reload(); // 刷新
                }
                return true;
            } else if (itemId == R.id.menu_copy) {
                if (mAgentWeb != null) {
                    SystemUtil.copyTextToClip(WebBaseActivity.this, mAgentWeb.getWebCreator().getWebView().getUrl(), null);
                }
                return true;
            } else if (itemId == R.id.menu_browser) {
                AppUtils.openBrowser(WebBaseActivity.this, mAgentWeb.getWebCreator().getWebView().getUrl());
                return true;
            } else if (itemId == R.id.menu_clean) {
                toCleanWebCache();
                return true;
            } else if (itemId == R.id.menu_share) {
                //AppUtil.INSTANCE.shareTxt(getApplication(), mAgentWeb.getWebCreator().getWebView().getUrl());
                shareDialog();
                return true;
            } else if (itemId == R.id.menu_setting) {
                //
                return true;
            }
            return false;

        }
    };

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {
        if (this.mAgentWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
            AgentWebConfig.clearDiskCache(this);
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
//            if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
//                closeIv.setVisibility(View.VISIBLE);
//                lineView.setVisibility(View.VISIBLE);
//            } else {
//                closeIv.setVisibility(View.GONE); val name = bean.name
//            et_search.setText(name)
//                lineView.setVisibility(View.GONE);
//            }
            super.onPageFinished(view, url);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
        mAgentWeb = null;
    }

    class WebJsInterface extends BaseJsInterface {
        private final Handler deliver = new Handler(Looper.getMainLooper());

        public WebJsInterface(Activity context, AgentWeb agent) {
            super(context, agent);
        }

        /**
         * 显示或隐藏titleBar
         *
         * @param params
         */
        @JavascriptInterface
        public void showTopBar(String params) {
            CallbackMethod method = new CallbackMethod(params);
            deliver.post(() -> {
                String show = method.getParams().get("show");
                setTitleBarHeight(!Boolean.parseBoolean(show));
            });
        }

        /**
         * 显示或隐藏分享菜单
         *
         * @param params
         */
        @JavascriptInterface
        public void showShare(String params) {
            CallbackMethod method = new CallbackMethod(params);
            deliver.post(() -> {
                String show = method.getParams().get("show");
                titleMore.setVisibility(Boolean.parseBoolean(show) ? View.VISIBLE : View.INVISIBLE);
            });
        }


    }
}
