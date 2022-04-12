package com.fairhr.module_support.config;


import com.fairhr.module_support.BuildConfig;
import com.fairhr.module_support.utils.ContextUtil;

/**
 * PackageName:com.kingstar.kstools.config
 * User: wangbin
 * Date: 11/18/20
 * Describe:默认实现配置
 */
public class DefaultIToolsConfig implements IToolsConfig {
    @Override
    public String getFileprovider() {
        return "";
    }

    @Override
    public boolean isOpenLogcat() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getLogcatTag() {
        return "kslog";
    }

    @Override
    public boolean isToastOpenLog() {
        return BuildConfig.DEBUG;
    }

    @Override
    public boolean isToastAllowQueue() {
        return false;
    }

    @Override
    public String getSPFileName() {
        if (ContextUtil.getContext() == null) {
            return "KSCache";
        } else {
            String packageName = ContextUtil.getContext().getPackageName();
            return packageName.substring(packageName.lastIndexOf("."));
        }
    }
}
