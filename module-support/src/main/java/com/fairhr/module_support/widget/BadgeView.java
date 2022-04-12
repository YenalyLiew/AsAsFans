package com.fairhr.module_support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.fairhr.module_support.R;

/**
 * 实现带有气泡功能的view
 */
public class BadgeView extends FrameLayout {

    private ImageView mIvIcon;
    private TextView mTvContent;
    private TextView mTvNum;
    private boolean mShowBadge;

    public BadgeView(@NonNull Context context) {
        this(context,null);
    }

    public BadgeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BadgeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.support_layout_view_badge, this);
        mIvIcon = view.findViewById(R.id.iv_icon);
        mTvContent = view.findViewById(R.id.tv_badge_title);
        mTvNum = view.findViewById(R.id.tv_badge_num);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.support_badge);
        if (typedArray.hasValue(R.styleable.support_badge_support_content_text)) {
            String name = typedArray.getString(R.styleable.support_badge_support_content_text);
            mTvContent.setText(name);
        }

        mShowBadge = typedArray.getBoolean(R.styleable.support_badge_support_show_badge, false);
        typedArray.recycle();
        updateShowBadge();
    }

    public void updateShowBadge(){
        if(mShowBadge){
            mTvNum.setVisibility(VISIBLE);
        }else{
            mTvNum.setVisibility(GONE);
        }
    }

    public void setContentText(String text) {
        mTvContent.setText(text);
    }

    public void setBadgeNum(String text) {
        if(TextUtils.isEmpty(text)){
            mTvNum.setVisibility(GONE);
        }else{
            mTvNum.setVisibility(VISIBLE);
            mTvNum.setText(text);
        }
    }

    public void setImgSource(int resId){
        mIvIcon.setImageResource(resId);
    }

}
