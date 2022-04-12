package com.fairhr.module_support.core;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 初始化回调接口
 */
public abstract class IConfigInit {
    //一会缓存配置，备用
    protected Map<Object, Object> mCacheData = new HashMap<>();

    //放置缓存配置
    public void putCacheData(Object key, Object value) {
        mCacheData.put(key, value);
    }

    //过去缓存配置
    public Object getCacheData(Object key) {
        return mCacheData.get(key);
    }

    /**
     * 初始化本地配置
     *
     * @param application
     */
    public abstract void initLocalConfig(Application application);

    /**
     * 初始化三方框架
     *
     * @param application
     */
    public abstract void initThreeFrame(Application application);

}
