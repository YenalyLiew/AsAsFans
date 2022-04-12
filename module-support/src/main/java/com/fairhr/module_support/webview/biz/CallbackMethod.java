package com.fairhr.module_support.webview.biz;

import android.text.TextUtils;


import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.webview.agent.AgentWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * class description
 *
 * @author ysw
 * @version 1.0.0
 * @date 2021-01-19
 */
public class CallbackMethod {

    private String flowNo;

    private Map<String, String> params;

    public Map<String, String> getParams() {
        return params;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public CallbackMethod(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                LogUtil.d(AgentWeb.TAG, "CallbackMethod: " + content);
                JSONObject jsonObject = new JSONObject(content);
                flowNo = jsonObject.optString("flowNo");
                JSONObject paramObj = jsonObject.optJSONObject("param");
                if (paramObj != null) {
                    Iterator<String> it = paramObj.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (params == null)
                            params = new HashMap<>();
                        params.put(key, paramObj.optString(key));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
