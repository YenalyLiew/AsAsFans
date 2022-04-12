package com.fairhr.module_support.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.R;
import com.fairhr.module_support.constants.SpConstants;
import com.fairhr.module_support.router.RouteNavigationPath;
import com.fairhr.module_support.utils.ToastUtils;

/**
 * 跳转fragment承载activity
 */
@Route(path = RouteNavigationPath.ModuleSupport.KS_FRAGMENT_ACTIVITY)
public class KsFragmentActivity extends FrameActivity {

    private Bundle mBundle;
    private String mFragmentArout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_activity_ks_fragment);
        initData();
        initView();
    }

    public void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mBundle = intent.getExtras();
            mFragmentArout = mBundle.getString(SpConstants.FRAGMENT_ACT_AROUTPATH);
        }
    }

    @Override
    public void initBundleData() {

    }

    @Override
    public void initView() {
        if (!TextUtils.isEmpty(mFragmentArout)) {
            Fragment fragment = (Fragment) ARouter.getInstance().build(mFragmentArout).navigation(this);
            if (fragment != null) {
                if (mBundle != null)
                    fragment.setArguments(mBundle);
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.ks_fragment_fl, fragment);
                fragmentTransaction.commit();
            } else {
                ToastUtils.showLog("没有找到路由界面");
                finish();
            }

        } else {
            ToastUtils.showLog("路由界面为空");
            finish();
        }
    }

    @Override
    public void initServiceData() {

    }
}
