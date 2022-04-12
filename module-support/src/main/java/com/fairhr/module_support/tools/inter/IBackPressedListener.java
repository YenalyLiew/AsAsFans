package com.fairhr.module_support.tools.inter;

/**
 * Description:监听返回键 主要用于activity回调到Fragment
 */
public interface IBackPressedListener {
    /**
     * 点击了物理返回键
     */
    boolean onBackPressed();
}
