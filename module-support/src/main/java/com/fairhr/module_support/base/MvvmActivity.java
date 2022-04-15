package com.fairhr.module_support.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fairhr.module_support.AppViewModel;
import com.fairhr.module_support.KtxActivityManger;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.utils.SPreferenceUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class MvvmActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends FrameActivity {
    protected DB mDataBinding;
    protected VM mViewModel;
    public AppViewModel mAppViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
//        if (FrameConfigManager.getInstance().isInitArouter())
        ARouter.getInstance().inject(this);
        KtxActivityManger.pushActivity(this);
        LogUtil.d("MvvmActivity","MvvmActivity11");
        initBundleData();
        initViewDataBinding(savedInstanceState);
        initView();
        registerLiveDataObserve();
        initServiceData();
    }

    /**
     * 初始化bundle数据
     */
    @Override
    public void initBundleData() {

    }

    /**
     * 初始化ViewModle 和databinding
     *
     * @param savedInstanceState
     */
    protected void initViewDataBinding(@Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.setContentView(this, initContentView());
        mViewModel = initViewModel();
        mAppViewModel = ((BaseApplication) getApplication())
                .getAppViewModelProvider()
                .get(AppViewModel.class);
        if (mViewModel == null) {
            //通过获取第二个范型创建mode
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) createViewModel(this, modelClass);
        }
        mDataBinding.setLifecycleOwner(this);
        initDataBindingVariable();
        if (mViewModel != null)
            getLifecycle().addObserver(mViewModel);
    }

    /**
     * 初始化布局文件
     *
     * @return
     */
    public abstract int initContentView();

    /**
     * 初始化viewModel
     *
     * @return
     */
    public abstract VM initViewModel();

    /**
     * 初始化主题
     */
    public abstract void setTheme();

    /**
     * 初始化DataBinding常量
     */
    public abstract void initDataBindingVariable();


    /**
     * 初始化view
     */
    @Override
    public void initView() {

    }

    /**
     * 注册livedata监听器
     */
    public void registerLiveDataObserve() {
        if (mViewModel != null) {
            mViewModel.getShowLoadingLiveData().observe(this, new Observer<Void>() {
                @Override
                public void onChanged(Void aVoid) {
                    showLoading();
                }
            });
            mViewModel.getDimissLoadingLiveData().observe(this, new Observer<Void>() {
                @Override
                public void onChanged(Void aVoid) {
                    dimissLoding();
                }
            });
            mViewModel.getFinishLoadingLiveData().observe(this, new Observer<Void>() {
                @Override
                public void onChanged(Void aVoid) {
                    finish();
                }
            });
        }
    }

    /**
     * 初始化服务器数据
     */
    @Override
    public void initServiceData() {

    }

    /**
     * 显示loading
     */
    public void showLoading() {

    }

    /**
     * 关闭loading
     */
    public void dimissLoding() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataBinding != null)
            mDataBinding.unbind();
        if (mViewModel != null) {
            mViewModel.onStateChanged(this, Lifecycle.Event.ON_DESTROY);
            getLifecycle().removeObserver(mViewModel);
        }
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity fragmentActivity, Class<T> cls) {
        return new ViewModelProvider(fragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(fragmentActivity.getApplication())).get(cls);
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return new ViewModelProvider(fragment, ViewModelProvider.AndroidViewModelFactory.getInstance(fragment.requireActivity().getApplication())).get(cls);
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity fragmentActivity, String key, Class<T> cls) {
        return new ViewModelProvider(fragmentActivity, ViewModelProvider.AndroidViewModelFactory.getInstance(fragmentActivity.getApplication())).get(key, cls);
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, String key, Class<T> cls) {
        return new ViewModelProvider(fragment, ViewModelProvider.AndroidViewModelFactory.getInstance(fragment.requireActivity().getApplication())).get(key, cls);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
