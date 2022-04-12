package com.fairhr.module_support.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.io.File;

/**
 * Description:Android系统文件
 */
public class AndroidFileUtils {
    /**
     * 获取系统根目录
     *
     * @return /data
     */
    public static String getSystemRootDir() {
        return Environment.getDataDirectory().getAbsolutePath();
    }


    /**
     * 获取app系统 Files目录
     *
     * @param context
     * @return /data/data/包名/files
     */
    public static String getAppSystemFilesDir(Context context) {
        return addSplte(context.getFilesDir().getAbsolutePath());
    }

    /**
     * 删除app系统 file
     *
     * @param context
     * @param fileName 文件名
     * @return true/false
     */
    public static boolean deleteAppSystemFile(Context context, String fileName) {
        return context.deleteFile(fileName);
    }

    /**
     * 获取app系统files文件
     *
     * @param context
     * @return
     */
    public static String[] getAppSysetemFiles(Context context) {
        return context.fileList();
    }

    /**
     * 获取app系统缓存目录
     *
     * @param context
     * @return /data/data/包名/cache
     */
    public static String getAppSystemCacheDir(Context context) {
        return addSplte(context.getCacheDir().getAbsolutePath());
    }

    /**
     * 获取app系统数据目录
     *
     * @param context
     * @return /data/data/包名/databases
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getAppSystemDBDir(Context context) {
        File dataDir = context.getDataDir();
        return addSplte(dataDir.exists() ? dataDir.getAbsolutePath() : "");
    }

    /**
     * 获取app系统数据库文件
     *
     * @param context
     * @param dbName  数据文件名
     * @return
     */
    public static String getAppSystemDBFile(Context context, String dbName) {
        File databasePath = context.getDatabasePath(dbName);
        return databasePath.exists() ? databasePath.getAbsolutePath() : "";
    }

    /**
     * 删除app系统数据库文件
     *
     * @param context
     * @param dbName  数据文件名
     * @return
     */
    public static boolean deleteAppSystemDBFile(Context context, String dbName) {
        return context.deleteDatabase(dbName);
    }

    /**
     * 获取sd卡的状态
     *
     * @return
     */
    public static String getSDCardState() {
        return Environment.getExternalStorageState();
    }

    /**
     * SD卡是否存在
     *
     * @return SD卡是否存在
     */
    public static boolean isSdCardExists() {
        return Environment.MEDIA_MOUNTED.equals(getSDCardState());
    }

    /**
     * 获取手机SD更目录
     *
     * @return /storage/emulated/0
     */
    public static String getSDCardRootDir() {
        return addSplte(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 获取SD卡app文件目录
     *
     * @param context
     * @param type    文件类型 空字符串返回根目录，不存在的type创建，系统存在目录返回
     * @return /storage/emulated/0/Android/data/应用包名/files
     */
    public static String getSDAppFileDir(Context context, String type) {
        File externalFilesDir = context.getExternalFilesDir(type);
        return addSplte(externalFilesDir.exists() ? externalFilesDir.getAbsolutePath() : "");
    }

    /**
     * 获取SD卡公共文件目录
     *
     * @param type 空字符串返回根目录，不存在的type创建，系统存在目录返回
     * @return sd卡目录
     */
    public static String getSDCardPublicDir(String type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File file = new File(getSDCardRootDir(), type);
            if (!file.exists())
                file.mkdir();
            return addSplte(file.getAbsolutePath());
        } else {
            return addSplte(Environment.getExternalStoragePublicDirectory(type).getAbsolutePath());
        }
    }

    /**
     * 获取SD卡app cache目录
     *
     * @param context
     * @return
     */
    public static String getSDCadeCacheDir(Context context) {
        return addSplte(context.getExternalCacheDir().getAbsolutePath());
    }

    /**
     * 文件添加/
     *
     * @param filePath
     * @return
     */
    private static String addSplte(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            if (filePath.lastIndexOf(File.separator) == filePath.length() - 1) {
                return filePath;
            } else {
                return filePath + File.separator;
            }
        }
    }
}
