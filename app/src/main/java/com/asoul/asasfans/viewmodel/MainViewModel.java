package com.asoul.asasfans.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.asoul.asasfans.bean.GithubVersionBean;
import com.fairhr.module_support.base.BaseViewModel;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.network.YenalyNetManager;
import com.fairhr.module_support.utils.GsonUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class MainViewModel extends BaseViewModel {

    private MutableLiveData<GithubVersionBean> mVersion;

    public LiveData<GithubVersionBean> getmVersion() {
        return mVersion;
    }


    public MainViewModel(@NonNull Application application) {
        super(application);
        mVersion = new MutableLiveData<>();
    }

    //获取认证问题
    public void getVersion() {

        YenalyNetManager.INSTANCE.getRequest(ServiceConstants.VERSION, new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                Type type = new TypeToken<GithubVersionBean>() {
                }.getType();
                GithubVersionBean retList = GsonUtils.fromJson(s, type);
                mVersion.postValue(retList);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                LogUtil.d("JSONObject", "errorMsg111=?:" + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }


}
