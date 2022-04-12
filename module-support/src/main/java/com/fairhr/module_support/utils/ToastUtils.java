/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.fairhr.module_support.utils;

import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.fairhr.module_support.base.BaseApplication;


/**
 * Toast工具类
 */
public class ToastUtils {
    //是否开启log toast
    private static boolean sOpenLog;
    private static Toast sToast;
    private static boolean sAllowQueue;

    public static void iniConfig(ToastConfig toastConfig) {
        sOpenLog = toastConfig.mOpenLog;
        sAllowQueue = toastConfig.mAllowQueue;
    }


    public static void showNomal(@NonNull String msg) {
        creatNomalToast(msg, Toast.LENGTH_SHORT, -1, 0, 0).show();
    }


    public static void showNomal(@NonNull String msg, int time) {
        creatNomalToast(msg, time, -1, 0, 0).show();
    }


    public static void showNomal(@NonNull String msg, int gravity, int xOffset, int yOffset) {
        creatNomalToast(msg, Toast.LENGTH_SHORT, gravity, xOffset, yOffset).show();
    }

    public static void showNomal(@NonNull String msg, int time, int gravity, int xOffset, int yOffset) {
        creatNomalToast(msg, time, gravity, xOffset, yOffset).show();
    }

    public static void showCenterToast(@NonNull String msg) {
        creatNomalToast(msg, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0).show();
    }

    private static Toast creatNomalToast(@NonNull String msg, int time, int gravity, int xOffset, int yOffset) {
        resetToast();
        sToast = Toast.makeText(BaseApplication.sApplication, msg, time);
        if (gravity >= 0) {
            sToast.setGravity(gravity, xOffset, yOffset);
        }
        return sToast;
    }

    public static void showNomal(@StringRes int msgResId) {
        creatNomalToast(msgResId, Toast.LENGTH_SHORT, -1, 0, 0).show();
    }

    public static void showNomal(@StringRes int msgResId, int time) {
        creatNomalToast(msgResId, time, -1, 0, 0).show();
    }

    public static void showNomal(@StringRes int msgResId, int gravity, int xOffset, int yOffset) {
        creatNomalToast(msgResId, Toast.LENGTH_SHORT, gravity, xOffset, yOffset).show();
    }

    public static void showNomal(@StringRes int msgResId, int time, int gravity, int xOffset, int yOffset) {
        creatNomalToast(msgResId, time, gravity, xOffset, yOffset).show();
    }


    private static Toast creatNomalToast(@StringRes int msgResId, int time, int gravity, int xOffset, int yOffset) {
        resetToast();
        sToast = Toast.makeText(BaseApplication.sApplication, msgResId, time);
        if (gravity >= 0) {
            sToast.setGravity(gravity, xOffset, yOffset);
        }
        return sToast;
    }

    public static void showLog(@NonNull String msg) {
        if (sOpenLog)
            showNomal(msg);
    }


    /**
     *
     */
    private static void resetToast() {
        if (sToast != null && !sAllowQueue) {
            sToast.cancel();
            sToast = null;
        }
    }


    public static final class ToastConfig {
        private boolean mAllowQueue;
        private boolean mOpenLog;

        public ToastConfig setAllowQueue(boolean allowQueue) {
            mAllowQueue = allowQueue;
            return this;
        }

        public ToastConfig setOpenLog(boolean openLog) {
            mOpenLog = openLog;
            return this;
        }

    }


}
