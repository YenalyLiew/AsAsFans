package com.fairhr.module_support;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import android.os.Process;

public class KtxActivityManger {

    private static List<Activity> mActivityList = new LinkedList<>();

    public static Activity getCurrentActivity(){
        if(mActivityList.isEmpty()){
            return null;
        }else{
            return mActivityList.get(mActivityList.size() - 1);
        }
    }

    /**
     * activity入栈
     */
    public static void pushActivity(Activity activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.get(mActivityList.size() - 1) != activity) {
                mActivityList.remove(activity);
                mActivityList.add(activity);
            }
        } else {
            mActivityList.add(activity);
        }
    }

    /**
     * activity出栈
     */
    public static void popActivity(Activity activity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
        }
    }

    /**
     * 关闭当前activity
     */
    public static void finishCurrentActivity() {
        if(getCurrentActivity() != null){
            getCurrentActivity().finish();
        }
    }

    /**
     * 关闭传入的activity
     */
    public static void finishActivity(Activity activity) {
        mActivityList.remove(activity);
        activity.finish();
    }

    /**
     * 关闭指定类名的所有Activity
     * @param cls
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivityList != null) {
            // 使用迭代器进行安全删除
            for (Iterator<Activity> it = mActivityList.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }
    }

    /**
     * 关闭所有的activity
     */
    public static void finishAllActivity() {
        if(!mActivityList.isEmpty()){
            for (int i = 0; i < mActivityList.size(); i++) {
                Activity activity = mActivityList.get(i);
                activity.finish();
            }
        }
    }

    /**
     * 退出APP
     */
    public static void exitApp() {
        try {
            finishAllActivity();
            Process.killProcess(Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出APP，不杀进程。
     */
    public static void exitAppWithoutKillingProcess() {
        finishAllActivity();
    }

    /**
     * 一键回到主页
     */
    public static void directToMain() {
        if (mActivityList.size() > 1) {
            for (int i = mActivityList.size() - 1; i < mActivityList.size(); i--) {
                if (mActivityList.get(i).getLocalClassName() != "com.fairhr.ers.activity.MainActivity") {
                    mActivityList.get(i).finish();
                }
            }
        }
    }
}
