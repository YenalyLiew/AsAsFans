package com.fairhr.module_support.config;

/**
 * Describe:工具库配置类
 */
public interface IToolsConfig {

    /**
     * android文件提供者
     *
     * @return
     */
    String getFileprovider();

    /**
     * 是否开启日志
     *
     * @return
     */
    boolean isOpenLogcat();

    /**
     * 通用日志标签
     *
     * @return
     */
    String getLogcatTag();

    /**
     * 是否打开toast日志
     *
     * @return
     */
    boolean isToastOpenLog();

    /**
     * toast是否以队列的方式打开
     *
     * @return
     */
    boolean isToastAllowQueue();

    /**
     * sp缓存文件名
     *
     * @return
     */
    String getSPFileName();


}
