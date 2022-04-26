package com.asoul.asasfans.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.asoul.asasfans.bean.SearchVidoeListBean;
import com.asoul.asasfans.bean.VideoListBean;
import com.fairhr.module_support.base.BaseViewModel;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.network.ErsNetManager;
import com.fairhr.module_support.tools.inter.ErsDataObserver;

import com.fairhr.module_support.utils.GsonUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.utils.UrlUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SearchViewModel extends BaseViewModel {


    public SearchViewModel(@NonNull Application application) {
        super(application);
        mVideoLists = new MutableLiveData<>();
        mSearchVideoLists = new MutableLiveData<>();
    }

    private MutableLiveData<VideoListBean> mVideoLists;

    public LiveData<VideoListBean> getVideoListData() {
        return mVideoLists;
    }

    private MutableLiveData<SearchVidoeListBean> mSearchVideoLists;

    public LiveData<SearchVidoeListBean> getSearchVideoListData() {
        return mSearchVideoLists;
    }


    public void getVideoList() {

        Map<String, Object> params = new HashMap<>();

        params.put("order", "score");
        params.put("q", "");
        params.put("copyright", "");

        params.put("tname", "");
        params.put("page",1);


        ErsNetManager.getInstance().getRequest(UrlUtils.formatUrl(ServiceConstants.BASE_NET, ServiceConstants.NET_TITLE, params),
                new ErsDataObserver() {
                    @Override
                    public void onSuccess(String result) {

                        Type type = new TypeToken<VideoListBean>() {
                        }.getType();
                        VideoListBean retList = GsonUtils.fromJson(result, type);
                        mVideoLists.postValue(retList);

                    }

                    @Override
                    public void onServiceError(int errorCode, String errorMsg) {

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }



    public void getSearchResult(int pageSize,String content) {

        Map<String, Object> params = new HashMap<>();


        params.put("context", "");
        params.put("search_type","video");
        params.put("page",pageSize );
        params.put("order","click" );
        params.put("keyword",content );
        params.put("duration",0 );
        params.put("category_id","" );
        params.put("tids_2","" );
        params.put("__refresh__",true );
        params.put("_extra","" );
        params.put("tids",0);
        params.put("highlight",1);
        params.put("single_column",0);




        ErsNetManager.getInstance().getRequest(UrlUtils.formatUrl(ServiceConstants.BILIBILI_SEARCH, ServiceConstants.BILIBILI_SEARCH_TYPE, params),
                new ErsDataObserver() {
                    @Override
                    public void onSuccess(String result) {

                        Type type = new TypeToken<SearchVidoeListBean>() {
                        }.getType();
                        SearchVidoeListBean retList = GsonUtils.fromJson(result, type);
                        mSearchVideoLists.postValue(retList);

                    }

                    @Override
                    public void onServiceError(int errorCode, String errorMsg) {
                        LogUtil.d("JSONObject","errorMsg111=:"+errorMsg);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        LogUtil.d("JSONObject","errorMsg111=:"+e.toString());

                    }
                });
    }



}
