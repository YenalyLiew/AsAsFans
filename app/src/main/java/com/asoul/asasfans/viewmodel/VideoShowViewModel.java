package com.asoul.asasfans.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.asoul.asasfans.bean.VideoBean;
import com.asoul.asasfans.bean.VideoListBean;
import com.fairhr.module_support.base.BaseViewModel;
import com.fairhr.module_support.constants.ServiceConstants;
import com.fairhr.module_support.network.ErsNetManager;
import com.fairhr.module_support.tools.inter.ErsDataObserver;
import com.fairhr.module_support.utils.GsonUtils;
import com.fairhr.module_support.utils.UrlUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoShowViewModel extends BaseViewModel {


    private MutableLiveData<VideoListBean> mVideoLists;

    public LiveData<VideoListBean> getVideoListData() {
        return mVideoLists;
    }

    public final ArrayList<VideoBean> tempData = new ArrayList<>();

    public VideoShowViewModel(@NonNull Application application) {
        super(application);
        mVideoLists = new MutableLiveData<>();
    }


    public void getVideoList(int type, int pageIndex) {

        Map<String, Object> params = new HashMap<>();

        long timestamp = System.currentTimeMillis() / 1000;
        long fifteenDaysTimestamp = 25_9200L;

        if (type == 0) {
            params.put("order", "score");
            params.put("copyright", 1);
            params.put("q", "pubdate." + (timestamp - fifteenDaysTimestamp) + "+" + timestamp + ".BETWEEN");
        } else if (type == 1) {
            params.put("order", "score");
            params.put("copyright", 2);
            params.put("q", "pubdate." + (timestamp - fifteenDaysTimestamp) + "+" + timestamp + ".BETWEEN");
        } else if (type == 2) {
            params.put("order", "pubdate");
            params.put("q", "");
            params.put("copyright", "");
        } else if (type == 3) {
            params.put("order", "score");
            params.put("q", "");
            params.put("copyright", "");
        }

        params.put("tname", "");
        params.put("page", pageIndex);


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


}
