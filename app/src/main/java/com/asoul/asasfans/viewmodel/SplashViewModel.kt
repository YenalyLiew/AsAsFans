package com.asoul.asasfans.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asoul.asasfans.bean.ImageDataBean
import com.fairhr.module_support.base.BaseViewModel
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.network.YenalyNetManager
import com.fairhr.module_support.utils.GsonUtils
import com.fairhr.module_support.utils.UrlUtils
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import kotlin.collections.HashMap

class SplashViewModel (application: Application) : BaseViewModel(application) {

    private val _mFanArtList = MutableLiveData<Result<List<ImageDataBean>?>>()

    val mFanArtList: LiveData<Result<List<ImageDataBean>?>> = _mFanArtList

    fun getBackground() {


        val page = Random().nextInt(11)
        val part = 0
        val rank = 0
        val ctime =""
        val sort = ""

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