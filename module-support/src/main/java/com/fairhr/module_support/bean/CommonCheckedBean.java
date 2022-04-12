package com.fairhr.module_support.bean;

/**
 * Description: 通用选择状态
 */
public class CommonCheckedBean<T> {
    public T data;
    public boolean isChecked;
    public Object tag;

    public CommonCheckedBean(boolean isChecked) {

        this.isChecked = isChecked;
    }

    public CommonCheckedBean(T data) {
        this.data = data;
    }

    public CommonCheckedBean(T data, boolean isChecked) {
        this.data = data;
        this.isChecked = isChecked;
    }

    public CommonCheckedBean(T data, Object tag) {
        this.data = data;
        this.tag = tag;
    }

    public CommonCheckedBean(T data, boolean isChecked, Object tag) {
        this.data = data;
        this.isChecked = isChecked;
        this.tag = tag;
    }
}
