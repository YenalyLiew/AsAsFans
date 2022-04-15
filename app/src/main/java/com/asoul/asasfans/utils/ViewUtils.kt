package com.asoul.asasfans.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.AttrRes
import com.fairhr.module_support.utils.ContextUtil

/**
 * 动态设置View的Margin。
 *
 * @param start 开始px
 * @param top 上px
 * @param end 结束px
 * @param bottom 下px
 */
fun View.setMargin(start: Int, top: Int, end: Int, bottom: Int) {
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(0, top, 0, bottom)
        p.marginStart = start
        p.marginEnd = end
        this.requestLayout()
    }
}

/**
 * 获取屏幕宽度
 *
 * @author Yenaly Liew
 */
val screenWidth: Int
    get() {
        return ContextUtil.getContext().resources.displayMetrics.widthPixels
    }

/**
 * dp to px
 *
 * @param dpValue dp value
 * @return px值
 */
fun dip2px(dpValue: Float): Int {
    val scale = ContextUtil.getContext().resources.displayMetrics.density
    return (dpValue * scale + 0.5F).toInt()
}

val Number.dp: Int
    get() {
        return dip2px(this.toFloat())
    }

fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}