package com.fairhr.module_support.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import androidx.lifecycle.MutableLiveData;


import com.fairhr.module_support.tools.inter.NetChangeListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class NetWorkManager {
    public final static int NONE = 0;// 无网络
    public final static int WIFI = 1;// Wi-Fi
    public final static int MOBILE = 2;// 5G,4G，3G,GPRS
    private MutableLiveData<Integer> mNetChangeLiveData = new MutableLiveData<>();
    private volatile static NetWorkManager sNetWorkManager;
    private Set<NetChangeListener> mNetChangeListners = new CopyOnWriteArraySet<>();
    private int mCurrentNetType;

    public void setCurrentNetType(int currentNetType) {
        if (currentNetType != mCurrentNetType)
            mCurrentNetType = currentNetType;
    }

    public int getCurrentNetType() {
        return mCurrentNetType;
    }

    public MutableLiveData<Integer> getNetChangeLiveData() {
        return mNetChangeLiveData;
    }

    public static NetWorkManager getNetWorkManager() {
        return sNetWorkManager;
    }

    private NetWorkManager() {

    }

    public static NetWorkManager getInstance() {
        if (sNetWorkManager == null) {
            synchronized (NetWorkManager.class) {
                if (sNetWorkManager == null) {
                    sNetWorkManager = new NetWorkManager();
                }
            }
        }
        return sNetWorkManager;
    }

    //监听网络状态变更
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()) ||
                    WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction()) ||
                    WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                networkChange(getNetworkState(context));
            }
        }
    };

    /**
     * 通知网络状态发生改变
     *
     * @param netWorkState
     */
    private void networkChange(int netWorkState) {
        mNetChangeLiveData.postValue(netWorkState);
        if (mNetChangeListners != null && mNetChangeListners.size() > 0) {
            for (NetChangeListener netChangeListner : mNetChangeListners) {
                if (netChangeListner != null) {
                    if (netWorkState != mCurrentNetType) {
                        mCurrentNetType = netWorkState;
                        netChangeListner.onChange(netWorkState);
                    }
                }
            }
        }
    }

    /**
     * 设置网络监听
     *
     * @param activity
     * @param netChangeListner
     */
    public void addNetChangeListner(Activity activity, NetChangeListener netChangeListner) {
        mNetChangeListners.add(netChangeListner);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        activity.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /**
     * 取消网络监听
     */
    public void removeNetChangeListner(Activity activity, NetChangeListener netChangeListner) {
        activity.unregisterReceiver(mBroadcastReceiver);
        if (mNetChangeListners.contains(netChangeListner))
            mNetChangeListners.remove(netChangeListner);
    }


    /**
     * 获取当前网络状态(wifi,3G)
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static int getNetworkState(Context context) {
        if (context == null) {
            return NONE;
        }
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null ||
                connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null ||
                connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null) {
            return NONE;
        }
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return MOBILE;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return WIFI;
        }
        return NONE;

    }

    /**
     * 检测是否有网络
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo network : info) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否有可用的wifi
     *
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifiAvailable(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getNetworkTypeName(Context context) {
        if (context != null) {
            ConnectivityManager connectMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectMgr != null) {
                NetworkInfo info = connectMgr.getActiveNetworkInfo();
                if (info != null) {
                    switch (info.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            return "WIFI";
                        case ConnectivityManager.TYPE_MOBILE:
                            return getNetworkTypeName(info.getSubtype());
                    }
                }
            }
        }
        return getNetworkTypeName(TelephonyManager.NETWORK_TYPE_UNKNOWN);
    }

    /**
     * 获取网络类型
     *
     * @param type
     * @return
     */

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * 获取ip地址
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {    // 当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {    // 当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                // 得到IPV4地址
                return intIP2StringIP(wifiInfo.getIpAddress());
            }
        } else {
            // 当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


}
