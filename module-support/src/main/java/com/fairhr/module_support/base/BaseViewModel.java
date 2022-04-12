package com.fairhr.module_support.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.fairhr.module_support.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Describe:基础BaseViewModel
 */
public class BaseViewModel extends AndroidViewModel implements LifecycleEventObserver {
    protected final Application mApplication;
    private MutableLiveData<Void> mShowLoadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Void> mDimissLoadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Void> mFinishLoadingLiveData = new MutableLiveData<>();
    private List<LiveData> mAllLiveData = new ArrayList<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        this.mApplication = application;
        addLiveData(mShowLoadingLiveData);
        addLiveData(mDimissLoadingLiveData);
        addLiveData(mFinishLoadingLiveData);
    }


    public MutableLiveData<Void> getShowLoadingLiveData() {
        return mShowLoadingLiveData;
    }

    public MutableLiveData<Void> getDimissLoadingLiveData() {
        return mDimissLoadingLiveData;
    }

    public MutableLiveData<Void> getFinishLoadingLiveData() {
        return mFinishLoadingLiveData;
    }


    public void addLiveData(LiveData liveData) {
        mAllLiveData.add(liveData);
    }

    public void addLiveData(LiveData... liveDatas) {
        if (liveDatas != null) {
            for (LiveData liveData : liveDatas) {
                if (liveData != null)
                    mAllLiveData.add(liveData);
            }
        }

    }

    public List<LiveData> getAllLiveData() {
        return mAllLiveData;
    }

    /**
     * 显示loding
     */
    public void showLoading() {
        mShowLoadingLiveData.postValue(null);
    }

    /**
     * 关闭loading
     */
    public void dimissLoding() {
        mDimissLoadingLiveData.postValue(null);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        mFinishLoadingLiveData.postValue(null);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

    }

    public RequestBody getRequestBody(Map<String, Object> map){
        return RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                JsonUtil.getInstance().toJsonString(map));
    }

    public RequestBody getRequestBody(String jsonStr){
        return RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonStr);
    }
}

