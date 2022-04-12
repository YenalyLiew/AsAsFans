package com.fairhr.module_support.webview.agent;

import static com.fairhr.module_support.utils.AppUtils.openBrowser;

import android.app.Activity;
import android.content.DialogInterface;
import android.webkit.DownloadListener;

import androidx.appcompat.app.AlertDialog;

import com.fairhr.module_support.R;
import com.fairhr.module_support.utils.ContextUtil;
import com.fairhr.module_support.utils.LogUtil;

public class BrowserDownloadImpl implements DownloadListener {

    private final Activity mContext;

    public BrowserDownloadImpl(Activity activity) {
        this.mContext = activity;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

        LogUtil.i(AgentWeb.TAG, "onDownloadStart $url");
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.agentweb_dialog_tip)
                .setMessage(R.string.agentweb_tip_download)
                .setPositiveButton(R.string.support_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openBrowser(mContext, url);
                    }
                })
                .setNegativeButton(R.string.support_cancel, null)
                .show();


    }
}
