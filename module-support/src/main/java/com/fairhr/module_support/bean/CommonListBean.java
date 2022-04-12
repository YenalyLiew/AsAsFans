package com.fairhr.module_support.bean;

public class CommonListBean{

    public String content;
    public boolean checked;
    private String type;

    public CommonListBean() {

    }

    public CommonListBean(String content, boolean checked,String type) {
        this.content = content;
        this.checked = checked;
        this.type = type;
    }
}
