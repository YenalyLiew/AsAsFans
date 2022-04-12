package com.fairhr.module_support.utils;

import android.content.Context;

/**
 * Description:Context提供类
 */
public final class ContextUtil {

    private static Context sContext;

    public static void initContext(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }
}
