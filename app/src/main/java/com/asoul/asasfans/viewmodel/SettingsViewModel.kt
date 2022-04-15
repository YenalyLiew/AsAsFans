package com.asoul.asasfans.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asoul.asasfans.bean.GithubVersionBean
import com.fairhr.module_support.base.BaseViewModel
import com.fairhr.module_support.constants.ServiceConstants
import com.fairhr.module_support.network.ErsNetManager
import com.fairhr.module_support.network.YenalyNetManager
import com.fairhr.module_support.tools.inter.ErsDataObserver
import com.fairhr.module_support.utils.GsonUtils
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/14 014 14:53
 * @Description : Description...
 */
class SettingsViewModel(application: Application) : BaseViewModel(application) {

    private val _mVersion = MutableLiveData<Result<GithubVersionBean?>>()
    val mVersion: LiveData<Result<GithubVersionBean?>> = _mVersion

    fun getVersion() {
        YenalyNetManager.getRequest(ServiceConstants.VERSION, object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: String) {
                val type = object : TypeToken<GithubVersionBean?>() {}.type
                val retList: GithubVersionBean? = GsonUtils.fromJson(t, type)
                _mVersion.postValue(Result.success(retList))
            }

            override fun onError(e: Throwable) {
                _mVersion.postValue(Result.failure(e))
            }

            override fun onComplete() {
            }
        })
    }
}