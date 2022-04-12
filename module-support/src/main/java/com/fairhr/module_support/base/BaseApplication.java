package com.fairhr.module_support.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.fairhr.module_support.core.ApplicationManager;

public class BaseApplication extends Application implements ViewModelStoreOwner {

    public static BaseApplication sApplication;

    private ViewModelStore mAppViewModelStore;

    private ViewModelProvider.Factory mFactory;


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mAppViewModelStore = new ViewModelStore();
        registerActivityLifecycleCallbacks(ApplicationManager.getInstance());
    }

    /**
     * 获取一个全局的ViewModel
     */
    public ViewModelProvider getAppViewModelProvider(){
        return new ViewModelProvider(this, this.getAppFactory());
    }

    private ViewModelProvider.Factory getAppFactory(){
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this);
        }
        return mFactory;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mAppViewModelStore;
    }
}
