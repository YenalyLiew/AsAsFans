package com.asoul.asasfans.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.asoul.asasfans.bean.GithubVersionBean;
import com.fairhr.module_support.base.BaseViewModel;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.network.ErsNetManager;
import com.fairhr.module_support.tools.inter.ErsDataObserver;

import com.fairhr.module_support.utils.GsonUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class MainViewModel extends BaseViewModel {



    private MutableLiveData<GithubVersionBean> mVersion;

    public LiveData<GithubVersionBean> getmVersion(){
        return mVersion;
    }


    public MainViewModel(@NonNull Application application) {
        super(application);
        mVersion = new MutableLiveData<>();
    }

    //获取认证问题
    public void getVersion(){




        ErsNetManager.getInstance().getRequestSpecial(ServiceConstants.VERSION, new ErsDataObserver(){
                    @Override
                    public void onSuccess(String result) {

                        Type type = new TypeToken<GithubVersionBean>() {
                        }.getType();
                        GithubVersionBean retList= GsonUtils.fromJson(result,type);
                        mVersion.postValue(retList);
                    }

                    @Override
                    public void onServiceError(int errorCode, String errorMsg) {
                        LogUtil.d("JSONObject","errorMsg111=?:"+errorMsg);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        LogUtil.d("JSONObject","errorMsg111=?:"+e.toString());
                    }
                });
    }



}
