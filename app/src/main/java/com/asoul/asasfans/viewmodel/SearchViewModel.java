package com.asoul.asasfans.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;


import com.fairhr.module_support.base.BaseViewModel;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.network.ErsNetManager;
import com.fairhr.module_support.tools.inter.ErsDataObserver;

import com.fairhr.module_support.utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;

public class SearchViewModel extends BaseViewModel {


    public SearchViewModel(@NonNull Application application) {
        super(application);

    }



    public void gethotSearch() {

        Map<String, Object> params = new HashMap<>();
//
//
//        ErsNetManager.getInstance().getRequest(UrlUtils.formatUrl(ServiceConstants.BASE_NET, ServiceConstants.NET_TITLE, params),
//                new ErsDataObserver() {
//                    @Override
//                    public void onSuccess(String result) {
//
//
//                    }
//
//                    @Override
//                    public void onServiceError(int errorCode, String errorMsg) {
//
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//
//                    }
//                });
    }




}
