package com.fairhr.module_support.utils;

import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *数据格式化类
 */
public class NumberUtils {
    public static final double EPISON = 10E-6;

    private static Pattern pattern = Pattern.compile("^[-\\+]?\\d*[.]\\d+$");

    private NumberUtils() {

    }

    /**
     * 解析整形数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 整形数(如果字符串不是一个整形数, 则会返回默认值)
     */
    public static int parseInt(@Nullable String str, int defaultValue) {
        int result;
        try {
            result = parseInt(str);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 解析整形数
     *
     * @param str 字符串
     * @return 整形数
     * @throws NumberFormatException 字符串不是一个整形数
     */
    public static int parseInt(@Nullable String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    /**
     * 解析长整型数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 长整型数(如果字符串不是一个长整型数, 则会返回默认值)
     */
    public static long parseLong(@Nullable String str, long defaultValue) {
        long result;
        try {
            result = parseLong(str);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 解析长整型数
     *
     * @param str 字符串
     * @return 长整型数
     * @throws NumberFormatException 字符串不是一个长整形数
     */
    public static long parseLong(@Nullable String str) throws NumberFormatException {
        return Long.parseLong(str);
    }

    /**
     * 解析浮点数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 浮点数(如果字符串不是一个浮点数, 则会返回默认值)
     */
    public static float parseFloat(@Nullable String str, float defaultValue) {
        float result;
        try {
            result = parseFloat(str);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 解析浮点数
     *
     * @param str 字符串
     * @return 浮点数
     * @throws NumberFormatException 字符串不是一个浮点数
     */
    public static float parseFloat(@Nullable String str) throws NumberFormatException {
        if (TextUtils.isEmpty(str)) {
            throw new NumberFormatException("字符串不是一个浮点数");
        } else {
            return Float.parseFloat(str);
        }
    }

    /**
     * 解析双精度浮点数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 双精度浮点数(如果字符串不是一个双精度浮点数, 则会返回默认值)
     */
    public static double parseDouble(@Nullable String str, double defaultValue) {
        double result;
        try {
            result = parseDouble(str);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 解析双精度浮点数
     *
     * @param str 字符串
     * @return 双精度浮点数
     * @throws NumberFormatException 字符串不是一个双精度浮点数
     */
    public static double parseDouble(@Nullable String str) throws NumberFormatException {
        if (TextUtils.isEmpty(str)) {
            throw new NumberFormatException("字符串不是一个双精度浮点数");
        } else {
            return Double.parseDouble(str);
        }
    }


    /**
     * 解析双精度浮点数
     *
     * @param str 字符串
     * @return 双精度浮点数
     * @throws NumberFormatException 字符串不是一个双精度浮点数
     */
    public static double parseDoubleS(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return 0.00;
        } else {
            return Double.parseDouble(str);
        }
    }


    /**
     * 格式化整形数
     *
     * @param value         值
     * @param digitCapacity 位数(值少于该位数时前端以0补齐; 值多于该位数时, 则不会补0, 但也不会截断值;)
     * @return 格式化字符串
     */
    @NonNull
    public static String formatInt(int value, int digitCapacity) {
        String result;
        String format;
        if (digitCapacity > 0) {
            format = String.format("%%0%dd", digitCapacity);
        } else {
            format = "%d";
        }
        result = String.format(Locale.CHINA, format, value);
        return result;
    }

    /**
     * 格式化双精度浮点数
     *
     * @param valueString  值字符串
     * @param precision    精度
     * @param defaultValue 默认值
     * @return 格式化字符串(如果字符串不是一个双精度浮点数, 则会返回默认值)
     */
    @NonNull
    public static String formatDouble(@Nullable String valueString,
                                      @IntRange(from = 0, to = Integer.MAX_VALUE) int precision,
                                      @NonNull String defaultValue) {
        @NonNull String result;
        try {
            result = formatDouble(valueString, precision);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 格式化双精度浮点数
     *
     * @param valueString 值字符串
     * @param precision   精度
     * @return 格式化字符串
     * @throws NumberFormatException 值字符串不是一个双精度浮点数
     */
    @NonNull
    public static String formatDouble(@Nullable String valueString,
                                      @IntRange(from = 0, to = Integer.MAX_VALUE) int precision)
            throws NumberFormatException {
        double value = parseDouble(valueString);
        return formatDouble(value, precision);
    }

    @NonNull
    public static double formatDouble(@Nullable String valueString) {
        double value = parseDouble(valueString);
        return value;
    }

    /**
     * 格式化双精度浮点数
     *
     * @param value     值
     * @param precision 精度
     * @return 格式化字符串
     */
    @NonNull
    public static String formatDouble(double value,
                                      @IntRange(from = 0, to = Integer.MAX_VALUE) int precision) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(precision);
        format.setMinimumFractionDigits(precision);
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);
        return format.format(value);
    }

    /**
     * 格式化双精度浮点数
     *
     * @param value     值
     * @param precision 精度
     * @return 格式化字符串
     */
    @NonNull
    public static String formatDouble(double value,
                                      @IntRange(from = 0, to = Integer.MAX_VALUE) int precision, RoundingMode roundingMode) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(precision);
        format.setMinimumFractionDigits(precision);
        format.setRoundingMode(roundingMode);
        format.setGroupingUsed(false);
        return format.format(value);
    }

    /**
     * 格式化双精度浮点数为百分数
     *
     * @param valueString  值字符串
     * @param precision    精度
     * @param defaultValue 默认值
     * @return 格式化字符串
     */
    @NonNull
    public static String formatPercentage(@Nullable String valueString,
                                          @IntRange(from = 0, to = Integer.MAX_VALUE) int precision,
                                          @NonNull String defaultValue) {
        @NonNull String result;
        try {
            result = formatPercentage(valueString, precision);
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 格式化双精度浮点数为百分数
     *
     * @param valueString 值字符串
     * @param precision   精度
     * @return 格式化字符串
     * @throws NumberFormatException 值字符串不是一个双精度浮点数
     */
    @NonNull
    public static String formatPercentage(@Nullable String valueString,
                                          @IntRange(from = 0, to = Integer.MAX_VALUE) int precision)
            throws NumberFormatException {
        double value = parseDouble(valueString);
        return formatPercentage(value, precision);
    }

    /**
     * 格式化双精度浮点数
     *
     * @param valueString 值字符串
     * @param precision   精度
     * @return 格式化字符串
     * @throws NumberFormatException 值字符串不是一个双精度浮点数
     */
    @NonNull
    public static String formatOriginalPercentage(@Nullable String valueString,
                                          @IntRange(from = 0, to = Integer.MAX_VALUE) int precision)
            throws NumberFormatException {
        double value = parseDouble(valueString);
        return formatDouble(value, precision);
    }

    /**
     * 格式化双精度浮点数为百分数
     *
     * @param value     值
     * @param precision 精度
     * @return 格式化字符串
     */
    @NonNull
    public static String formatPercentage(double value,
                                          @IntRange(from = 0, to = Integer.MAX_VALUE) int precision) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(precision);
        format.setMinimumFractionDigits(precision);
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(false);
        return format.format(value);
    }

    /**
     * 判断双精度浮点数是否为0
     *
     * @param value 值
     * @return 双精度浮点数是否为0
     */
    public static boolean isDoubleMax(double value) {
        return Double.MAX_VALUE == value;
    }

    /**
     * 判断双精度浮点数是否为0
     *
     * @param value 值
     * @return 双精度浮点数是否为0
     */
    public static boolean isDoubleZero(double value) {
        return doubleEquals(value, 0);
    }

    /**
     * 判断双精度浮点数是否相等
     *
     * @param value1 值1
     * @param value2 值2
     * @return 是否相等
     */
    public static boolean doubleEquals(double value1, double value2) {
        double deltaValue = value1 - value2;
        return -10E-6 < deltaValue && deltaValue < 10E-6;
    }

    /**
     * 判断双精度浮点数是否小于等于0
     *
     * @param value1 值1
     * @return 是否相等
     */
    public static boolean isDoubleLessThanOrEqualZero(double value1) {
        double deltaValue = value1 - 0d;
        return deltaValue < 10E-6;
    }

    /**
     * 最大double值字符串
     *
     * @param integerDigits 整数位数
     * @param precision     精度
     * @return 最大double值字符串
     */
    @NonNull
    public static String maxDoubleValueString(
            @IntRange(from = 1, to = Integer.MAX_VALUE) int integerDigits,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int precision) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < integerDigits; i++) {
            resultBuilder.append('9');
        }
        if (precision > 0) {
            resultBuilder.append('.');
            for (int i = 0; i < precision; i++) {
                resultBuilder.append('9');
            }
        }
        return resultBuilder.toString();
    }


    public static int getNumSpacing(int value, int step) {
        int spacing = value / step;
        int baseValue = 1;
        if (spacing >= 10)
            for (int i = 1; i < (spacing + "").length(); i++) {
                baseValue *= 10;
            }
        for (int i = step; i < step + 20; i++) {
            if ((spacing / baseValue * baseValue * i) >= value) {
                return spacing / baseValue * baseValue * i;
            }
        }
        return value;
    }

    public static long getNumSpacing(long value, int step) {
        long spacing = value / step;
        int baseValue = 1;
        if (spacing >= 10)
            for (int i = 1; i < (spacing + "").length(); i++) {
                baseValue *= 10;
            }
        for (int i = step; i < step + 20; i++) {
            if ((spacing / baseValue * baseValue * i) >= value) {
                return spacing / baseValue * baseValue * i;
            }
        }
        return value;
    }

    /**
     * 格式化个数
     *
     * @param rmb
     * @return
     */
    public static String formatNum(double rmb, int keepNum) {
        if (Math.abs(rmb / 100000000) >= 1) {
            return formatDouble(rmb / 100000000, keepNum) + "亿";
        } else if (Math.abs(rmb / 10000) >= 1) {
            return formatDouble(rmb / 10000, keepNum) + "万";
        } else if (rmb == 0) {
            return "0";
        } else {
            return formatDouble(rmb, keepNum) + "";
        }
    }

    /**
     * 格式化个数
     *
     * @param rmb
     * @return
     */
    public static String formatNum(double rmb, int keepNum, String unitType) {
        if (Math.abs(rmb / 100000000) >= 1) {
            return formatDouble(rmb / 100000000, keepNum) + "亿" + unitType;
        } else if (Math.abs(rmb / 10000) >= 1) {
            return formatDouble(rmb / 10000, keepNum) + "万" + unitType;
        } else if (rmb == 0) {
            return "0.00";
        } else {
            return formatDouble(rmb, keepNum) + "";
        }
    }

    /**
     * 格式化个数
     *
     * @param rmb
     * @return
     */
    public static String formatNum(int rmb, int keepNum, String unitType) {
        if (Math.abs(rmb / 100000000) >= 1) {
            return formatDouble((float) rmb / 100000000, keepNum) + "亿" + unitType;
        } else if (Math.abs(rmb / 10000) >= 1) {
            return formatDouble((float) rmb / 10000, keepNum) + "万" + unitType;
        } else if (rmb == 0) {
            return "0";
        } else {
            return rmb + "";
        }
    }

    /**
     * @param percent
     * @param keepNum
     * @return
     */
    public static String formatPercent(double percent, int keepNum) {
        String formatDouble = formatDouble(percent, keepNum);
        return percent == 0 ? "0.00%" : formatDouble + "%";
    }


    /**
     * 转中文数字
     *
     * @param src
     * @return
     */
    public static String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String unit[] = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String dst = "";
        int count = 0;
        while (src > 0) {
            dst = (num[src % 10] + unit[count]) + dst;
            src = src / 10;
            count++;
        }
        return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }

    public static boolean isDouble(String str) {
        if (str.isEmpty()) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    public static double formatPositionData(long num,int keepNum){
        if(num / 10000 > 0){
            String s = formatDouble(num / 10000, keepNum);
            return formatDouble(s);
        }else{
            return formatDouble(formatDouble(num,keepNum));
        }
    }

    public static String formatStringPercent(String num,int keepNum){
        if(TextUtils.isEmpty(num)){
            return "--";
        }else{
           return formatDouble(num,keepNum);
        }
    }

    public static String formatIndexValue(String num, int precision){
        String indexValue;
        if(TextUtils.isEmpty(num)){
            indexValue = "--";
        }else{
            double v = formatDouble(num);
            if (v < - NumberUtils.EPISON) {
                //<0
                indexValue = formatDouble(num,precision);

            } else if (v < NumberUtils.EPISON) {
                //=0
                indexValue = formatDouble(num,precision);
            } else {
                indexValue = "+" + formatDouble(num,precision);
            }
        }
        return indexValue;
    }

    public static boolean isMoreThanZero(double v){
        boolean isMoreThanZero = false;
        if (v < - NumberUtils.EPISON) {
            //<0
            isMoreThanZero = false;
        } else if (v < NumberUtils.EPISON) {
            //=0
            isMoreThanZero = false;
        } else {
            isMoreThanZero = true;
        }
        return isMoreThanZero;
    }

    public static boolean isNumeric(String str){

        for (int i = str.length();--i>=0;){

            if (!Character.isDigit(str.charAt(i))){

                return false;

            }

        }
        return true;

    }

    /**
     * 格式化十大股东数据
     *
     * @param rmb
     * @return
     */
    public static String formatHolderNum(double rmb, int keepNum) {
        if (Math.abs(rmb / 100000000) >= 1) {
            if(rmb % 100000000 == 0){
                return formatDouble(rmb / 100000000, 0) + "亿";
            }else{
                return formatDouble(rmb / 100000000, keepNum) + "亿";
            }
        } else if (Math.abs(rmb / 10000) >= 1) {
            if(rmb % 10000 == 0){
                return formatDouble(rmb / 10000, 0) + "万";
            }else{
                return formatDouble(rmb / 10000, keepNum) + "万";
            }
        } else if (rmb == 0) {
            return "0";
        } else {
            return formatDouble(rmb, keepNum) + "";
        }
    }

    public static boolean isNum(String num){
        if(num.startsWith("-")){
            return true;
        }else{
            return  num.matches("^[0-9]+(.[0-9]+)?$");
        }
    }

    public static String formatNum(double num){
        double stt = 999999999999.3344;
        DecimalFormat format = new DecimalFormat("##0.00");//不以科学计数法显示，并把结果用逗号隔开保留两位小数
        BigDecimal bigDecimal = new BigDecimal(stt);//不以科学计数法显示，正常显示保留两位小数
        return format.format(num) + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
//        tv.setText("" + format.format(num) + "\n" + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP) + "");


//        DecimalFormat df = new DecimalFormat("0");
//        Double d = new Double(num);
//        return df.format(d);
    }

}
