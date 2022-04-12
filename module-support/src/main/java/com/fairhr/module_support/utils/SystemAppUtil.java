package com.fairhr.module_support.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SystemAppUtil {
    private static Map<String, String> mTypeMap = new HashMap<>();

    static {
        mTypeMap.put("3gp", "video/3gpp");
        mTypeMap.put("apk", "application/vnd.android.package-archive");
        mTypeMap.put("asf", "video/x-ms-asf");
        mTypeMap.put("avi", "video/x-msvideo");
        mTypeMap.put("bin", "application/octet-stream");
        mTypeMap.put("bmp", "image/bmp");
        mTypeMap.put("c", "text/plain");
        mTypeMap.put("class", "application/octet-stream");
        mTypeMap.put("conf", "text/plain");
        mTypeMap.put("cpp", "text/plain");
        mTypeMap.put("doc", "application/msword");
        mTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mTypeMap.put("xls", "application/vnd.ms-excel");
        mTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mTypeMap.put("exe", "application/octet-stream");
        mTypeMap.put("gif", "image/gif");
        mTypeMap.put("gtar", "application/x-gtar");
        mTypeMap.put("gz", "application/x-gzip");
        mTypeMap.put("h", "text/plain");
        mTypeMap.put("htm", "text/html");
        mTypeMap.put("html", "text/html");
        mTypeMap.put("jar", "application/java-archive");
        mTypeMap.put("java", "text/plain");
        mTypeMap.put("jpeg", "image/jpeg");
        mTypeMap.put("jpg", "image/jpeg");
        mTypeMap.put("js", "application/x-JavaScript");
        mTypeMap.put("log", "text/plain");
        mTypeMap.put("m3u", "audio/x-mpegurl");
        mTypeMap.put("m4a", "audio/mp4a-latm");
        mTypeMap.put("m4b", "audio/mp4a-latm");
        mTypeMap.put("m4p", "audio/mp4a-latm");
        mTypeMap.put("m4u", "video/vnd.mpegurl");
        mTypeMap.put("m4v", "video/x-m4v");
        mTypeMap.put("mov", "video/quicktime");
        mTypeMap.put("mp2", "audio/x-mpeg");
        mTypeMap.put("mp3", "audio/x-mpeg");
        mTypeMap.put("mp4", "video/mp4");
        mTypeMap.put("mpc", "application/vnd.mpohun.certificate");
        mTypeMap.put("mpe", "video/mpeg");
        mTypeMap.put("mpeg", "video/mpeg");
        mTypeMap.put("mpg", "video/mpeg");
        mTypeMap.put("mpg4", "video/mp4");
        mTypeMap.put("mpga", "audio/mpeg");
        mTypeMap.put("msg", "application/vnd.ms-outlook");
        mTypeMap.put("ogg", "audio/ogg");
        mTypeMap.put("pdf", "application/pdf");
        mTypeMap.put("png", "image/png");
        mTypeMap.put("pps", "application/vnd.ms-powerpoint");
        mTypeMap.put("ppt", "application/vnd.ms-powerpoint");
        mTypeMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        mTypeMap.put("prop", "text/plain");
        mTypeMap.put("rc", "text/plain");
        mTypeMap.put("rmvb", "audio/x-pn-realaudio");
        mTypeMap.put("rtf", "application/rtf");
        mTypeMap.put("sh", "text/plain");
        mTypeMap.put("tar", "application/x-tar");
        mTypeMap.put("tgz", "application/x-compressed");
        mTypeMap.put("txt", "text/plain");
        mTypeMap.put("wav", "audio/x-wav");
        mTypeMap.put("wma", "audio/x-ms-wma");
        mTypeMap.put("wmv", "audio/x-ms-wmv");
        mTypeMap.put("wps", "application/vnd.ms-works");
        mTypeMap.put("xml", "text/plain");
        mTypeMap.put("z", "application/x-compress");
        mTypeMap.put("zip", "application/x-zip-compressed");
        mTypeMap.put("", "*/*");
    }

    /**
     * 安装app
     *
     * @param context context
     * @param file    文件path
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists()) {
            Log.i("te", "文件为空或者不存在");
            return;
        }
        try {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                boolean hasInstallPermission = SystemUtil.isHasInstallPermissionWithO(context);
                if (!hasInstallPermission) {
                    startInstallPermissionSettingActivity(context);
                    return;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(getUriForFile(context, file),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            }

        } catch (Exception e) {
            LogUtil.e(e.toString());
        }

    }

    /**
     * 卸载指定app
     *
     * @param context     context
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        if (SystemUtil.isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        } else {
            LogUtil.e("未安装此应用");
        }
    }

    /**
     * 去查看指定包的app市场
     *
     * @param context context
     * @param pck     包名
     */
    public static void gotoMarket(Context context, String pck) {
        if (!SystemUtil.isHaveMarket(context)) {
            Toast.makeText(context, "你手机中没有安装应用市场！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    /**
     * 打开app市场并显示本应用
     *
     * @param context context
     */
    public static void openAppInMarket(Context context) {
        if (context != null) {
            String pckName = context.getPackageName();
            try {
                gotoMarket(context, pckName);
            } catch (Exception ex) {
                try {
                    String otherMarketUri = "http://market.android.com/details?id=" + pckName;
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(otherMarketUri));
                    context.startActivity(intent);
                } catch (Exception e) {

                }
            }
        }
    }

    public static void shareAppShop(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showNomal("您没有安装应用市场");
        }
    }

    /**
     * 调用系统拨打电话
     *
     * @param context context
     * @param number  号码
     */
    public static void callPhone(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 打开系统拨号界面
     *
     * @param context context
     */
    public static void openDail(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发送短信
     *
     * @param context contex
     * @param smsBody 消息内容
     * @param tel     电话号码
     */
    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     * 打开短信界面
     *
     * @param context context
     */
    public static void openSendMsg(Context context) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param subject 主题
     * @param content 内容
     * @param emails  邮件地址
     */
    public static void sendEmail(Context context, String subject,
                                 String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822"); // 真机
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 调用系统安装了的应用分享
     *
     * @param context context
     * @param title   title
     * @param url     url
     */
    public static void showSystemShareOption(Context context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context context
     */
    public static void showSystemFileShare(Context context,
                                           String filePath) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                shareIntent.putExtra(Intent.EXTRA_STREAM,
                        getUriForFile(context, new File(filePath)));
                shareIntent.setType("*/*");//此处可发送多种文件
            } else {
                shareIntent.putExtra(Intent.EXTRA_STREAM,
                        Uri.fromFile(new File(filePath)));
                shareIntent.setType("*/*");//此处可发送多种文件
            }
            Intent intent = Intent.createChooser(shareIntent, "选择分享");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.d(e.toString());
        }

    }

    /**
     * 打开系统相机
     *
     * @param activity
     * @param filePath
     * @param fileName
     * @param requestCode
     */
    public static Uri openCamera(Activity activity, String filePath, String fileName, int requestCode) {
        Uri imageUri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri qUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //设置参数
//		设置保存参数到ContentValues中
                    ContentValues contentValues = new ContentValues();
                    //设置文件名
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                    //RELATIVE_PATH是相对路径不是绝对路径
                    //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
                    //设置文件类型
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                    //执行insert操作，向系统文件夹中添加文件
                    //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
                    qUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imageUri = qUri;
                } else {
                    File pictureSaveFile = new File(filePath, fileName);
                    qUri = getUriForFile(activity, pictureSaveFile);//通过FileProvider创建一个content类型的Uri
                    imageUri = Uri.fromFile(pictureSaveFile);
                }
                Intent cameraIntent = new Intent();
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, qUri);//将拍取的照片保存到指定URI
                activity.startActivityForResult(cameraIntent, requestCode);
            } else {
                File pictureSaveFile = new File(filePath, fileName);
                imageUri = Uri.fromFile(pictureSaveFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity.startActivityForResult(cameraIntent, requestCode);
            }
        } catch (Exception e) {
            ToastUtils.showNomal("请打开相机权限");
        }
        return imageUri;
    }

    /**
     * 打开系统相册
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhotoAlum(Activity activity, int requestCode) {
        try {
            Intent photoAlumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoAlumIntent.setType("image/*");
            activity.startActivityForResult(photoAlumIntent, requestCode);
        } catch (Exception e) {
            ToastUtils.showNomal("打开相册失败，请检查权限是否打开");
        }
    }

    /**
     * 裁剪指定图片
     *
     * @param activity    activity
     * @param size        裁剪尺寸
     * @param filePath    裁剪图片文件
     * @param tarFilePath 裁剪图片文件
     * @param requestCode 请求code
     */
    public static Uri cropPhoto(Activity activity, int size, File filePath, File tarFilePath, int requestCode) {
        try {
            String fileCompressFormat = TextUtils.isEmpty(FileUtils.getFileFormat(filePath.getAbsolutePath())) ? Bitmap.CompressFormat.PNG.toString() : FileUtils.getFileFormat(filePath.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri imageUri = getUriForFile(activity, filePath);//通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tarFilePath));
                intent.putExtra("outputFormat", fileCompressFormat);
                intent.putExtra("noFaceDetection", true); // no face detection
                activity.startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Uri.fromFile(filePath), "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", size);
                intent.putExtra("outputY", size);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tarFilePath));
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", fileCompressFormat);
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            ToastUtils.showNomal("此文件无法裁剪");
        }
        return Uri.fromFile(tarFilePath);
    }


    /**
     * 打开系统相机
     *
     * @param fragment
     * @param filePath
     * @param fileName
     * @param requestCode
     */
    public static Uri openCamera(Fragment fragment, String filePath, String fileName, int requestCode) {
        Uri imageUri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri qUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //设置参数
//		设置保存参数到ContentValues中
                    ContentValues contentValues = new ContentValues();
                    //设置文件名
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                    //RELATIVE_PATH是相对路径不是绝对路径
                    //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
                    //设置文件类型
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                    //执行insert操作，向系统文件夹中添加文件
                    //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
                    qUri = fragment.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imageUri = qUri;
                } else {
                    File pictureSaveFile = new File(filePath, fileName);
                    qUri = getUriForFile(fragment.getActivity(), pictureSaveFile);//通过FileProvider创建一个content类型的Uri
                    imageUri = Uri.fromFile(pictureSaveFile);
                }
                Intent cameraIntent = new Intent();
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, qUri);//将拍取的照片保存到指定URI
                fragment.startActivityForResult(cameraIntent, requestCode);
            } else {
                File pictureSaveFile = new File(filePath, fileName);
                imageUri = Uri.fromFile(pictureSaveFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                fragment.startActivityForResult(cameraIntent, requestCode);
            }
        } catch (Exception e) {
            ToastUtils.showNomal("请打开相机权限");
        }
        return imageUri;
    }

    /**
     * 打开系统相册
     *
     * @param fragment
     * @param requestCode
     */
    public static void openPhotoAlum(Fragment fragment, int requestCode) {
        try {
            Intent photoAlumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoAlumIntent.setType("image/*");
            fragment.startActivityForResult(photoAlumIntent, requestCode);
        } catch (Exception e) {
            ToastUtils.showNomal("打开相册失败，请检查权限是否打开");
        }
    }

    /**
     * 裁剪指定图片
     *
     * @param fragment    activity
     * @param size        裁剪尺寸
     * @param filePath    裁剪图片文件
     * @param tarFilePath 裁剪图片文件
     * @param requestCode 请求code
     */
    public static Uri cropPhoto(Fragment fragment, int size, File filePath, File tarFilePath, int requestCode) {
        try {
            String fileCompressFormat = TextUtils.isEmpty(FileUtils.getFileFormat(filePath.getAbsolutePath())) ? Bitmap.CompressFormat.PNG.toString() : FileUtils
                    .getFileFormat(filePath.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri imageUri = getUriForFile(fragment.getContext(), filePath);//通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tarFilePath));
                intent.putExtra("outputFormat", fileCompressFormat);
                intent.putExtra("noFaceDetection", true); // no face detection
                fragment.startActivityForResult(intent, requestCode);
            } else {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Uri.fromFile(filePath), "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", size);
                intent.putExtra("outputY", size);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tarFilePath));
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", fileCompressFormat);
                fragment.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            ToastUtils.showNomal("此文件无法裁剪");
        }
        return Uri.fromFile(tarFilePath);
    }


    /**
     * 获取文件提供者
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        String authority = context.getPackageName() + ".provider";
        return FileProvider.getUriForFile(context, authority, file);
    }

    /**
     * 打开系统设置
     *
     * @param context
     */
    public static void openSystemSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 通过系统app打开文件
     *
     * @param context
     * @param filePath
     */
    public static void openFileBySystemApp(Context context, String filePath) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = getUriForFile(context, new File(filePath));
                intent.setDataAndType(uri, mTypeMap.get(FileUtils.getFileFormat(filePath)));
            } else {
                Uri uri = Uri.fromFile(new File(filePath));
                intent.setDataAndType(uri, mTypeMap.get(FileUtils.getFileFormat(filePath)));
            }
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.d(e.toString());
            ToastUtils.showNomal("没有找到应用打开该类型的文件");
        }
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        intent.setData(packageURI);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //设置不同版本跳转未知应用的动作
        if (Build.VERSION.SDK_INT >= 26) {
            intent.setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        } else {
            intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
        }
        context.startActivity(intent);
    }
}
