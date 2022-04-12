package com.fairhr.module_support.base;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.fairhr.module_support.tools.inter.IBackPressedListener;
import com.fairhr.module_support.tools.inter.ISysKeyBoardListener;
import com.fairhr.module_support.utils.SystemStatusUtil;

import java.util.List;

/**
 * Description: 框架层activity
 */
public abstract class FrameActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    public static boolean WINDOW_FULL_MODE = true;
    public static boolean STATUS_ICONDARK_MODE = true;

    //系统键盘显示隐藏监听
    private ISysKeyBoardListener mISysKeyBoardListener;
    private View mAcRootDecorView;//activity根View
    private int mAcRootViewVisibleHeight;//用于监听根布局高度

    public ISysKeyBoardListener getISysKeyBoardListener() {
        return mISysKeyBoardListener;
    }

    /**
     * 设置APP系统键盘监听
     *
     * @param ISysKeyBoardListener
     */
    public void setISysKeyBoardListener(ISysKeyBoardListener ISysKeyBoardListener) {
        mISysKeyBoardListener = ISysKeyBoardListener;
        systemKeyboardChange();
    }

    /**
     * 初始化数据
     */
    public abstract void initBundleData();

    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 初始化服务器舒数据
     */
    public abstract void initServiceData();


    @Override
    public void setContentView(int layoutResID) {
        initStatusBar();
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        initStatusBar();
        super.setContentView(view, params);
    }

    /**
     * 初始化状态栏
     */
    public void initStatusBar() {
        if (isWindowFullMode()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.hide();
            SystemStatusUtil.fullScreen(getWindow(), true);
            SystemStatusUtil.setStatusIconDarkMode(getWindow(), isStatusIconDarkMode());
        }
    }

    /**
     * 界面是否全屏
     *
     * @return
     */
    public boolean isWindowFullMode() {
        return WINDOW_FULL_MODE;
    }

    /**
     * 状态栏是否深色模式
     *
     * @return
     */
    public boolean isStatusIconDarkMode() {
        return STATUS_ICONDARK_MODE;
    }

    /**
     * 请求屏幕竖屏
     * 当背景为透明时8.0 8.1不能设置
     */
    protected void requestScreenPortrait() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O && Build.VERSION.SDK_INT != Build.VERSION_CODES.O_MR1)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /**
     * 设置系统键盘监听
     */
    private void systemKeyboardChange() {
        mAcRootDecorView = getWindow().getDecorView();
        mAcRootDecorView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        //获取当前根视图在屏幕上显示的大小
        Rect r = new Rect();
        mAcRootDecorView.getWindowVisibleDisplayFrame(r);

        int visibleHeight = r.height();
        if (mAcRootViewVisibleHeight == 0) {
            mAcRootViewVisibleHeight = visibleHeight;
            return;
        }
        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (mAcRootViewVisibleHeight == visibleHeight) {
            return;
        }
        //根视图显示高度变小超过200，可以看作软键盘显示了
        if (mAcRootViewVisibleHeight - visibleHeight > 200) {
            int keyHeight = mAcRootViewVisibleHeight - visibleHeight;
            mAcRootViewVisibleHeight = visibleHeight;
            softKeyBoardChange(true, keyHeight);
            return;
        }
        //根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - mAcRootViewVisibleHeight > 200) {
            softKeyBoardChange(false, 0);
            mAcRootViewVisibleHeight = visibleHeight;
        }
    }

    /**
     * 系统键盘显示关闭
     *
     * @param isOpen true显示 false关闭
     */
    private void softKeyBoardChange(boolean isOpen, int keyHeight) {
        if (mISysKeyBoardListener != null) {
            mISysKeyBoardListener.onSoftKeyBoardChange(isOpen, keyHeight);
        }
    }

    @Override
    public void onBackPressed() {
        if (!distributeBackPressedToFragment()) {
            super.onBackPressed();
        }
    }

    /**
     * 分发物理返回键点击事件到Fragment
     */
    private boolean distributeBackPressedToFragment() {
        boolean hasDeal = false;
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            List<Fragment> fragments = supportFragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof IBackPressedListener && fragment.isVisible()) {
                        IBackPressedListener iBackPressedListener = (IBackPressedListener) fragment;
                        if (iBackPressedListener.onBackPressed())
                            hasDeal = true;
                    }
                }
            }
        } catch (Exception e) {

        }
        return hasDeal;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAcRootDecorView != null && mISysKeyBoardListener != null) {
            mAcRootDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mAcRootDecorView = null;
            mISysKeyBoardListener = null;
        }
    }
}
