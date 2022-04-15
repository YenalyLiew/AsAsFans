package com.fairhr.module_support.network

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @ProjectName : AsAsFans
 * @Author : Yenaly Liew
 * @Time : 2022/04/15 015 16:31
 * @Description : Description...
 */
object YenalyNetManager {

    fun getRequest(url: String, observer: Observer<String>) {
        Observable.create<String> { emitter ->
            val call = OkHttpManager.getInstance()[url]
            val execute = call.execute()
            if (execute.code == 200) {
                val body = execute.body
                if (body != null) {
                    emitter.onNext(body.string())
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable("请求返回数据为空" + execute.code))
                }
            } else {
                emitter.onError(Throwable("请求失败：" + execute.code))
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
}