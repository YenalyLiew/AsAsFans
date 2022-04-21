package com.fairhr.module_support.router;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.constants.SpConstants;

public class RouteUtils {



    /**
     * @description 打开web页
     * @param
     * @return
     */
    public static void openWeb(String url) {
        ARouter.getInstance().build(RouteNavigationPath.ModuleSupport.SUPPORT_WEB)
                .withString("url", url)
                .navigation();

    }



    /**
     * 打开H5
     * @param url           H5url
     */
    public static void openWebview(String url, String title) {

            ARouter.getInstance().build(RouteNavigationPath.ModuleSupport.KS_FRAGMENT_ACTIVITY)
                    .withString(SpConstants.FRAGMENT_ACT_AROUTPATH, RouteNavigationPath.ModuleSupport.KS_WEB_VIEW)
                    .withString("url", url)
                    .withString("title", title)
                    .navigation();
    }

}
