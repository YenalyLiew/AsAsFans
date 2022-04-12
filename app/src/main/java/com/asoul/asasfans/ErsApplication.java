package com.asoul.asasfans;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.base.BaseApplication;
import com.fairhr.module_support.utils.AppUtils;
import com.fairhr.module_support.utils.ContextUtil;
import com.fairhr.module_support.utils.ProcessUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;


public class ErsApplication extends BaseApplication {

    /**
     * 基础模块在此初始化，和业务功能相关的到各自相关模块进行初始化
     * Note：获取手机信息或者网络请求等初始化操作不可放在这里
     */
    @Override
    public void onCreate() {
        //因为推送有个单独的进程，因此为了避免多次初始化，所以添加主进程判断
        if (ProcessUtils.isMainProcess(this)) {
            ContextUtil.initContext(getApplicationContext());
            super.onCreate();
            //Dex分包初始化
//            MultiDex.install(this);
            //Debug模式判断，Module中可用
            AppUtils.syncIsDebug(this);
            //DataStore初始化
//            DataStoreUtil.init(this)
            //ARouter初始化
            initARouter(this);
            //LiveEventBus初始化，替代EventBus
            initLiveEventBus();

        }
    }

    /**
     * 初始化ARouter
     */
    private void initARouter(BaseApplication application) {
        //文档说 必须在初始化之前调用，负责无效
        if (AppUtils.isDebug) {
            /**
             * ARouter加载Dex中的映射文件会有一定耗时，所以ARouter会缓存映射文件，直到新版本升级(版本号或者versionCode变化)，
             * 而如果是开发版本(ARouter.openDebug())， ARouter 每次启动都会重新加载映射文件，开发阶段一定要打开 Debug 功能
             */
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);

    }

    /**
     * 初始化LiveEventBus
     */
    private void initLiveEventBus() {
        LiveEventBus.config()
                .enableLogger(AppUtils.isDebug)
                .autoClear(true)
                .lifecycleObserverAlwaysActive(true);
    }


}
