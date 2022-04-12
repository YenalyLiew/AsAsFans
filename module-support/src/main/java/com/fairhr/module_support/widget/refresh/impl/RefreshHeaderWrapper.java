package com.fairhr.module_support.widget.refresh.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.fairhr.module_support.widget.refresh.api.RefreshHeader;
import com.fairhr.module_support.widget.refresh.internal.InternalAbstract;


/**
 * 刷新头部包装
 * Created by scwang on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
