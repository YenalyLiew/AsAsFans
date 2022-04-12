package com.fairhr.module_support.utils;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 */
public class UrlUtils {
    /**
     * 获取url的端口号
     *
     * @param url
     * @return
     */
    public static int formatUrlToPort(String url) {
        int port = 80;
        String regexString = "\\:\\d{1,5}";
        Pattern p = Pattern.compile(regexString);
        Matcher m = p.matcher(url);
        boolean result = m.find();
        if (result) {
            String temp = m.group();
            temp = temp.replace(":", "");
            return Integer.parseInt(temp);
        }
        return port;
    }

    /**
     * 获取url的域名
     *
     * @param url
     * @return
     */
    public static String formatUrlToHost(String url) {
        String host = formatUrlToIp(url);
        if (host == null) {
            host = formatUrlToDomain(url);
        }
        return host;
    }

    /**
     * 从url从分离ip地址
     *
     * @param url
     * @return
     */
    public static String formatUrlToIp(String url) {
        String regexString = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        Pattern p = Pattern.compile(regexString);
        Matcher m = p.matcher(url);
        boolean result = m.find();
        if (result) {
            return m.group();
        }
        return null;
    }

    /**
     * 获取url的域名
     *
     * @param url
     * @return
     */
    public static String formatUrlToDomain(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http") && !url.startsWith("https")) {
                url = "http://" + url;
            }
            try {
                URL urlTest = new URL(url);
                return urlTest.getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取url的协议
     *
     * @param url
     * @return
     */
    public static String getUrlProtocol(String url) {
        String protocol = "http";
        if (!TextUtils.isEmpty(url)) {
            try {
                URL urlTest = new URL(url);
                protocol = urlTest.getProtocol();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return protocol;
    }

    /**
     * 把url格式化为host+ip的形式
     *
     * @return
     */
    public static String formatUrlToKey(String url) {
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        int port = uri.getPort();
        if (port < 0) {
            port = 80;
        }
        String key = host + ":" + port;
        return key;
    }

    /**
     * 格式化url
     *
     * @param baseUrl   基本url
     * @param extraData 额外数据
     * @return 完成url
     */
    public static String formatUrl(@NonNull String hostUrl, @NonNull String baseUrl, Map<String, ? extends Object> extraData) {
        if (TextUtils.isEmpty(hostUrl) || TextUtils.isEmpty(baseUrl))
            return "";
        if (baseUrl.contains("http"))
            hostUrl = "";
        if (!TextUtils.isEmpty(hostUrl) && hostUrl.lastIndexOf("/") == hostUrl.length() - 1 &&
                baseUrl.charAt(0) == '/') {
            hostUrl = hostUrl.substring(0, hostUrl.length() - 1);
        } else if (!TextUtils.isEmpty(hostUrl) && hostUrl.lastIndexOf("/") != hostUrl.length() - 1 &&
                baseUrl.charAt(0) != '/') {
            baseUrl = "/" + baseUrl;
        }
        String allUrl;
        String urlParams;
        if (extraData != null) {
            urlParams = getParams(extraData);
        } else {
            urlParams = "";
        }
        allUrl = hostUrl + baseUrl;
        if (allUrl.contains("?")) {
            if (allUrl.lastIndexOf("?") == (allUrl.length() - 1)) {
                allUrl = allUrl + urlParams;
            } else {
                allUrl = allUrl + "&" + urlParams;
            }
        } else {
            if (!TextUtils.isEmpty(urlParams)) {
                allUrl = allUrl + "?" + urlParams;
            }
        }
        return allUrl;
    }


    /**
     * 格式化url
     *
     * @param baseUrl   基本url
     * @param extraData 额外数据
     * @return 完成url
     */
    public static String formatUrl(String baseUrl, Map<String, String> extraData) {
        if (TextUtils.isEmpty(baseUrl))
            return "";
        String allUrl;
        String urlParams;
        if (extraData != null) {
            urlParams = getParams(extraData);
        } else {
            urlParams = "";
        }
        if (baseUrl.contains("?")) {
            if (baseUrl.lastIndexOf("?") == (baseUrl.length() - 1)) {
                allUrl = baseUrl + urlParams;
            } else {
                allUrl = baseUrl + "&" + urlParams;
            }
        } else {
            if (TextUtils.isEmpty(urlParams)) {
                allUrl = baseUrl;
            } else {
                allUrl = baseUrl + "?" + urlParams;
            }
        }
        return allUrl;
    }

    /**
     * 得到参数列表字符串
     *
     * @param paramValues 参数map对象
     * @return 参数列表字符串
     */
    public static String getParams(Map<String, ? extends Object> paramValues) {
        String params = "";
        Set<String> key = paramValues.keySet();
        String beginLetter = "";
        for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
            String s = (String) it.next();
            if (params.equals("")) {
                params += beginLetter + s + "=" + paramValues.get(s);
            } else {
                params += "&" + s + "=" + paramValues.get(s);
            }
        }
        return params;
    }

    /**
     * url 参数转map形式
     *
     * @param paramsString
     * @return
     */
    public static Map<String, String> paramsToMap(String paramsString) {
        Map<String, String> paramsMap = new HashMap<>();
        if (!TextUtils.isEmpty(paramsString)) {
            if (paramsString.contains("&")) {
                String[] allParams = paramsString.split("&");
                for (String allParam : allParams) {
                    if (allParam.contains("=")) {
                        String[] param = allParam.split("=");
                        paramsMap.put(param[0], param[1]);
                    }
                }
            } else {
                if (paramsString.contains("=")) {
                    String[] param = paramsString.split("=");
                    paramsMap.put(param[0], param[1]);
                }
            }
        }
        return paramsMap;
    }

    /**
     * 根据文件绝对路径获取文件名（带扩展名）
     *
     * @param filePath 文件path
     * @return 文件名
     */
    public static String getUrlFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        try {
            filePath = URLDecoder.decode(filePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (filePath.contains("?")) {
            String getUrl = filePath.substring(0, filePath.indexOf("?"));
            return getUrl.substring(getUrl.lastIndexOf(File.separator) + 1);
        } else {
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        }

    }


}
