package com.fairhr.module_support.base;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * Author:kingstar
 * Time:2019-09-09
 * PackageName:com.kingstar.ksframework.base
 * Description: 框架层Fragment
 */
public abstract class FrameworkFragment extends Fragment{
    public Activity mAttachActivity;
    private boolean isResumedAndVisible;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAttachActivity = getActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
        //上一个界面退出时会走这里，界面切换 、前后台切换都会走这里
        if (!isHidden() && getUserVisibleHint()) {
            isResumedAndVisible = true;
            onFragmentShow();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (!isVisibleToUser) {
                isResumedAndVisible = false;
                onFragmentHide();
            } else {
                isResumedAndVisible = true;
                onFragmentShow();
            }
        }
    }

    /**
     * fragment显示
     */
    public void onFragmentShow() {

    }

    /**
     * fragmen隐藏
     */
    public void onFragmentHide() {
    }

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * findview
     */
    public abstract void findViews();


}
