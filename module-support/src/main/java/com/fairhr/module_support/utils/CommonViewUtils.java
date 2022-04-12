package com.fairhr.module_support.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.fairhr.module_support.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * 常用View处理工具
 */

public class CommonViewUtils {
    /**
     * 最大快速双击间隔毫秒数
     */
    private static final long MAX_FAST_DOUBLE_CLICK_INTERVAL_MILLS = 800L;
    /**
     * 视图tag键 - 上次点击时间(单位:毫秒数)
     */
    private static final int VIEW_TAG_KEY_LAST_CLICK_TIME_MILLIS = -99;
    private static long sLastClickTime = 0;

    /**
     * 过滤快速双击
     *
     * @param view 视图
     * @return true->不是快速双击; false->是快速双击
     */
    public static boolean filterFastDoubleClick(@NonNull View view) {
        boolean result;
        long currentTimeMillis = System.currentTimeMillis();
        long lastClickTimeMillis;
        try {
            lastClickTimeMillis = (long) view.getTag(VIEW_TAG_KEY_LAST_CLICK_TIME_MILLIS);
        } catch (ClassCastException | NullPointerException e) {
            lastClickTimeMillis = 0;
        }
        long deltaTimeMills = currentTimeMillis - lastClickTimeMillis;
        result = Math.abs(deltaTimeMills) >= MAX_FAST_DOUBLE_CLICK_INTERVAL_MILLS;
        if (result) {
            view.setTag(VIEW_TAG_KEY_LAST_CLICK_TIME_MILLIS, currentTimeMillis);
        }
        return result;
    }

    /**
     * 过滤快熟点击问题
     *
     * @return
     */
    public synchronized static boolean filterFastDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - sLastClickTime >= MAX_FAST_DOUBLE_CLICK_INTERVAL_MILLS) {
            sLastClickTime = currentTimeMillis;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移动输入框光标
     *
     * @param editText
     */
    public static void moveEditViewCursorChange(final EditText editText) {
        Observable.timer(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        editText.setSelection(editText.length());
                        editText.setCursorVisible(true);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 计算测量过的尺寸
     *
     * @param widthMeasureSpec     horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param heightMeasureSpec    vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param viewDesiredDimension 视图需要的尺寸
     * @param measuredDimension    测量过的尺寸
     * @return 测量过的尺寸
     */
    @NonNull
    public static Point calculateMeasuredDimension(int widthMeasureSpec, int heightMeasureSpec
            , @NonNull Point viewDesiredDimension, @NonNull Point measuredDimension) {
        measuredDimension.x = calculateMeasuredDimension(widthMeasureSpec, viewDesiredDimension.x);
        measuredDimension.y = calculateMeasuredDimension(heightMeasureSpec, viewDesiredDimension.y);
        return measuredDimension;
    }

    /**
     * 计算测量过的(水平或者竖直)边长
     *
     * @param measureSpec           horizontal or vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param viewDesiredSideLength 视图需要的(水平或者竖直)边长
     * @return 测量过的(水平或者竖直)边长
     */
    private static int calculateMeasuredDimension(int measureSpec, int viewDesiredSideLength) {
        int result;
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case View.MeasureSpec.EXACTLY:
                // 对应match_parent和具体数值
                result = size;
                break;
            case View.MeasureSpec.AT_MOST:
                // 对应wrap_content
                result = Math.min(viewDesiredSideLength, size);
                break;
            case View.MeasureSpec.UNSPECIFIED:
                // 父容器不对View有任何限制，要多大有多大，这种情况一般用于系统内容。在我们使用过程中，一般不考虑这种模式。
                result = viewDesiredSideLength;
                break;
            default:
                // 按道理不会进入这里
                result = viewDesiredSideLength;
                break;
        }
        return result;
    }

    /**
     * 获得视图尺寸
     *
     * @param view 视图
     * @return 视图尺寸
     */
    @NonNull
    public static Point getViewSize(@NonNull View view) {
        Point result = new Point();
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        result.x = view.getMeasuredWidth();
        result.y = view.getMeasuredHeight();
        return result;
    }

    /**
     * textView 设置view
     */
    public static void setTextViewDrawableLeft(TextView view,
                                               int resId, int paddingPx) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        view.setCompoundDrawablePadding(paddingPx);
    }

    /**
     * textView 设置view
     */
    public static void setTextViewDrawableTop(TextView view,
                                              int resId, int paddingPx) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
        view.setCompoundDrawablePadding(paddingPx);
    }

    /**
     * textView 设置view
     */
    public static void setTextViewDrawableRight(TextView view,
                                                int resId, int paddingPx) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(paddingPx);
    }

    /**
     * textView 设置view
     */
    public static void setTextViewDrawableBottom(TextView view,
                                                 int resId, int paddingPx) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, drawable);
        view.setCompoundDrawablePadding(paddingPx);
    }

    /**
     * textView 设置没有View
     */
    public static void setTextViewNoDrawable(TextView view) {
        view.setCompoundDrawables(null, null, null, null);
    }

    public static void setTextViewShadeTopToBottom(TextView tv, int startColor, int endColor) {
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0,
                tv.getTextSize(),
                startColor,
                endColor,
                Shader.TileMode.CLAMP);
        tv.getPaint().setShader(mLinearGradient);
    }

    /**
     * 测量能够画在指定宽度内字符串的字体大小
     *
     * @param text        文本内容
     * @param width       指定的宽度
     * @param maxTextSize 文本字体最大
     * @return
     */

    public static float measureTextSize(String text, int width, float maxTextSize) {
        if (TextUtils.isEmpty(text)) {
            return maxTextSize;
        }
        float textSize = maxTextSize;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);

        while (textWidth > width) {
            textSize--;
            paint.setTextSize(textSize);
            textWidth = paint.measureText(text);
        }
        return textSize;
    }

    /**
     * 密码明文显示开关
     *
     * @param nEditText
     * @param isChecked
     */
    public static void EditPwdShowSwitch(EditText nEditText, boolean isChecked) {
        if (isChecked) {
            nEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            nEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        int len = nEditText.getText().toString().length();
        if (len != 0) {
            nEditText.setSelection(len);
        }
    }


    /**
     * 是否省略了
     *
     * @param textView 文字视图
     * @return 是否省略了
     */
    public static boolean isEllipsised(@NonNull TextView textView) {
        return getEllipsisCount(textView) > 0;
    }

    /**
     * 获得省略数量
     *
     * @param textView 文字视图
     * @return 省略数量
     */
    public static int getEllipsisCount(@NonNull TextView textView) {
        int result = 0;
        Layout layout = textView.getLayout();
        if (null != layout) {
            int lines = layout.getLineCount();
            if (lines > 0) {
                result = layout.getEllipsisCount(lines - 1);
            }
        }
        return result;
    }

    /**
     * 设置view为状态栏的高度
     *
     * @param view
     */
    public static void changeView2StatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = DeviceInfo.getStatusBarHeight(BaseApplication.sApplication);
        view.setLayoutParams(layoutParams);
    }

    /**
     * view在屏幕中的位置
     *
     * @param view
     * @return
     */
    public static int[] getViewAtScreeenLocal(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    /**
     * @param context
     * @param watermarkTexts
     * @param bgColor
     * @param textColor
     * @param textSizePx
     * @param rowWatermarkNum
     * @return
     */
    public static BitmapDrawable creatWatermarkBitmapDrawable(Context context, List<String> watermarkTexts, int watermarkAngle
            , @ColorInt int bgColor, @ColorInt int textColor, int textSizePx, int rowWatermarkNum) {
        int screenwidth = DeviceInfo.getAppScreenSize(context)[0];
        int watermarkWidth = screenwidth / rowWatermarkNum;
        int watermarkHight = watermarkWidth;
        Bitmap bitmap = Bitmap.createBitmap(watermarkWidth, watermarkHight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(bgColor);
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setAlpha(20);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSizePx);
        Path path = new Path();
        float maxWidth = 0;
        for (String watermarkText : watermarkTexts) {
            float v = paint.measureText(watermarkText);
            if (v > maxWidth) {
                maxWidth = v;
            }
        }
        float textWidth = maxWidth;
        float textHWidth = (float) (Math.cos(Math.PI * watermarkAngle / 180.0) * textWidth);
        float textVHeight = (float) (Math.sin(Math.PI * watermarkAngle / 180.0) * textWidth);

        path.moveTo((watermarkWidth - textHWidth) / 2, (watermarkHight - textVHeight) / 2 + textVHeight);
        path.lineTo((watermarkWidth - textHWidth) / 2 + textHWidth, (watermarkHight - textVHeight) / 2);
        int vOffset = 0;
        for (String watermarkText : watermarkTexts) {
            canvas.drawTextOnPath(watermarkText, path, 0, vOffset, paint);
            vOffset += 45;
        }


        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);

        return bitmapDrawable;
    }

    /**
     * 设置textview缩进
     *
     * @param textView
     * @param srcContent
     * @param indentationNum
     */
    public static void indentationText(TextView textView, String srcContent, int indentationNum) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indentationNum; i++) {
            stringBuilder.append("缩");
        }
        SpannableStringBuilder span = new SpannableStringBuilder(stringBuilder.toString() + srcContent);
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, indentationNum,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(12f / 18f), 0, indentationNum,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }

    /**
     * 通常资源名字和类型获取id
     *
     * @param context
     * @param type
     * @param resourceName
     * @return
     */
    public static int getResourceId(Context context, String type, String resourceName) {
        try {
            ApplicationInfo appInfo = context.getApplicationInfo();
            int resID = context.getResources().getIdentifier(resourceName, type, appInfo.packageName);
            return resID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置输入框禁止输入汉字
     * @param editText
     */
    public static void editTextFilterChinese(EditText editText) {
        InputFilter[] filters = editText.getFilters();
        List<InputFilter> inputFilters = new ArrayList<>();
        InputFilter typeFilter = (source, start, end, dest, dstart, dend) -> {
            if (source != null && source.length() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < source.length(); i++) {
                    char c = source.charAt(i);
                    if (c < 0x4e00 || c > 0X9fff) {
                        stringBuilder.append(c);
                    }
                }
                return stringBuilder.toString();
            }
            return null;
        };
        inputFilters.add(typeFilter);
        if (filters != null && filters.length > 0) {
            for (InputFilter filter : filters) {
                inputFilters.add(filter);
            }
        }
        editText.setFilters(inputFilters.toArray(new InputFilter[0]));
    }

}
