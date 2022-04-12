package com.fairhr.module_support.tools.inter;

/**
 * Description: 系统键盘显示隐藏监听
 */
public interface ISysKeyBoardListener {
    /**
     * 系统打开关闭改变
     *
     * @param isOpen true打开 false关闭
     */
    void onSoftKeyBoardChange(boolean isOpen, int keyHeight);
}
