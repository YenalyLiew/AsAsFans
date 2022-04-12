package com.fairhr.module_support.utils;

import static android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.fairhr.module_support.R;

import java.io.File;
import java.security.Provider;

public class AppUtils {

    public static boolean isDebug = false;

    public static void syncIsDebug(Context context){
        PackageManager pm = context.getPackageManager();
        try{
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            isDebug = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }catch(PackageManager.NameNotFoundException e){
            /*debuggable variable will remain false*/
        }
    }

    public static boolean getDebugMode(){
        return isDebug;
    }

    /**
     * 跳转到系统应用详情界面
     */
    public static void openAppDetailSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    public static void shareTxt(Context context,String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);  //设置分享行为
        String type = "text/plain"; //设置分享内容的类型
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");  //添加分享内容标题
        intent.putExtra(Intent.EXTRA_TEXT, content);  //添加分享内容

        //创建分享的Dialog
        Intent chooser = Intent.createChooser(intent, "分享到");
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooser);
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static Boolean sharePic(Context context, File file){
        if(file != null){
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileUtils.getFileUri(context, file);
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                }
                intent.setType("image/*");
                Intent chooser = Intent.createChooser(intent, "分享到");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(chooser);
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean sharePic(Context context,String picFilePath){
        File shareFile = new File(picFilePath);
        return sharePic(context, shareFile);
    }

    public static void shareFile(Context context,String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(
                        context, context.getString(R.string.support_file_provider_authorities), file);
                share.putExtra(Intent.EXTRA_STREAM, contentUri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            share.setType("application/vnd.ms-excel");//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "分享文件"));
        } else {
            Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    public static void openBrowser(Context context, String targetUrl) {
        if (!URLUtil.isNetworkUrl(targetUrl)) {
//            context.getString(R.string.support_url_error).toast(context)
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
//            "浏览器不存在".toast()
        }
    }

    public static void callTel(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL); // 显示拨号界面
        // 直接拨打（<uses-permission android:name="android.permission.CALL_PHONE"/>）
        // intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:$phone"));
        context.startActivity(intent);
    }

    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key){
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        context.getPackageName(),
                        PackageManager.GET_META_DATA
                );
                if (applicationInfo.metaData != null) {
                    channelNumber = applicationInfo.metaData.getString(key);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }
}
