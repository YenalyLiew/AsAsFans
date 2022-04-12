package com.fairhr.module_support.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * *@description 进程工具
 */
public class ProcessUtils {

    private ProcessUtils() {
    }

    /**
     * 是否为主进程
     *
     * @param context 上下文
     * @return 是否为主进程, 包名和当前进程名相等，或者无法获取进程名都认为是主进程
     */
    public static boolean isMainProcess(@NonNull Context context) {
        String processName = getProcessName(context);
        return TextUtils.equals(context.getPackageName(), processName) || TextUtils.isEmpty(processName);
    }

    /**
     * 获得进程名称
     *
     * @param context 上下文
     * @return 进程名称
     */
    @Nullable
    public static String getProcessName(@NonNull Context context) {
        String result = "";
        //防止有些机型闪退
        try {
            ActivityManager activityManager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
                    activityManager.getRunningAppProcesses();
            if (null != runningAppProcessInfoList && 0 != runningAppProcessInfoList.size()) {
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo
                        : runningAppProcessInfoList) {
                    if (null != runningAppProcessInfo
                            && runningAppProcessInfo.pid == android.os.Process.myPid()
                            && null != runningAppProcessInfo.processName) {
                        result = runningAppProcessInfo.processName;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前进程ID
     *
     * @param context
     * @return 进程ID
     */
    public static int getProcessID(Context context) {
        return android.os.Process.myPid();
    }

}
