package com.fairhr.module_support.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 */
public class CommonUtils {
    /**
     * 获取文本的MD5值
     *
     * @param value 字符串
     * @return 字符串
     */
    public static String getStrToMD5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(value.getBytes());
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return value;
        }
    }

    /**
     * 获取字节的MD5值
     *
     * @param bytes 字节
     * @return
     */
    public static String getBytesToMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(bytes);
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取文件的MD5值
     *
     * @param filePath 文件地址
     * @return
     */
    public static String getFileToMD5(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        File file = new File(filePath);
        if (!file.isFile())
            return "";
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return toHexString(digest.digest());
    }


    /**
     * 将MD5的字节转成字符串
     *
     * @param bytes 字节
     * @return
     */
    private static String toHexString(byte bytes[]) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0xff);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }



    /**
     * 设置透明度
     *
     * @param precent
     * @param color
     * @return
     */
    public static int changeTransparentColor(float precent, String color) {
        if (precent > 1)
            precent = 1;
        if (precent < 0)
            precent = 0;
        if (color.contains("#"))
            color.replace("#", "");
        String strHex = Integer.toHexString((int) (precent * 255));
        strHex = strHex.length() == 1 ? "0" + strHex : strHex;
        return Color.parseColor("#" + strHex + color);
    }

    /**
     * 通常资源名字和类型获取id
     *
     * @param context
     * @param type
     * @param resourceName
     * @return
     */
    public static int getResourceId(Context context, String type, String resourceName) {
        try {
            ApplicationInfo appInfo = context.getApplicationInfo();
            int resID = context.getResources().getIdentifier(resourceName, type, appInfo.packageName);
            return resID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //判断手机号是否合法
    public static boolean isChinaPhoneLegal(String str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$";;
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //判断邮箱是否合法
    public static boolean isChinaEmailLegal(String str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        Pattern pattern = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");//\w表示a-z，A-Z，0-9(\\转义符)
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

}
