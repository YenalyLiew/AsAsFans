package com.fairhr.module_support;


import java.util.HashMap;
import java.util.Map;

public class ModuleConfig {

    public static Map<String,String> JS_INTERFACES = new HashMap<>();


    public static Map<String,String> getJS_INTERFACES(){
        //需要注入的JsInterface
        JS_INTERFACES.put("app","com.fairhr.module_support.webview.biz.SupportJsInterface");
        JS_INTERFACES.put("help","com.fairhr.module_mine.viewmodel.WebProblemInterface");
        return JS_INTERFACES;
    }

}
