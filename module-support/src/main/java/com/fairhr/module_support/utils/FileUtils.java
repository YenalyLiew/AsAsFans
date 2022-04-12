package com.fairhr.module_support.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.fairhr.module_support.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class FileUtils {

    public static String getFromAssets(Context context, String fileName, String separator){
        String result = "";
        BufferedReader br = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            br = new BufferedReader(
                    new InputStreamReader(
                            context.getResources().getAssets().open(fileName)
                    )
            );
            String line = null;
            while (br.readLine() != null){
                stringBuilder.append(line);
                if (!TextUtils.isEmpty(separator)) {
                    stringBuilder.append(separator);
                }
            }

            result = stringBuilder.toString();
            if (!TextUtils.isEmpty(separator) && result.length() > separator.length()) {
                result = result.substring(0, result.length() - separator.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (Exception e1) {
                    //do nothing
                }
            }
        }
        return result;
    }

    public static File getUriForFile(Context context, Uri uri){
        String result;
        Cursor cursor = context.getContentResolver().query(
                uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                null, null, null
        );
        if (cursor == null)
            result = uri.getPath().toString();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return new File(result);
    }

    public static Uri getFileUri(Context context, File file){
        return FileProvider.getUriForFile(context,
                context.getString(R.string.support_file_provider_authorities),
                file);
    }

    public static void saveFile2Gallery(Context context,File file) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(),
                    file.getName(),
                    null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = getFileUri(context, file);
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 保存到系统相册
     * @date        2021/5/10
     * @author      ysw
     * @param
     * @return
     */
    public static File saveBitmap2Gallery(Context context, Bitmap bmp,String title){
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, title, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse(path);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return getUriForFile(context, uri);
    }

    public static String saveBitmap2File(Bitmap bitmap, String filePath){
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if(parentFile == null || !parentFile.exists()){
            parentFile.mkdirs();
        }

        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {

        }
        return filePath;
    }


    /**
     * 指定文件夹创建文件
     *
     * @param folderPath 文件夹path
     * @param fileName   文件名
     * @return file
     */
    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件name
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";
        int point = 0;
        if (fileName.contains(".")) {
            point = fileName.lastIndexOf('.');
        } else {
            return "";
        }
        return fileName.substring(point + 1);
    }

    /**
     * 根据文件绝对路径获取文件名（带扩展名）
     *
     * @param filePath 文件path
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件绝对路径获取文件名（带扩展名）
     *
     * @param filePath 文件path
     * @return 文件名
     */
    public static String getFileParentPath(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param filePath 文件path
     * @return file name
     */
    public static String getFileNameNoFormat(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
                point);
    }


    /**
     * 获取文件大小
     *
     * @param filePath 文件path
     * @return 文件大小
     */
    public static long getFileSize(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileSize 文件大小
     * @return B/KB/MB/GB 返回合适的大小字符串
     */
    public static String formatFileSize(long fileSize) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param fileFolder 文件夹
     * @return 所有文件大小
     */
    public static long getDirSize(File fileFolder) {
        if (fileFolder == null) {
            return 0;
        }
        if (!fileFolder.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = fileFolder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件个数
     *
     * @param fileFolder 文件夹
     * @return 文件数量
     */
    public long getFileList(File fileFolder) {
        long count = 0;
        File[] files = fileFolder.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }

    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context
     * @param fileName
     */
    public static void writeToDataFile(Context context, String fileName, String content) {
        if (content == null)
            content = "";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readDataFile(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字节流转文本
     *
     * @param inStream 读取字节流
     * @return
     */
    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return "";
    }

    /**
     * 读取指定文本文件的内容
     *
     * @param filePath
     * @return
     */
    public static String getFileString(String filePath) {
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return readInStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param filePath 文件path
     * @return 删除结果
     */
    public static boolean deleteDirectory(String filePath) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!TextUtils.isEmpty(filePath)) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + listfile[i].toString());
                        deletedFile.delete();
                    }
                    newPath.delete();
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param filePath 文件path
     * @return 删除结果
     */
    public static boolean deleteDirectoryFiles(String filePath) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!TextUtils.isEmpty(filePath)) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + listfile[i].toString());
                        deletedFile.delete();
                    }
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件path
     * @return 删除结果
     */
    public static boolean deleteFile(String filePath) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!TextUtils.isEmpty(filePath)) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 重命名
     *
     * @param oldPath 旧地址
     * @param newPath 新地址
     * @return 结果
     */
    public static boolean reNamePath(String oldPath, String newPath) {
        File f = new File(oldPath);
        return f.renameTo(new File(newPath));
    }

    /**
     * 删除文件
     *
     * @param filePath 文件Path
     */
    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File file = new File(filePath);
        checker.checkDelete(filePath);
        if (file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 清空一个文件夹
     *
     * @param filePath path
     */
    public static void clearFileWithPath(String filePath) {
        List<File> files = listPathFiles(filePath);
        if (files.isEmpty()) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                clearFileWithPath(f.getAbsolutePath());
            } else {
                f.delete();
            }
        }
    }

    /**
     * 获取一个文件夹下的所有文件
     *
     * @param root 根目录
     * @return
     */
    public static List<File> listPathFiles(String root) {
        List<File> allDir = new ArrayList<File>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        File[] files = path.listFiles();
        for (File f : files) {
            if (f.isFile())
                allDir.add(f);
            else
                listPath(f.getAbsolutePath());
        }
        return allDir;
    }

    /**
     * 列出root目录下所有子目录
     *
     * @param root 根目录
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        // 过滤掉以.开始的文件夹
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    /**
     * 从url中获取file
     *
     * @param context context
     * @param uri     uri
     * @return
     */
    public static File getFileByUri(Context context, Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
                cursor.close();
            }
            if (!TextUtils.isEmpty(path)) {
                return new File(path);
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Android 10文件转存到内部文件中
     *
     * @param context
     * @param uri
     * @param type
     * @param maxSize 0不进行压缩
     * @return
     */
    public static Observable<String> sdkQUriToFilePath(final Context context, final Uri uri, final String type, final long maxSize) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                String sdAppFileDir = AndroidFileUtils.getSDAppFileDir(context, type);
                File fileByUri = getFileByUri(context, uri);
                String fileName = FileUtils.getFileName(fileByUri.getAbsolutePath());
                File file = new File(sdAppFileDir, fileName);
                FileUtils.writeToFile(file.getAbsolutePath(), inputStream);
                if (maxSize == 0) {
                    emitter.onNext(file.getAbsolutePath());
                    emitter.onComplete();
                } else {
                    String filePath = BitmapUtils.compressToAssignSize(context, file.getAbsolutePath(), maxSize);
                    emitter.onNext(filePath);
                    emitter.onComplete();
                }
            }
        });

    }


    /**
     * 保存文件
     *
     * @param filePath 文件地址
     * @param bytes    字节
     */
    public static void saveFile(String filePath, byte[] bytes) {
        try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     *
     * @param filePath    文件地址
     * @param inputStream 读取字节流
     * @return
     */
    public static boolean writeToFile(String filePath, InputStream inputStream) {
        int byteCount = 0;
        byte[] bytes = new byte[1024];
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, byteCount);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 读取asset文本文件中的字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssetData(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 文件转字节
     *
     * @param filename
     * @return
     */
    public static byte[] readFile(String filename) throws FileNotFoundException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            // throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 复制文件
     *
     * @param srcPath 源文件地址
     * @param tarPath 目标文件地址
     * @return 复制结果
     */
    public static boolean copyFile(String srcPath, String tarPath) {
        File srcFile = new File(srcPath);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }
        // 判断目标文件是否存在
        File destFile = new File(tarPath);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖

            // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
            new File(tarPath).delete();

        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }
        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param filePaths
     * @param zipFile
     * @return
     */
    public static Observable<String> zipFiles(final List<String> filePaths, final boolean deleteSrcFiles, final String zipFile) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //创建ZIP
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
                for (String filePath : filePaths) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        ZipEntry zipEntry = new ZipEntry(FileUtils.getFileName(filePath));
                        FileInputStream inputStream = new FileInputStream(filePath);
                        zipOutputStream.putNextEntry(zipEntry);
                        int len;
                        byte[] buffer = new byte[4096];
                        while ((len = inputStream.read(buffer)) != -1) {
                            zipOutputStream.write(buffer, 0, len);
                        }
                        zipOutputStream.closeEntry();
                    }
                }
                //完成和关闭
                zipOutputStream.finish();
                zipOutputStream.close();
                if (deleteSrcFiles) {
                    for (String file : filePaths) {
                        deleteFile(file);
                    }
                }
                emitter.onNext(zipFile);
                emitter.onComplete();
            }
        });

    }


    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
        //完成和关闭
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
            }
        }
    }

    /**
     * 生成图片文件名
     *
     * @param imageFormat
     * @return
     */
    public static String creatImageFileName(String imageFormat) {
        return "IMG" + DateUtil.formLocalTime("yyyyMMddhhmmss", System.currentTimeMillis()) + "." + imageFormat;
    }

    /**
     * 读取指定文本文件的内容
     *
     * @param filePath
     * @return
     */
    public static String readFileContent(String filePath) {
        String encoding = "utf-8";
        File file = new File(filePath);
        long fileLength = file.length();
        byte[] fileContent = new byte[(int) fileLength];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
//            GJLogger.logDiskAndConsole(e.getMessage());
            return null;
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
//            GJLogger.logDiskAndConsole(e.getMessage());
            return null;
        }
    }

    /**
     * 保存文件
     *
     * @param filePath 文件地址
     * @param bytes    字节
     */
    public static void writeFile(String filePath, byte[] bytes) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
