package com.fairhr.module_support;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.router.RouteNavigationPath;

/**
 * Author:kingstar
 * Time:2019-10-21
 * PackageName:com.simu.mine.login
 * Description: 登录拦截器
 */
@Interceptor(name = "login", priority = 1)
public class LoginIInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        boolean isInterceptor = ArouterManager.getInstance().loginInterceptor(postcard.getPath());
        if (isInterceptor) {
            if (UserInfoManager.getInstance().isLogin()) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(new Throwable("未登录进行拦截"));
                Bundle extras = postcard.getExtras();
//                ARouter.getInstance().build(RouteNavigationPath.ModuleMy.MY_PAGE_LOGIN_REGISTER)
//                        .with(extras)
////                        .withString(BundleKeyConstant.Mine.LOGIN_AFTER_AROUT_PATH, postcard.getPath())
//                        .navigation();
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {

    }
}
