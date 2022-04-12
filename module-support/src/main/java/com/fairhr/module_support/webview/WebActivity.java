package com.fairhr.module_support.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.R;
import com.fairhr.module_support.router.RouteNavigationPath;
import com.fairhr.module_support.utils.ToastUtils;
import com.fairhr.module_support.webview.biz.WebBaseActivity;

import kotlin.jvm.JvmField;

@Route(path = RouteNavigationPath.ModuleSupport.SUPPORT_WEB)
public class WebActivity extends WebBaseActivity {

    @Autowired
    @JvmField
    String title= null;

    @Autowired
    @JvmField
    String url = "";

    @Autowired
    @JvmField
    Boolean share = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
            showWebTitle = false;
        } else {
            showWebTitle = true;
        }
        if (!URLUtil.isValidUrl(url)) {
            ToastUtils.showNomal(R.string.support_url_error);
            finish();
            return;
        }

        if (share){
            titleMore.setVisibility(View.VISIBLE);
        }else{
            titleMore.setVisibility(View.GONE);
        }

        mAgentWeb.go(url);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();

    }

}
