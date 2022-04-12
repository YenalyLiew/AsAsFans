package com.fairhr.module_support.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;


import com.fairhr.module_support.R;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * 系统工具
 * <p>
 * Created by wangbin on 2016/11/22.
 */

public class SystemUtil {
    /**
     * 获取应用程序名称
     */
    public static synchronized String getLableIdAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getLableAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.applicationInfo.loadLabel(packageManager).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当前app VersionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            name = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            LogUtil.e(ex.toString());
        }
        return name;
    }

    /**
     * 当前app ersionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            LogUtil.e(ex.toString());
        }
        return versionCode;
    }

    /**
     * 检查包是否存在
     *
     * @param context context
     * @param pckName 包名
     * @return 是否存在
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(e.getMessage());
        }
        return false;
    }

    /**
     * 检查手机是否安装有app市场
     *
     * @param context context
     * @return 是否有
     */
    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    /**
     * 返回home
     *
     * @param context
     */
    public static void goHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }


    /**
     * 打开指定包名app
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void openOtherApp(Context context, String packageName) {
        Intent mainIntent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        if (mainIntent == null) {
            mainIntent = new Intent(packageName);
        } else {
            LogUtil.i("Action:" + mainIntent.getAction());
        }
        context.startActivity(mainIntent);
    }

    /**
     * 打开指定activity
     *
     * @param context      context
     * @param packageName  包名
     * @param activityName activity名（包含package）
     */
    public static boolean openAppActivity(Context context, String packageName, String activityName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, activityName);
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            LogUtil.i(e.toString());
            return false;
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    public static void scanFile(Context ctx, String filePath) {
        try {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(filePath);
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUri = SystemAppUtil.getUriForFile(ctx, file);
            } else {
                contentUri = Uri.fromFile(file);
            }
            mediaScanIntent.setData(contentUri);
            ctx.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            ToastUtils.showLog("刷新文件路劲异常，" + e.toString());
        }
    }


    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static String getProcessName(Context context) {
        String processName = "";
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        if (am != null) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid() && !TextUtils.isEmpty(info.processName)) {
                    processName = info.processName;
                    break;
                }
            }
        }
        return processName;
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

    /**
     * 判断键盘是否打开
     *
     * @param activity
     * @return
     */
    private boolean isSoftShowing(Activity activity) {
        //获取当前屏幕内容的高度
        Window window = activity.getWindow();
        int screenHeight = window.getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void closeKey(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }


    /**
     * 打开软键盘
     *
     * @param context
     */
    public static void openKey(Context context) {
        InputMethodManager imm = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     *
     * @param context
     */
    public static void openKey(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> myList = activityManager.getRunningServices(Integer.MAX_VALUE);
            if (myList == null || myList.size() == 0) {
                return false;
            }
            for (int i = 0; i < myList.size(); i++) {
                String mName = myList.get(i).service.getClassName();
                if (mName.equals(serviceName)) {
                    isWork = true;
                    break;
                }
            }
        }
        return isWork;
    }

    /**
     * 判断当前app是否后台
     *
     * @param context
     * @return
     */
    public static boolean isBackgroundWork(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                }
            }
        }
        return false;
    }

    /**
     * 当前屏幕是否开启
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm != null && pm.isScreenOn();
    }


    /**
     * 当前屏幕是否开启
     *
     * @param context
     * @return
     */
    @SuppressLint("InvalidWakeLockTag")
    public static void wakeAndUnlock(Context context) {
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
        }
    }

    /**
     * 复制文本
     *
     * @param context
     * @param text
     */
    public static void copyTextToClip(Context context, String text, String copySuccessTips) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", text);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(myClip);
            ToastUtils.showNomal(TextUtils.isEmpty(copySuccessTips) ? context.getString(R.string.support_copy_success_tips) : copySuccessTips);
        }
    }

    /**
     * 当前界面是否为横屏显示
     */
    public static boolean isLandspaceScreen(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        return mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    public static boolean isMainProcess(Context context) {
        return context.getApplicationContext().getPackageName().equals
                (getCurrentProcessName(context));
    }

    /**
     * 获取当前进程名
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /*****************************处理声音焦点Start*****************************/
    /**
     * 获取播放焦点
     *
     * @param context
     */
    public static void requestAudioFocus(Context context) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://Pause playback
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN://Resume playback
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS://Stop playback
                            audioManager.abandonAudioFocus(this);
                            break;
                    }
                }
            };
            audioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }

    /**
     * 释放播放焦点
     *
     * @param context
     */
    public static void abandonAudioFocus(Context context) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://Pause playback
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN://Resume playback
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS://Stop playback
                            break;
                    }
                }
            };
            audioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }


    /*****************************处理声音焦点end*****************************/


    /*****************************切换app语言Start*****************************/
    /**
     * 获得当前系统语言
     *
     * @param context
     * @return
     */
    public static Locale getSystemLanguage(Context context) {
        Resources resources = context.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        return config.locale;
    }


    /**************************切换app语言end*********************************/

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isHasInstallPermissionWithO(Context context){
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }


}
