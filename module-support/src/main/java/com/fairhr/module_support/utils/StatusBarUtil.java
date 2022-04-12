package com.fairhr.module_support.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 处理沉浸式状态栏工具类
 */
public class StatusBarUtil {

    /**
     * 提供给Activity或者Fragment调用来实现沉浸式的方法
     * [activity]上下文对象
     * [isImmersion]是否需要支持沉浸式，支持的话需要将指定的[expandView]扩展到状态栏，默认需要展示沉浸式
     * [isHideNavigation]是否需要隐藏底部的导航栏，默认不需要隐藏
     * [isLightMode]扩展到标题栏的View是否是浅色，因为在Android 6.0以后，可以修改状态栏字体和图标颜色
     * [expandView]需要扩展至状态栏的View,会给这个View添加一个状态栏高度的paddingTop,如果要实现沉浸式则此参数不可为空，
     * 当仅仅需要改变状态栏的字体和图标颜色的时候可以传空
     */
    public static void setStatusBarImmersion(Activity activity, boolean isImmersion,
                                             boolean isHideNavigation,
                                             boolean isLightMode, View expandView) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (isImmersion) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //大于Android M 且不是联想ZUI系统，则状态栏透明
                    window.setStatusBarColor(Color.TRANSPARENT);
                    //浅色则使用浅色状态栏
                    if(isLightMode){
                        visibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    }else{
                        visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                    }
                    //小米系统处理方式
                    if (DeviceUtil.deviceIsMIUI()) {
                        setMIUIStatusBar(activity, isLightMode);
                    }
                } else {
                    window.setStatusBarColor(Integer.parseInt(Integer.toHexString(0xFF212138),16));
                    visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                }
            } else {
                visibility = 0;
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //透明导航栏
            if (isHideNavigation) {
                window.setNavigationBarColor(Color.TRANSPARENT);
                visibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(visibility);
            if (isImmersion && expandView != null) {
                expandViewForStatusBar(activity, expandView);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isImmersion) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (expandView != null) {
                    expandViewForStatusBar(activity, expandView);
                }
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //透明导航栏
            if (isHideNavigation) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 将View添加状态栏一样高度的PaddingTop来实现沉浸式
     */
    private static void expandViewForStatusBar(Context context, View view) {
        view.setPadding(
                view.getPaddingLeft(),
                view.getPaddingTop() + getStatusBarHeight(context),
                view.getPaddingRight(),
                view.getPaddingBottom());
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context){
        int result = 20;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    /**
     * 设置MIUI V6以上状态栏的颜色
     */
    private static void setMIUIStatusBar(Activity activity, boolean isLightMode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int tranceFlag;
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager.LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Integer.class,Integer.class);
            //状态栏透明且黑色字体
            if(isLightMode){
                tranceFlag = darkModeFlag;
            }else{
                tranceFlag = 0;
            }

            extraFlagField.invoke(
                    activity.getWindow(),
                    tranceFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
