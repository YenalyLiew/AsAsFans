package com.fairhr.module_support;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:kingstar
 * Time:2019-09-17
 * PackageName:com.kingstar.ksframework.utils
 * Description:
 */
public class ThreadUtils {
    /**
     * 运行在子线程
     *
     * @param runnable Runnable
     */
    public static void runOnChildThread(@NonNull Runnable runnable) {
        ThreadPoolHolder.THREAD_POOL.submit(runnable);
    }

    /**
     * 运行在UI线程
     *
     * @param runnable Runnable
     */
    public static void runOnUiThread(@NonNull Runnable runnable) {
        UiThreadHandlerHolder.UI_THREAD_HANDLER.post(runnable);
    }

    /**
     * 获得UI线程Handler
     *
     * @return UI线程Handler
     */
    @NonNull
    public static Handler getUiThreadHandler() {
        return UiThreadHandlerHolder.UI_THREAD_HANDLER;
    }

    /**
     * 运行在UI线程
     *
     * @param runnable Runnable
     * @param delayed  延时多久处理
     */
    public static void postRunOnUiThread(@NonNull Runnable runnable, long delayed) {
        UiThreadHandlerHolder.UI_THREAD_HANDLER.postDelayed(runnable, delayed);
    }

    /**
     * 移除主线程消息
     *
     * @param messagesType 延时多久处理
     */
    public static void removeUiThreadMessages(int messagesType) {
        UiThreadHandlerHolder.UI_THREAD_HANDLER.removeMessages(messagesType);
    }

    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    /**
     * 线程池Holder
     */
    private static class ThreadPoolHolder {

        /**
         * 线程池
         */
        private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    }

    /**
     * UI线程HandlerHolder
     */
    private static class UiThreadHandlerHolder {

        /**
         * UI线程Handler
         */
        private static final Handler UI_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    }
}
