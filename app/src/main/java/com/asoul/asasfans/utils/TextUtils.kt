package com.asoul.asasfans_concept.utils

/**
 * 把一个数转化为带万、亿的格式。
 *
 * 例如：102002 -> 10.2万
 *
 * @author Yenaly Liew
 */
fun Int.toViewsCase(): String {
    if (this / 1_0000 == 0) {
        return this.toString()
    }
    if (this / 1_0000 < 10) {
        return this.toString().substring(0, 1) + "." + this.toString().substring(1, 2) + "万"
    }
    if (this / 10_0000 < 10) {
        return this.toString().substring(0, 2) + "." + this.toString().substring(2, 3) + "万"
    }
    if (this / 100_0000 < 10) {
        return this.toString().substring(0, 3) + "." + this.toString().substring(3, 4) + "万"
    }
    if (this / 1000_0000 < 10) {
        return this.toString().substring(0, 4) + "." + this.toString().substring(4, 5) + "万"
    }
    if (this / 1_0000_0000 < 10) {
        return this.toString().substring(0, 1) + "." + this.toString().substring(1, 2) + "亿"
    }
    if (this / 10_0000_0000 < 10) {
        return this.toString().substring(0, 2) + "." + this.toString().substring(2, 3) + "亿"
    }
    return this.toString()
}

/**
 * 把一个分钟数转化为时间格式。
 *
 * 例如：123 -> 02:03
 *
 * @author Yenaly Liew
 */
fun Int.toDurationCase(): String {
    val second: Int = this % 60
    var minute: Int = this / 60
    var hour = 0
    if (minute >= 60) {
        hour = minute / 60
        minute %= 60
    }
    val secondString = if (second < 10) "0$second" else second.toString()
    val minuteString = if (minute < 10) "0$minute" else minute.toString()
    val hourString = if (hour < 10) "0$hour" else hour.toString()
    return if (hour != 0) "$hourString:$minuteString:$secondString" else "$minuteString:$secondString"
}