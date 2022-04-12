package com.fairhr.module_support.config;

import com.fairhr.module_support.utils.ContextUtil;
import com.fairhr.module_support.utils.GsonUtils;
import com.fairhr.module_support.utils.SPreferenceUtils;

/**
 * Describe:工具模块配置管理类
 */
public class ToolsConfigManager implements IToolsConfig {
    private static final String DISK_CACHE_CONFIG = "disk_cache_config";
    private volatile static ToolsConfigManager sToolsConfigManager;
    private IToolsConfig mIToolsConfig;

    private ToolsConfigManager() {
    }

    public static ToolsConfigManager getInstance() {
        if (sToolsConfigManager == null) {
            synchronized (ToolsConfigManager.class) {
                if (sToolsConfigManager == null) {
                    sToolsConfigManager = new ToolsConfigManager();
                }
            }
        }
        return sToolsConfigManager;
    }

    /**
     * 注入tool配置
     *
     * @param iToolsConfig
     */
    public void injectIToolsConfig(IToolsConfig iToolsConfig) {
        mIToolsConfig = iToolsConfig;
        SPreferenceUtils.write(ContextUtil.getContext(),DISK_CACHE_CONFIG, GsonUtils.toJson(mIToolsConfig));
    }

    /**
     * 获取注入的配置
     *
     * @return
     */
    private IToolsConfig getIToolsConfig() {
        if (mIToolsConfig != null) {
            return mIToolsConfig;
        }
        return new DefaultIToolsConfig();
    }


    @Override
    public String getFileprovider() {
        return getIToolsConfig().getFileprovider();
    }

    @Override
    public boolean isOpenLogcat() {
        return getIToolsConfig().isOpenLogcat();
    }

    @Override
    public String getLogcatTag() {
        return getIToolsConfig().getLogcatTag();
    }

    @Override
    public boolean isToastOpenLog() {
        return getIToolsConfig().isToastOpenLog();
    }

    @Override
    public boolean isToastAllowQueue() {
        return getIToolsConfig().isToastAllowQueue();
    }

    @Override
    public String getSPFileName() {
        return getIToolsConfig().getSPFileName();
    }
}
