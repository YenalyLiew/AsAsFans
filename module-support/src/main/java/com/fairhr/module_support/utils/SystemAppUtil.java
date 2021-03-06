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
     * ??????app
     *
     * @param context context
     * @param file    ??????path
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists()) {
            Log.i("te", "???????????????????????????");
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
     * ????????????app
     *
     * @param context     context
     * @param packageName ??????
     */
    public static void uninstallApk(Context context, String packageName) {
        if (SystemUtil.isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        } else {
            LogUtil.e("??????????????????");
        }
    }

    /**
     * ?????????????????????app??????
     *
     * @param context context
     * @param pck     ??????
     */
    public static void gotoMarket(Context context, String pck) {
        if (!SystemUtil.isHaveMarket(context)) {
            Toast.makeText(context, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
     * ??????app????????????????????????
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
            ToastUtils.showNomal("???????????????????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param context context
     * @param number  ??????
     */
    public static void callPhone(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * ????????????????????????
     *
     * @param context context
     */
    public static void openDail(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * ????????????
     *
     * @param context contex
     * @param smsBody ????????????
     * @param tel     ????????????
     */
    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     * ??????????????????
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
     * ????????????
     *
     * @param context
     * @param subject ??????
     * @param content ??????
     * @param emails  ????????????
     */
    public static void sendEmail(Context context, String subject,
                                 String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822"); // ??????
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * ????????????????????????????????????
     *
     * @param context context
     * @param title   title
     * @param url     url
     */
    public static void showSystemShareOption(Context context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "?????????" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "????????????"));
    }

    /**
     * ????????????????????????????????????
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
                shareIntent.setType("*/*");//???????????????????????????
            } else {
                shareIntent.putExtra(Intent.EXTRA_STREAM,
                        Uri.fromFile(new File(filePath)));
                shareIntent.setType("*/*");//???????????????????????????
            }
            Intent intent = Intent.createChooser(shareIntent, "????????????");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.d(e.toString());
        }

    }

    /**
     * ??????????????????
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
                    //????????????
//		?????????????????????ContentValues???
                    ContentValues contentValues = new ContentValues();
                    //???????????????
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    //android Q???????????????DATA???????????????RELATIVE_PATH??????
                    //RELATIVE_PATH?????????????????????????????????
                    //DCIM???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
                    //??????????????????
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                    //??????insert??????????????????????????????????????????
                    //EXTERNAL_CONTENT_URI????????????????????????????????????
                    qUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imageUri = qUri;
                } else {
                    File pictureSaveFile = new File(filePath, fileName);
                    qUri = getUriForFile(activity, pictureSaveFile);//??????FileProvider????????????content?????????Uri
                    imageUri = Uri.fromFile(pictureSaveFile);
                }
                Intent cameraIntent = new Intent();
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //???????????????????????????????????????????????????Uri??????????????????
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//??????Action?????????
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, qUri);//?????????????????????????????????URI
                activity.startActivityForResult(cameraIntent, requestCode);
            } else {
                File pictureSaveFile = new File(filePath, fileName);
                imageUri = Uri.fromFile(pictureSaveFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity.startActivityForResult(cameraIntent, requestCode);
            }
        } catch (Exception e) {
            ToastUtils.showNomal("?????????????????????");
        }
        return imageUri;
    }

    /**
     * ??????????????????
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
            ToastUtils.showNomal("????????????????????????????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param activity    activity
     * @param size        ????????????
     * @param filePath    ??????????????????
     * @param tarFilePath ??????????????????
     * @param requestCode ??????code
     */
    public static Uri cropPhoto(Activity activity, int size, File filePath, File tarFilePath, int requestCode) {
        try {
            String fileCompressFormat = TextUtils.isEmpty(FileUtils.getFileFormat(filePath.getAbsolutePath())) ? Bitmap.CompressFormat.PNG.toString() : FileUtils.getFileFormat(filePath.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri imageUri = getUriForFile(activity, filePath);//??????FileProvider????????????content?????????Uri
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
            ToastUtils.showNomal("?????????????????????");
        }
        return Uri.fromFile(tarFilePath);
    }


    /**
     * ??????????????????
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
                    //????????????
//		?????????????????????ContentValues???
                    ContentValues contentValues = new ContentValues();
                    //???????????????
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    //android Q???????????????DATA???????????????RELATIVE_PATH??????
                    //RELATIVE_PATH?????????????????????????????????
                    //DCIM???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
                    //??????????????????
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                    //??????insert??????????????????????????????????????????
                    //EXTERNAL_CONTENT_URI????????????????????????????????????
                    qUri = fragment.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imageUri = qUri;
                } else {
                    File pictureSaveFile = new File(filePath, fileName);
                    qUri = getUriForFile(fragment.getActivity(), pictureSaveFile);//??????FileProvider????????????content?????????Uri
                    imageUri = Uri.fromFile(pictureSaveFile);
                }
                Intent cameraIntent = new Intent();
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //???????????????????????????????????????????????????Uri??????????????????
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//??????Action?????????
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, qUri);//?????????????????????????????????URI
                fragment.startActivityForResult(cameraIntent, requestCode);
            } else {
                File pictureSaveFile = new File(filePath, fileName);
                imageUri = Uri.fromFile(pictureSaveFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                fragment.startActivityForResult(cameraIntent, requestCode);
            }
        } catch (Exception e) {
            ToastUtils.showNomal("?????????????????????");
        }
        return imageUri;
    }

    /**
     * ??????????????????
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
            ToastUtils.showNomal("????????????????????????????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param fragment    activity
     * @param size        ????????????
     * @param filePath    ??????????????????
     * @param tarFilePath ??????????????????
     * @param requestCode ??????code
     */
    public static Uri cropPhoto(Fragment fragment, int size, File filePath, File tarFilePath, int requestCode) {
        try {
            String fileCompressFormat = TextUtils.isEmpty(FileUtils.getFileFormat(filePath.getAbsolutePath())) ? Bitmap.CompressFormat.PNG.toString() : FileUtils
                    .getFileFormat(filePath.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri imageUri = getUriForFile(fragment.getContext(), filePath);//??????FileProvider????????????content?????????Uri
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
            ToastUtils.showNomal("?????????????????????");
        }
        return Uri.fromFile(tarFilePath);
    }


    /**
     * ?????????????????????
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
     * ??????????????????
     *
     * @param context
     */
    public static void openSystemSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * ????????????app????????????
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
            ToastUtils.showNomal("??????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        //????????????apk???URI???????????????intent??????????????????????????????????????????????????????????????????????????????????????????????????????
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        intent.setData(packageURI);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //?????????????????????????????????????????????
        if (Build.VERSION.SDK_INT >= 26) {
            intent.setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        } else {
            intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
        }
        context.startActivity(intent);
    }
}
