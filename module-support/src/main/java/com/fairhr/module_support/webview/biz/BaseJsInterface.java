package com.fairhr.module_support.webview.biz;

import android.app.Activity;

import com.fairhr.module_support.webview.agent.AgentWeb;

/**
 * class description
 *
 * @author ysw
 * @version 1.0.0
 * @date 2021-01-20
 */
public class BaseJsInterface {
    //JAVA调用JS的固定方法
    public static final String CALL_JS_METHOD = "nativeCallback";

    public Activity context;
    public AgentWeb agent;

    public BaseJsInterface(Activity context, AgentWeb agent) {
        this.context = context;
        this.agent = agent;
    }
}
