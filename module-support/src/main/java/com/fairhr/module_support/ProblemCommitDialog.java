package com.fairhr.module_support;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProblemCommitDialog extends Dialog {

    private final Context mContext;
    private View mRootView;
    private TextView mTvClose;
    private TextView mTvCommit;
    private RecyclerView mRcvReason;
    private EditText mEtFeedback;
    private TextView mTvCount;

    public ProblemCommitDialog(@NonNull Context context) {
        this(context, R.style.bottomDialog_bottomTransAnim);
    }

    public ProblemCommitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    public void init(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.support_layout_web_problem_feedback, null, false);
        setContentView(mRootView);
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        initView();
        initRcv();
        initEvent();
    }

    private void initView() {
        mTvClose = mRootView.findViewById(R.id.tv_close);
        mTvCommit = mRootView.findViewById(R.id.tv_commit);
        mRcvReason = mRootView.findViewById(R.id.rcv_reason);
        mEtFeedback = mRootView.findViewById(R.id.et_shuru);
        mTvCount = mRootView.findViewById(R.id.tv_count);
    }

    private void initRcv() {

    }

    private void initEvent() {

    }





}
