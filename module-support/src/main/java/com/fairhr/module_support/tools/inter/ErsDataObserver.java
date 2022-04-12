package com.fairhr.module_support.tools.inter;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public abstract class   ErsDataObserver implements Observer<String> {


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull String respose) {
        try {
            String data;
            int code;
            JSONObject result = new JSONObject(respose);
            code = result.optInt("Code");
            if(code == 0){
                code = result.optInt("code");
            }
            if(code == 200 || code == 0){
                boolean hasSuccess = result.has("isSuccess");
                if(hasSuccess){
                    boolean isSuccess = result.optBoolean("isSuccess");
                    if(isSuccess){
                        data = result.optString("Data");
                        if(TextUtils.isEmpty(data)){
                            data = result.optString("data");
                        }
                        onSuccess(data);
                    }else{
                        onServiceError(code,result.optString("msg"));
                    }
                }else{
                    data = result.optString("Data");
                    if(TextUtils.isEmpty(data)){
                        data = result.optString("data");
                    }
                    onSuccess(data);
                }

            }else {

                onServiceError(code,result.optString("Message"));
            }

        } catch (Exception e) {
            onError(e);
        }

    }


    public abstract void onSuccess(String result);

    public abstract void onServiceError(int errorCode, String errorMsg);

    @Override
    public void onComplete() {

    }
}
