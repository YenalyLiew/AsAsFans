package com.fairhr.module_support.tools.inter;


import java.io.File;
import java.util.HashMap;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public abstract class OnUploadFileObserver<T> implements Observer<T> {

    private HashMap<String, String> mHasUploadSuccess = new HashMap<>();

    public abstract void onSuccess(T t);

    public abstract void onProgress(File file, long bytesWritten, long contentLength, int percent);

    public abstract void onFail(Throwable throwable);


    //监听进度的改变
    public void onProgressChange(File file, long bytesWritten, long contentLength) {
        onProgress(file, bytesWritten, contentLength, contentLength == 0 ? 0 : (int) (bytesWritten * 100 / contentLength));
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    @Override
    public void onComplete() {

    }
}
