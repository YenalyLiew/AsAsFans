package com.fairhr.module_support.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


/**
 * Description: APP 页面管理类
 */
public class ApplicationManager implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityStackManager";
    private static ApplicationManager sApplicationManager;
    //Activity栈
    private Stack<WeakReference<Activity>> mActivityStack;
    // 当前可见的activity数量
    private int mActiveActivitiesCount;
    //(前后)台变化了主题
    @NonNull
    private Subject<Boolean> mGroundChangedSubject;

    /**
     * 获取应用前后台监听
     *
     * @return
     */
    @NonNull
    public Subject<Boolean> getGroundChangedSubject() {
        return mGroundChangedSubject;
    }

    private ApplicationManager() {
        mGroundChangedSubject = PublishSubject.create();
    }

    /***
     * 获得AppManager的实例
     *
     * @return AppManager实例
     */
    public static ApplicationManager getInstance() {
        if (sApplicationManager == null) {
            synchronized (ApplicationManager.class) {
                if (sApplicationManager == null)
                    sApplicationManager = new ApplicationManager();
            }
        }
        return sApplicationManager;
    }

    /***
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int stackSize() {
        return mActivityStack == null ? 0 : mActivityStack.size();
    }

    /***
     * 获得Activity栈
     *
     * @return Activity栈
     */
    public Stack<WeakReference<Activity>> getStack() {
        return mActivityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    private void addActivity(WeakReference<Activity> activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.push(activity);
    }

    /**
     * 删除ac
     *
     * @param activity 弱引用的ac
     */
    private void removeActivity(WeakReference<Activity> activity) {
        if (mActivityStack != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 删除ac
     *
     * @param activity 弱引用的ac
     */
    public void removeActivity(Activity activity) {
        if (mActivityStack != null) {
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> stackActivity = iterator.next();
                if (stackActivity.get() == null) {
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get() == activity) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 判断某个页面是否存在
     *
     * @param cls cls，比如：MainActivity.class.getCanonicalName().
     * @return
     */
    public boolean isActivityRunning(String cls) {
        if (TextUtils.isEmpty(cls) || mActivityStack == null) {
            return false;
        }

        for (WeakReference<Activity> activity : mActivityStack) {
            if (activity.get().getClass().getCanonicalName().equals(cls)) {
                return true;
            }
        }

        return false;
    }


    /***
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    public Activity getTopActivity() {
        if (mActivityStack == null)
            return null;
        Activity activity = mActivityStack.lastElement().get();
        if (null == activity) {
            return null;
        } else {
            return mActivityStack.lastElement().get();
        }
    }

    /***
     * 通过class 获取栈顶Activity
     *
     * @param cls
     * @return Activity
     */
    public Activity getActivityByClass(Class<?> cls) {
        Activity resultActivity = null;
        if (mActivityStack == null)
            return null;
        for (WeakReference<Activity> activity : mActivityStack) {
            if (activity.get().getClass().equals(cls)) {
                resultActivity = activity.get();
                break;
            }
        }
        return resultActivity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void finishTopActivity() {
        try {
            WeakReference<Activity> activity = mActivityStack.lastElement();
            finishActivity(activity);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /***
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(WeakReference<Activity> activity) {
        try {
            if (mActivityStack == null)
                return;
            Iterator<WeakReference<Activity>> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                WeakReference<Activity> stackActivity = iterator.next();
                if (stackActivity.get() == null) {
                    iterator.remove();
                    continue;
                }
                if (stackActivity.get().getClass().getName().equals(activity.get().getClass().getName())) {
                    iterator.remove();
                    stackActivity.get().finish();
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /***
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        try {
            if (mActivityStack == null)
                return;
            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity == null) {
                    listIterator.remove();
                    continue;
                }
                if (activity.getClass() == cls) {
                    listIterator.remove();
                    activity.finish();
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        try {
            if (mActivityStack == null)
                return;
            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    activity.finish();
                }
                listIterator.remove();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param cls 界面
     */
    public void finishAllActivityOnlyOne(Class cls) {
        try {
            if (mActivityStack == null)
                return;
            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    if (activity.getClass().getName().equals(cls.getName())) {
                        continue;
                    }
                    activity.finish();
                    listIterator.remove();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param cls 界面
     */
    public void finishAllActivityOnlyOneAndTop(Class cls) {
        if (mActivityStack == null)
            return;
        WeakReference<Activity> activityWeakReference = mActivityStack.lastElement();
        Activity lastActivity = activityWeakReference.get();
        try {
            ListIterator<WeakReference<Activity>> listIterator = mActivityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next().get();
                if (activity != null) {
                    if (activity.getClass().getName().equals(cls.getName()) ||
                            activity.getClass().getName().equals(lastActivity.getClass().getName())) {
                        continue;
                    }
                    activity.finish();
                    listIterator.remove();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 退出应用程序
     */
    public void restartAPP(Context context) {
        try {
            finishAllActivity();
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        addActivity(new WeakReference<Activity>(activity));
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        mActiveActivitiesCount++;
        if (1 == mActiveActivitiesCount)
            mGroundChangedSubject.onNext(true);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        mActiveActivitiesCount--;
        if (0 == mActiveActivitiesCount)
            mGroundChangedSubject.onNext(false);
    }

    /**
     * 应用是否在前台
     *
     * @return 应用是否在前台
     */
    public boolean isApplicationInForeground() {
        return mActiveActivitiesCount > 0;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        removeActivity(activity);
    }

    /**
     * 获得Activities栈的一份(浅)复制
     *
     * @return Activities栈的一份(浅)复制
     */
    @NonNull
    public List<WeakReference<Activity>> getActivitiesStackCopy() {
        return new ArrayList<>(mActivityStack);
    }
}
