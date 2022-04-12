package com.fairhr.module_support.webview.biz;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import androidx.appcompat.app.AlertDialog;

import com.fairhr.module_support.R;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.webview.agent.AgentWeb;
import com.fairhr.module_support.webview.agent.AgentWebUIControllerImplBase;


/**
 * 如果你需要修改某一个AgentWeb 内部的某一个弹窗 ，请看下面的例子
 * 注意写法一定要参照 DefaultUIController 的写法 ，因为UI自由定制，但是回调的方式是固定的，并且一定要回调。
 */
public class MyUIController extends AgentWebUIControllerImplBase {

    private Activity mActivity;

    public MyUIController(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onShowMessage(String message, String from) {
        super.onShowMessage(message, from);
        LogUtil.i(AgentWeb.TAG, "onShowMessage: " + message);
    }

    /**
     * 修改文件选择的弹窗
     */
    @Override
    public void onSelectItemsPrompt(WebView view, String url, String[] items, final Handler.Callback callback) {
        LogUtil.i(AgentWeb.TAG, "onSelectItemsPrompt: " + url);
        //super.onSelectItemsPrompt(view, url, items, callback); // 使用默认的UI
        final int[] index = {-1};
        new AlertDialog.Builder(mActivity)
                .setTitle(R.string.agentweb_select_title)
                .setItems(items, (dialog, which) -> {
                    index[0] = which;
                    dialog.dismiss();
                })
                .setOnDismissListener(dialog -> {
                    if (callback != null) {
                        Message mMessage = Message.obtain();
                        mMessage.what = index[0];  //mMessage.what 必须等于ways的index
                        callback.handleMessage(mMessage); //最后callback一定要回调
                    }
                })
                .show();
    }
}
