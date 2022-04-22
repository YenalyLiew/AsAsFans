package com.asoul.asasfans.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;


import com.asoul.asasfans.entity.HistorySearchEntity;

import java.util.List;

/**
 * 历史搜索dao
 */
@Dao
public abstract class HistorySearchDao {

    @Query("SELECT * FROM HistorySearchEntity ORDER BY id DESC")
    public abstract List<HistorySearchEntity> loadAllSync();

    @Query("SELECT * FROM HistorySearchEntity ORDER BY id DESC")
    public abstract LiveData<List<HistorySearchEntity>> loadAllHistoryEntities();

    @Query("DELETE FROM HistorySearchEntity")
    public abstract void deleteAllHistoryEntities();

    @Delete
    public abstract void deleteEntity(HistorySearchEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void insertEntity(HistorySearchEntity entity);

    @Query("SELECT * FROM HistorySearchEntity WHERE " +
            "(hotWordName = :hotWordName ) LIMIT 1")
    public abstract HistorySearchEntity loadEntity(String hotWordName);

    @Transaction
    public void insertOrUpdateEntity(HistorySearchEntity entity) {
        //历史记录限制为20条
        HistorySearchEntity dbEntity = loadEntity(entity.getHotWordName());
        if (dbEntity != null) {
            //已经存在， 删除已有
            deleteEntity(dbEntity);
             insertEntity(entity);
            return;
        }

        List<HistorySearchEntity> allEntities = loadAllSync();
        if (allEntities.size() >= 20) {
            HistorySearchEntity lastEntity = allEntities.get(allEntities.size() - 1);
            deleteEntity(lastEntity);
        }
        insertEntity(entity);
    }

}
