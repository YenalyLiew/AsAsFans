package com.asoul.asasfans.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 历史搜索entity
 */
@Entity
public class HistorySearchEntity {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    /**
     * 名称
     */
    protected String hotWordName;

    /**
     * 类型
     */
    protected int hotWordId;







    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHotWordName() {
        return hotWordName;
    }

    public void setHotWordName(String hotWordName) {
        this.hotWordName = hotWordName;
    }

    public int getHotWordId() {
        return hotWordId;
    }

    public void setHotWordId(int hotWordId) {
        this.hotWordId = hotWordId;
    }


}
