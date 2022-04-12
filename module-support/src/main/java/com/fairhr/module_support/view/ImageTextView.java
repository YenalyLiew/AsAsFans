package com.fairhr.module_support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.fairhr.module_support.R;


public class ImageTextView extends LinearLayout {
    private Context mContext;
    private LinearLayout view;
    private ImageView img;
    private TextView text;

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.support_image_text_view, this, true);
        view = findViewById(R.id.view);
        img = findViewById(R.id.img);
        text = findViewById(R.id.text);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        int orientation = a.getInt(R.styleable.ImageTextView_it_orientation, LinearLayout.HORIZONTAL);
        if (orientation == 0) {
            setOrientation(LinearLayout.HORIZONTAL);
            view.setOrientation(LinearLayout.HORIZONTAL);
            text.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            setOrientation(LinearLayout.VERTICAL);
            view.setOrientation(LinearLayout.VERTICAL);
            text.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        text.setText(a.getString(R.styleable.ImageTextView_it_text));
        text.setTextColor(a.getColor(R.styleable.ImageTextView_it_txtColor, Color.BLACK));
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.ImageTextView_it_txtSize, 14));
        int iconRes = a.getResourceId(R.styleable.ImageTextView_it_icon, 0);
        if (iconRes == 0) {
            img.setVisibility(GONE);
        } else {
            img.setImageResource(iconRes);
        }
        int padding = (int) a.getDimension(R.styleable.ImageTextView_it_padding, 3f);

        int margin = (int) a.getDimension(R.styleable.ImageTextView_it_textIconMargin, 3);
        LayoutParams layoutParams = (LayoutParams) text.getLayoutParams();
        if (orientation == 0) {
            layoutParams.setMargins(margin, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            text.setPadding(0, 0, padding, 0);
        } else {
            layoutParams.setMargins(0, margin, 0, 0);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            text.setPadding(padding, 0, padding, padding);
        }
        text.setLayoutParams(layoutParams);


        int size = (int) a.getDimension(R.styleable.ImageTextView_it_iconSize, 10);
        LayoutParams params = new LayoutParams(size, size);
        if (orientation == 0) {
            params.gravity = Gravity.CENTER_VERTICAL;
            img.setPadding(padding, padding, 0, padding);
        } else {
            params.gravity = Gravity.CENTER_HORIZONTAL;
            img.setPadding(padding, padding, padding, 0);
        }
        img.setLayoutParams(params);

        a.recycle();
    }

    public TextView getText() {
        return text;
    }

    public void setText(@StringRes int resId) {
        text.setText(resId);
    }

    public void setText(CharSequence txt) {
        text.setText(txt);
    }

    public void setIcon(@DrawableRes int resId) {
        img.setVisibility(VISIBLE);
        img.setImageResource(resId);
    }
}
