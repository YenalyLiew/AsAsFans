package com.fairhr.module_support.webview.biz;

import android.app.Activity;
import androidx.lifecycle.LifecycleObserver;
import com.fairhr.module_support.webview.agent.AgentWeb;



public class SupportJsInterface extends BaseJsInterface implements LifecycleObserver {
    private boolean isDestroy;

    public SupportJsInterface(Activity context, AgentWeb agent) {
        super(context, agent);
        if (context instanceof WebBaseActivity) {
            WebBaseActivity activity = (WebBaseActivity) context;
            activity.getLifecycle().addObserver(this);
        }
    }

}
