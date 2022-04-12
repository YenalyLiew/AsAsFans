package com.fairhr.module_support.core;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fairhr.module_support.config.IToolsConfig;
import com.fairhr.module_support.config.ToolsConfigManager;
import com.fairhr.module_support.utils.ContextUtil;

/**
 * @Description: 框架配置管理类
 */
public class FrameConfigManager<T extends IConfigInit> {
    private volatile static FrameConfigManager sFrameConfigManager;
    private boolean mInitArouter;
    private IConfigInit mIConfigInit;

    /**
     * 是否初始化Arouter
     *
     * @return
     */
    public boolean isInitArouter() {
        return mInitArouter;
    }

    /**
     * 设置的初始化配置
     *
     * @return
     */
    public IConfigInit getIConfigInit() {
        return mIConfigInit;
    }

    /**
     * 初始化本地配置
     *
     * @return
     */
    public IToolsConfig getIToolsConfig() {
        return ToolsConfigManager.getInstance();
    }


    private FrameConfigManager() {
    }

    public static FrameConfigManager getInstance() {
        if (sFrameConfigManager == null) {
            synchronized (FrameConfigManager.class) {
                if (sFrameConfigManager == null) {
                    sFrameConfigManager = new FrameConfigManager();
                }
            }
        }
        return sFrameConfigManager;
    }

    /**
     * 初始化
     *
     * @param application  Application
     * @param iToolsConfig 工具库配置
     * @return
     */
    public FrameConfigManager<T> init(@NonNull Application application, IToolsConfig iToolsConfig) {
        ///初始化系统context
        ContextUtil.initContext(application.getApplicationContext());
        //监听所有activity的生命周期
        application.registerActivityLifecycleCallbacks(ApplicationManager.getInstance());
        if (iToolsConfig != null)
            ToolsConfigManager.getInstance().injectIToolsConfig(iToolsConfig);
        return this;
    }

    /**
     * 其他初始化
     *
     * @param application
     * @param iConfigInit
     */
    public void otherInit(@NonNull Application application, T iConfigInit) {
        mInitArouter = true;

        if (iConfigInit != null) {
            iConfigInit.initLocalConfig(application);
            iConfigInit.initThreeFrame(application);
        }
    }
}
