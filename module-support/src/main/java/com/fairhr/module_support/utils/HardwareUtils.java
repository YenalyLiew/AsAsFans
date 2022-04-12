package com.fairhr.module_support.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Author:seven
 * Time:2020/5/25
 * PackageName:com.seven.framework.utils
 * Description:手机硬件信息
 */
public class HardwareUtils {
    //缓存自己生成的设备信息
    private final static Map<String, String> mDeviceInfo = new HashMap<>();

    /**
     * 得到设备唯一识别码
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 得到设备MD5唯一识别码
     *
     * @param context
     * @return
     */
    public static String getAndroidMdId5(Context context) {
        String udid = CommonUtils.getStrToMD5(getAndroidId(context));
        return TextUtils.isEmpty(udid) ? "" : udid;
    }


    /**
     * 获得手机型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获得系统版本号
     *
     * @return
     */
    public static String getDeviceSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 得到设备IMEI值
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!mDeviceInfo.containsKey("DeviceId")
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && tm != null
                && !TextUtils.isEmpty(tm.getDeviceId())
                && !checkHasDeviceInfos(context)) {
            return tm.getDeviceId();
        } else {
            try {
                return getDeviceInfos(context, "DeviceId");
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(e.toString());
            }

        }
        return "";
    }

    /**
     * 得到SIM序列号
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && tm != null && !checkHasDeviceInfos(context)) {
            return tm.getSimSerialNumber();
        } else {
            try {
                String simSerialNumber = getDeviceInfos(context, "SimSerialNumber");
                return simSerialNumber;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 得到手机号码
     *
     * @param context
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "please apply permission";
        }
        return tm != null ? tm.getLine1Number() : null;
    }


    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    private static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (info != null) {
            mac = info.getMacAddress();
        }
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     *
     * @return
     */
    private static String getMacAddress() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : all) {
                if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;
                return networkToMac(networkInterface);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取当前IP的mac地址
     *
     * @return
     */
    public static String getCurrentIpMacAddress(Context context) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(NetWorkManager.getIPAddress(context)));
            return networkToMac(networkInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * networkInterface 中获取mac地址
     *
     * @param networkInterface
     * @return
     * @throws SocketException
     */
    private static String networkToMac(NetworkInterface networkInterface) throws SocketException {
        byte[] macBytes = networkInterface.getHardwareAddress();
        if (macBytes != null && macBytes.length > 0) {
            StringBuilder str = new StringBuilder();
            for (byte b : macBytes) {
                str.append(String.format("%02X:", b));
            }
            if (str.length() > 0) {
                str.deleteCharAt(str.length() - 1);
            }
            return str.toString();
        }
        return "unknown";
    }


    /**
     * 检查是否有自己生成的设备信息
     *
     * @param context
     * @return
     */
    private static boolean checkHasDeviceInfos(Context context) {
        File file = getSaveDeviceInfoFile(context);
        if (file.exists()) {
            String deviceInfos = FileUtils.readFileContent(file.getAbsolutePath());
            return !TextUtils.isEmpty(deviceInfos);
        } else {
            return false;
        }
    }

    /**
     * 获取保存设备信息的文件
     *
     * @param context
     * @return
     */
    private static File getSaveDeviceInfoFile(Context context) {
        String systemRootDir = AndroidFileUtils.getAppSystemFilesDir(context);
        String packageName = context.getPackageName();
        if (packageName.contains("."))
            packageName = packageName.replace(".", "");
        String fileName = packageName;
        File file = FileUtils.createFile(systemRootDir, fileName);
        return file;
    }

    /**
     * 获取设备唯一标示
     *
     * @param context
     * @param deviceInfoType
     */
    private static String getDeviceInfos(Context context, String deviceInfoType) throws IOException {
        if (mDeviceInfo.containsKey(deviceInfoType)) {
            return mDeviceInfo.get(deviceInfoType);
        } else {
            mDeviceInfo.clear();
            File file = getSaveDeviceInfoFile(context);
            String deviceInfos = "";
            if (file.exists()) {
                deviceInfos = FileUtils.readFileContent(file.getAbsolutePath());
            } else {
                file.createNewFile();
            }
            if (TextUtils.isEmpty(deviceInfos)) {
                StringBuilder stringBuilder = new StringBuilder();
                mDeviceInfo.put("DeviceId", buildDeviceUUID(context));
                mDeviceInfo.put("SimSerialNumber", UUID.randomUUID().toString());
                Set<Map.Entry<String, String>> entries = mDeviceInfo.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    stringBuilder.append(entry.getKey() + ":" + entry.getValue() + ";");
                }
                deviceInfos = stringBuilder.substring(0, stringBuilder.length() - 1);
                FileUtils.writeFile(file.getAbsolutePath(), deviceInfos.getBytes());
            } else {
                String[] split = deviceInfos.split(";");
                if (split.length > 0) {
                    for (String deviceInfo : split) {
                        String[] split1 = deviceInfo.split(":");
                        mDeviceInfo.put(split1[0], split1[1]);
                    }
                }
            }
        }
        return mDeviceInfo.get(deviceInfoType);
    }


    /**
     * 创建一个设备id
     *
     * @param context
     * @return
     */
    private static String buildDeviceUUID(Context context) {
        String androidId = getAndroidId(context);
        if (!"9774d56d682e549c".equals(androidId)) {
            Random random = new Random();
            androidId = Integer.toHexString(random.nextInt())
                    + Integer.toHexString(random.nextInt())
                    + Integer.toHexString(random.nextInt());
        }
        return new UUID(androidId.hashCode(), getBuildInfo().hashCode()).toString();
    }

    /**
     * 获取Build的部分信息
     *
     * @return
     */
    public static String getBuildInfo() {
        //这里选用了几个不会随系统更新而改变的值
        StringBuffer buildSB = new StringBuffer();
        buildSB.append(Build.BRAND).append("/");
        buildSB.append(Build.PRODUCT).append("/");
        buildSB.append(Build.DEVICE).append("/");
        buildSB.append(Build.ID).append("/");
        buildSB.append(Build.VERSION.INCREMENTAL);
        return buildSB.toString();
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false; // 没有SIM卡
        }
        return result;
    }

}
