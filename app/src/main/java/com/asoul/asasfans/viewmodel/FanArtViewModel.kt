package com.asoul.asasfans.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asoul.asasfans.bean.ImageDataBean
import com.fairhr.module_support.base.BaseViewModel
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.network.YenalyNetManager
import com.fairhr.module_support.utils.GsonUtils
import com.fairhr.module_support.utils.UrlUtils
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/14 014 23:08
 * @Description : Description...
 */
class FanArtViewModel(application: Application) : BaseViewModel(application) {

    var fanArtOrder: String = ""
    var fanArtDate = 0
    var fanArtPart = 0

    val tempData = mutableListOf<ImageDataBean>()

    private val _mFanArtList = MutableLiveData<Result<List<ImageDataBean>?>>()

    val mFanArtList: LiveData<Result<List<ImageDataBean>?>> = _mFanArtList

    fun getFanArtList(
        page: Int,
        sort: String = "",
        part: Int = 0,
        rank: Int = 0,
        ctime: String = ""
    ) {

        val params: HashMap<String, Any> = hashMapOf(
            "page" to page,
            "part" to part,
            "rank" to rank
        )
        if (sort.isNotBlank()) params["sort"] = sort
        if (ctime.isNotBlank()) params["ctime"] = ctime

        YenalyNetManager.getRequest(
            UrlUtils.formatUrl(
                ServiceConstants.FAN_ART_BASE,
                ServiceConstants.FAN_ART_TITLE,
                params
            ), object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                    if (t.contains("\\u6ca1\\u6709\\u66f4\\u591a\\u6570\\u636e")) {
                        _mFanArtList.postValue(Result.failure(RuntimeException("没有更多数据")))
                    } else {
                        val typeToken = object : TypeToken<List<ImageDataBean>>() {}.type
                        val list = GsonUtils.fromJson<List<ImageDataBean>>(t, typeToken)
                        _mFanArtList.postValue(Result.success(list))
                    }
                }

                override fun onError(e: Throwable) {
                    _mFanArtList.postValue(Result.failure(e))
                }

                override fun onComplete() {
                }

            }
        )
    }
}