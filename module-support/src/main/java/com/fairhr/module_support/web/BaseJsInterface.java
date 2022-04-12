package com.fairhr.module_support.web;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.fairhr.module_support.R;
import com.fairhr.module_support.ThreadUtils;
import com.fairhr.module_support.UserInfoManager;
import com.fairhr.module_support.base.BaseWebViewFragment;
import com.fairhr.module_support.base.KsFragmentActivity;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.core.ApplicationManager;
import com.fairhr.module_support.network.ErsNetManager;
import com.fairhr.module_support.router.RouteUtils;
import com.fairhr.module_support.tools.inter.ErsDataObserver;
import com.fairhr.module_support.tools.inter.IUserInfoProvide;
import com.fairhr.module_support.utils.ContextUtil;
import com.fairhr.module_support.utils.GsonUtils;
import com.fairhr.module_support.utils.HardwareUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.utils.SystemAppUtil;
import com.fairhr.module_support.utils.ToastUtils;
import com.fairhr.module_support.utils.UrlUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:kingstar
 * Time:2019-09-24
 * PackageName:com.kingstar.ksframework.webview
 * Description:常用JS指令
 */
public class BaseJsInterface {
    //通知H5状态栏高度js
    public static final String INJECT_STATUS_BAR_HEIGHT = "javascript:var h5InitData = {}; h5InitData.status_bar_padding = %s";
    //  JS接口名称
    public static final String JS_INTERFACE_NAME = "andorid";
    private Context mContext;
    private WebView mWebView;
    private String[] mDataList = new String[]{};
    String Remark = null;
    String Reason = null;

    private  CharSequence temp = null;
    private int editStart = 0;
    private int editEnd = 0;
    private int countall;
    private String title;


    public BaseJsInterface(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
    }

    /**
     * 跳转客服
     * @param phone
     */
    @JavascriptInterface
    public void callPhone(String phone){
        LogUtil.d("phone=:"+phone);
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * @param type
     */
    @JavascriptInterface
    public void chooseBtn( String type){
        LogUtil.d("type111=:"+type);

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * 跳转问题
     */
    @JavascriptInterface
    public void toPage(String id){
        if(!TextUtils.isEmpty(id)){
            String name = null;
            IUserInfoProvide iUserInfoProvide = UserInfoManager.getInstance().getIUserInfoProvide();
            if (iUserInfoProvide != null){
                name = iUserInfoProvide.privateUserId();
            }else{
                name = "";
            }
            String[] strs = id.split("-");
            Map<String,String> extraMap = new HashMap<>();
            String deviceInfos = HardwareUtils.getDeviceId(ContextUtil.getContext());
            extraMap.put("QuestionId",strs[0]);
            extraMap.put("UserId",name);
            extraMap.put("Ip",deviceInfos);
            //RouteUtils.openWebview(UrlUtils.formatUrl(ServiceConstants.HELP_CENTER_URL, extraMap),strs[1]);
        }

    }




}
