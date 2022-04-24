package com.asoul.asasfans;

import androidx.lifecycle.LiveData;

import com.asoul.asasfans.dao.AppDataBase;
import com.asoul.asasfans.dao.HistorySearchDao;
import com.asoul.asasfans.entity.HistorySearchEntity;
import com.fairhr.module_support.utils.ContextUtil;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryDataRepository {

    private static HistoryDataRepository sInstance;

    private final HistorySearchDao historySearchDao;

    private HistoryDataRepository(AppDataBase database) {
        this.historySearchDao = database.getHistorySearchDao();
    }

    public static HistoryDataRepository getInstance() {
        if (null == sInstance) {
            synchronized (HistoryDataRepository.class) {
                if (null == sInstance) {
                    sInstance = new HistoryDataRepository(AppDataBase.getInstance(ContextUtil.getContext()));
                }
            }
        }
        return sInstance;
    }

    //获取所有的历史搜索记录
    public LiveData<List<HistorySearchEntity>> getAllHistoryLiveData() {
        return historySearchDao.loadAllHistoryEntities();
    }

    //插入历史搜索记录
    public void insertHistoryEntity(HistorySearchEntity entity){
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                historySearchDao.insertOrUpdateEntity(entity);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    //删除所有的历史搜索记录
    public void deleteAllHistoryEntity(){
        historySearchDao.deleteAllHistoryEntities();
    }


}
