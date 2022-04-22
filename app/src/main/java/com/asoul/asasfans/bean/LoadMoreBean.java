package com.asoul.asasfans.bean;

import com.asoul.asasfans.entity.HistorySearchEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class LoadMoreBean implements MultiItemEntity {

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data;

    private HistorySearchEntity entity;
    //查看更多
    public static final int LOADMORE = 0;
    //收起
    public static final int CANCELMORE = 1;
    //正常
    public static final int nomal = 2;

    private int itemType;

    public LoadMoreBean(int type, String data){
        this.data = data;
        this.itemType = type;
    }

    public LoadMoreBean(HistorySearchEntity historySearchEntity ){
        this.entity = historySearchEntity;
        this.data = historySearchEntity.getHotWordName();
        this.itemType = 2;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
