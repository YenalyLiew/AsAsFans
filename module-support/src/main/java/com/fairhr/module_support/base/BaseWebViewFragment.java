package com.fairhr.module_support.base;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fairhr.module_support.R;
import com.fairhr.module_support.bean.CommonCheckedBean;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.router.RouteNavigationPath;
import com.fairhr.module_support.router.RouteUtils;
import com.fairhr.module_support.rxpermissions.RxPermissions;
import com.fairhr.module_support.utils.AndroidFileUtils;
import com.fairhr.module_support.utils.CommonViewUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.utils.SystemAppUtil;
import com.fairhr.module_support.utils.SystemUtil;
import com.fairhr.module_support.web.KSWebChromeClient;
import com.fairhr.module_support.web.KSWebView;
import com.fairhr.module_support.web.KSWebViewClient;
import com.fairhr.module_support.web.WebViewManager;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksfoudation.base
 * Description:webview
 */
@Route(path = RouteNavigationPath.ModuleSupport.KS_WEB_VIEW)
public class BaseWebViewFragment extends BaseSelectPictureFragment implements View.OnClickListener {
    private ImageView mCommonTitleTivLeft;
    private TextView mCommonTitleTvTitle;
    private FrameLayout mBaseWebFlWebContainer;
    private View mRootView;
    private KSWebView mKsWebView;
    public String mLoadUrl;
    private View mBaseWebVStatus;
    private boolean mShowTitle = true;
    private String mTitleContent;
    private View mBaseWebRlTitle;
    //关闭webview时是否清除缓存
    private boolean mClearCookieCache = false;
    private View mBaseWebRl;
    private ValueCallback<Uri[]> mUploadMessage;
    private List<CommonCheckedBean<String>> mCommonCheckedBeans = new ArrayList<>();
    private boolean mIsHideStatusbar;
//    private View mKeFu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null)
            mRootView = inflater.inflate(R.layout.support_fragment_base_webview, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAttachActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initData();
        initView();
        initWebView();
        initStatusBarHeight();
        LogUtil.d(mLoadUrl);
        loadUrl(mLoadUrl);
    }

    @Override
    public void selectPictureFilePath(String filePath, String pictureFromType) {
        if (!TextUtils.isEmpty(filePath)) {
            Uri uriForFile;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uriForFile = SystemAppUtil.getUriForFile(mAttachActivity.getApplicationContext(), new File(filePath));
            } else {
                uriForFile = Uri.fromFile(new File(filePath));
            }
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(new Uri[]{uriForFile});
                mUploadMessage = null;
            }
        }
    }

    @Override
    public void cancleSelectPicture(String pictureFromType) {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(new Uri[]{});
            mUploadMessage = null;
        }
    }


    @Override
    public void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mLoadUrl = arguments.getString("url");
            mTitleContent = arguments.getString("title");
        }
        mCommonCheckedBeans.add(new CommonCheckedBean<String>("拍摄一张", "Camera"));
        mCommonCheckedBeans.add(new CommonCheckedBean<String>("从相册中选择一张", "Alum"));
    }

    private void initStatusBarHeight() {
        if (mIsHideStatusbar) {
            mBaseWebVStatus.setVisibility(View.GONE);
            mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        } else {
            mBaseWebVStatus.setVisibility(View.VISIBLE);
            mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示状态栏
        }
    }


    @Override
    public void initView() {
//        StatusBarCompat.setStatusBarLightMode(mAttachActivity);
        findViews();
        mCommonTitleTivLeft.setOnClickListener(this);
        CommonViewUtils.changeView2StatusBarHeight(mBaseWebVStatus);
        if (mShowTitle) {
            if (!TextUtils.isEmpty(mTitleContent))
                mCommonTitleTvTitle.setText(mTitleContent);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBaseWebFlWebContainer.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.base_web_rl_title);
        }
//        if (mAttachActivity instanceof BaseActivity) {
//            BaseActivity baseActivity = (BaseActivity) mAttachActivity;
//            baseActivity.setOnSystemKeyBoardListener(new OnSystemKeyBoardListener() {
//                @Override
//                public void onSoftKeyBoardChange(boolean isOpen, int keyHeight) {
//                    if (isOpen) {
//                        mBaseWebRl.setPadding(0, 0, 0, keyHeight);
//                    } else {
//                        mBaseWebRl.setPadding(0, 0, 0, 0);
//                    }
//                }
//            });
//        }
    }


    @Override
    public void findViews() {
        mBaseWebVStatus = mRootView.findViewById(R.id.base_web_v_status);
        mCommonTitleTivLeft = mRootView.findViewById(R.id.common_title_tiv_left);
        mCommonTitleTvTitle = mRootView.findViewById(R.id.common_title_tv_title);
        mBaseWebRlTitle = mRootView.findViewById(R.id.base_web_rl_title);
        mBaseWebFlWebContainer = mRootView.findViewById(R.id.base_web_fl_web_container);
//        mBaseWebRl = mRootView.findViewById(R.id.base_web_rl);
//        mKeFu = mRootView.findViewById(R.id.common_title_iv_kefu);
    }

    private void initWebView() {
        mKsWebView = WebViewManager.getInstance().createWebView(mAttachActivity.getApplicationContext());
        //设置下载监听
        mKsWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogUtil.d("");
                //检查是否有存储权限
                new RxPermissions(BaseWebViewFragment.this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    String fileName = null;
                                    if (contentDisposition.contains(";")) {
                                        String[] split = contentDisposition.split(";");
                                        if (split != null && split.length > 0) {
                                            for (String content : split) {
                                                if (content.contains("filename") && content.contains("=")) {
                                                    String[] split1 = content.split("=");
                                                    fileName = split1[1].replace("\"", "");
                                                }
                                            }
                                        }
                                    }
                                    if (TextUtils.isEmpty(fileName)) {
                                        fileName = UUID.randomUUID().toString();
                                    }
                                    String sdCardPublicDir = AndroidFileUtils.getSDAppFileDir(mAttachActivity.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS);
                                    File file = new File(sdCardPublicDir, fileName);
//                                    if (file.exists()) {
//                                        start2PdfActivity(file.getAbsolutePath(),fileName);
////                                        SystemAppUtil.showSystemFileShare(mAttachActivity.getApplicationContext(),file.getAbsolutePath());
//                                    } else {
//                                        onShowLoading();
//                                        String finalFileName = fileName;
//                                        OkHttpManager.getInstance().downFileGet(toUtf8String(url), null, file.getAbsolutePath(), new OnDownFileObserver() {
//                                            @Override
//                                            public void onSuccess(DownFileBean t) {
//                                                ToastUtils.showNomal("文件下载成功");
//                                                start2PdfActivity(t.downFilePath, finalFileName);
//                                                SystemUtil.scanFile(mAttachActivity.getApplicationContext(), t.downFilePath);
////                                                SystemAppUtil.showSystemFileShare(mAttachActivity.getApplicationContext(), t.downFilePath);
//                                                onDimissLoading();
//                                            }
//
//                                            @Override
//                                            public void onDownProgress(long readLength, long contentLength, int percent) {
//
//                                            }
//
//                                            @Override
//                                            public void onFail(Throwable throwable) {
//                                                onDimissLoading();
//                                            }
//                                        });
//                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        mKsWebView.setWebChromeClient(new KSWebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                mBaseWebFlWebContainer.setVisibility(View.VISIBLE);
//                mAttachActivity.runOnUiThread(() -> {
//                    if (progress != 100 && mBaseWebPbLoadingProgress.getVisibility() == View.GONE)
//                        showOrHideLoadingProgress(true);
//                    mBaseWebPbLoadingProgress.setProgress(progress);
//                    if (progress > 90 && mBaseWebFlWebContainer.getVisibility() != View.VISIBLE && mBaseWebLlError.getVisibility() == View.GONE) {
//                        mBaseWebFlWebContainer.setVisibility(View.VISIBLE);
//                    }
//                    if (progress == 100) {
//                        showOrHideLoadingProgress(false);
//                        if (!mShowTitle) {
//                            mBaseWebRlTitle.setVisibility(View.GONE);
//                            mBaseWebVStatus.setVisibility(View.GONE);
//                            mBaseWebPbLoadingProgress.setVisibility(View.GONE);
//                        }
//                    }
//                });
            }

            @Override
            public void onReceivedTitle(WebView view, String titleStr) {
                super.onReceivedTitle(view, titleStr);
//                mAttachActivity.runOnUiThread(() -> {
//                    if (!TextUtils.isEmpty(titleStr) && mShowTitle) {
//                        mCommonTitleTvTitle.setText(titleStr);
//                    }
//                });
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                openFileChooserImpl(filePathCallback);
                return true;

            }
        });

        mKsWebView.setWebViewClient(new KSWebViewClient(mAttachActivity.getApplicationContext()) {
            @Override
            public void setCurrentUrl(String currentUrl) {
                super.setCurrentUrl(currentUrl);
                mKsWebView.setCurrentUrl(currentUrl);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mAttachActivity.runOnUiThread(() -> {
                    showOrHideLoadingProgress(true);
                });

            }

            @Override
            public boolean onErrorShowDefaultView() {
//                showErrorView();
                return true;
            }
        });
        mBaseWebFlWebContainer.addView(mKsWebView);

//        mKeFu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    public void start2PdfActivity(String filePath,String fileName){
//        Intent intent = new Intent(getContext(), PdfViewActivity.class);
//        intent.putExtra(PdfViewActivity.PDF_FILE_STR,filePath);
//        intent.putExtra(PdfViewActivity.PDF_FILE_NAME,fileName);
//        startActivity(intent);
    }

    /**
     * 从手机选择上文件
     *
     * @param uploadMsg 返回信息
     */
    public synchronized void openFileChooserImpl(ValueCallback<Uri[]> uploadMsg) {
//        mAttachActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                SystemUtil.closeKey(mAttachActivity.getApplicationContext(), mKsWebView);
//                mUploadMessage = uploadMsg;
//                if (mSelectPictureDialog == null)
//                    mSelectPictureDialog = new SelectPictureDialog(mAttachActivity, R.style.CommonDialog);
//                mSelectPictureDialog.setItemData(mCommonCheckedBeans);
//                mSelectPictureDialog.setOnSelectPictureListener(new SelectPictureDialog.OnSelectPictureListener() {
//                    @Override
//                    public void onItemClick(int position, Dialog dialog) {
//                        dialog.dismiss();
//                        CommonCheckedBean<String> checkedBean = mCommonCheckedBeans.get(position);
//                        if ("Camera".equals(checkedBean.tag)) {
//                            selectPictureFromCamera();
//                        } else {
//                            selectPictureFromAlum();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelClick(Dialog dialog) {
//                        dialog.dismiss();
//                        if (mUploadMessage != null) {
//                            mUploadMessage.onReceiveValue(new Uri[]{});
//                            mUploadMessage = null;
//                        }
//                    }
//                });
//                mSelectPictureDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        if (mUploadMessage != null) {
//                            mUploadMessage.onReceiveValue(new Uri[]{});
//                            mUploadMessage = null;
//                        }
//                    }
//                });
//                mSelectPictureDialog.show();
//            }
//        });
    }

    private void showErrorView() {
        mAttachActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBaseWebFlWebContainer.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 显示隐藏进度条
     *
     * @param isShow
     */
    private void showOrHideLoadingProgress(boolean isShow) {

    }


    /**
     * 加载Url
     *
     * @param url Url
     */
    public void loadUrl(@NonNull String url) {
        WebViewManager.getInstance().loadUrl(mKsWebView, url);
    }

    /**
     * 加载字符串形式的HTML数据
     *
     * @param data 字符串形式的HTML数据
     */
    public void loadData(@NonNull String data) {
        WebViewManager.getInstance().loadData(mKsWebView, data);
    }

    /**
     * 重新加载此界面
     */
    public void reLoadUrl() {
        WebViewManager.getInstance().reloadUrl(mKsWebView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mKsWebView != null) {
            if (mClearCookieCache) {
                CookieSyncManager.createInstance(mAttachActivity.getApplicationContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                CookieSyncManager.getInstance().sync();
            }
            mKsWebView.loadUrl("about:blank");
            mKsWebView.setWebChromeClient(null);
            mKsWebView.setWebViewClient(null);
            WebViewManager.getInstance().removeKSWebView(mKsWebView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mKsWebView.destroy();
            }
            mBaseWebFlWebContainer.removeAllViews();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.common_title_tiv_left) {
            mAttachActivity.onBackPressed();
        }
    }

    @NonNull
    private static String toUtf8String(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (byte aB : b) {
                    int k = aB;
                    if (k < 0)
                        k += 256;
                    sb.append("%").append(Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
