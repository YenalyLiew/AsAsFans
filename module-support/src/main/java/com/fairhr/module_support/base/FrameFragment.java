package com.fairhr.module_support.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.fairhr.module_support.tools.inter.IBackPressedListener;


/**
 * Description: 框架层Fragment
 */

public abstract class FrameFragment extends Fragment implements IBackPressedListener {
    public AppCompatActivity mAttachActivity;
    public boolean isResumedAndVisible;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAttachActivity = (AppCompatActivity) getActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
        //上一个界面退出时会走这里，界面切换 、前后台切换都会走这里
        if (!isHidden()) {
            isResumedAndVisible = true;
            onFragmentShow();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //显示隐藏都会走这里
        if (isResumed()) {
            if (hidden) {
                isResumedAndVisible = false;
                onFragmentHide();
            } else {
                isResumedAndVisible = true;
                onFragmentShow();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isResumedAndVisible) {
            isResumedAndVisible = false;
            onFragmentHide();
        }
    }

    /**
     * fragment显示
     * onResume会执行
     */
    public void onFragmentShow() {

    }

    /**
     * fragmen隐藏
     * onPause会执行
     */
    public void onFragmentHide() {


    }

    /**
     * 初始化数据
     */
    public abstract void initBundleData();

    /**
     * 初始化view
     */
    public abstract void initView();


    /**
     * 初始化服务器舒数据
     */
    public abstract void initServiceData();


    /**
     * fragment返回键监听
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
