package com.fairhr.module_support.utils;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期工具类
 */

public class DateUtil {
    /**
     * 格式 - yyyy-MM-dd HH:mm:ss:SSS
     */
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    /**
     * 格式 - yyyy-MM-dd HH:mm:ss
     */
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式 - yyyy-MM-dd HH:mm
     */
    public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 格式 - yyyy-MM-dd
     */
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 格式 - yyyy-MM-dd
     */
    public static final String PATTERN_YYYY_MM = "yyyy-MM";
    /**
     * 格式 - MM-dd HH:mm
     */
    public static final String PATTERN_MM_DD_HH_MM = "MM-dd HH:mm";
    /**
     * 格式 - dd
     */
    public static final String PATTERN_DD = "dd";
    /**
     * 格式 - HH:mm:ss
     */
    public static final String PATTERN_HH_MM_SS = "HH:mm:ss";
    /**
     * 格式 - HH:mm:ss.SSS
     */
    public static final String PATTERN_HH_MM_SS_SSS = "HH:mm:ss.SSS";
    /**
     * 格式 - HH:mm
     */
    public static final String PATTERN_HH_MM = "HH:mm";

    /**
     * 格式 - yyyyMMddHHmmss
     */
    public static final String PATTERN_YYYYMMDD_HHMMSS = "yyyyMMddHHmmss";

    /**
     * 格式 - yyyyMMdd
     */
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    /**
     * 格式 - MM-dd
     */
    public static final String PATTERN_MM_DD = "MM-dd";
    public static final String PATTERN_MMDD = "MMdd";
    // 添加大小月月份并将其转换为list,方便之后的判断
    private static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private static String[] months_little = {"4", "6", "9", "11"};
    private static String[] WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 获得当前的年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前的月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前的日期天
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取指定格式的当前时间
     *
     * @param timeFormat 时间格式
     * @return
     */
    public static String getFormatCurrentTime(String timeFormat) {
        return new SimpleDateFormat(timeFormat, Locale.CHINA).format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取常用格式的时间字符串
     *
     * @param millis
     * @return
     */
    public static String formLocalTime(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * 获取指定格式的时间字符串
     *
     * @param pattern
     * @param millis
     * @return
     */
    public static String formLocalTime(String pattern, long millis) {
        Date date = new Date(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        String format = simpleDateFormat.format(date);
        return format;

    }

    /**
     * 获取指定格式的时间字符串
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String formLocalTime(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        String format = simpleDateFormat.format(date);
        return format;

    }

    /**
     * 获取指定格式的时间字符串
     *
     * @param oldPattern
     * @param date
     * @return
     */
    public static String formLocalTime(String oldPattern, String date, String newPattern) {
        try {
            return formLocalTime(newPattern, getDateFromDateString(date, oldPattern));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取Date对象
     *
     * @param dateString   时间字符串
     * @param formatString 时间格式
     * @return
     * @throws Exception
     */
    public static Date getDateFromDateString(String dateString, String formatString) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString, Locale.CHINA);
        return simpleDateFormat.parse(dateString);
    }

    /**
     * 通过给定的年、月、周获得该周内的每一天日期
     *
     * @param year  int 年
     * @param month int 月
     * @param week  int 周
     * @return List<Date> 七天的日期
     */
    public static List<Date> getDayByWeek(int year, int month, int week) {
        List<Date> list = new ArrayList<Date>();
        // 先滚动到该年.
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        // 滚动到月:
        c.set(Calendar.MONTH, month - 1);
        // 滚动到周:
        c.set(Calendar.WEEK_OF_MONTH, week);
        // 得到该周第一天:
        for (int i = 0; i < 6; i++) {
            c.set(Calendar.DAY_OF_WEEK, i + 2);
            list.add(c.getTime());
        }
        // 最后一天:
        c.set(Calendar.WEEK_OF_MONTH, week + 1);
        c.set(Calendar.DAY_OF_WEEK, 1);
        list.add(c.getTime());
        return list;
    }

    /**
     * 获得当前日期是本月的第几周
     *
     * @return int
     */
    public static int getCurWeekNoOfMonth() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获得当前日期是星期几
     *
     * @return int
     */
    public static int getCurWeekNo(String dat) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得当前日期是星期几
     *
     * @return int
     */
    public static int getCurWeekNo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 判断时间是否属于当天
     *
     * @param time
     * @return
     */
    public static boolean judgeTimeIsCurrentDate(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DATE) == getCurrentDay();
    }

    /**
     * 根据日期获取生肖
     *
     * @return
     */
    public static String date2Zodica(Calendar time) {
        String[] zodiacArr = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
        return zodiacArr[time.get(Calendar.YEAR) % 12];
    }


    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    public static String date2Constellation(Calendar time) {
        String[] constellationArr = {"水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
                "天蝎座", "射手座", "魔羯座"};
        int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        return constellationArr[11];
    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time 传进来的时间毫秒数 使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(Long time) {
        String display = ""; // 要显示的文字
        int tMin = 60 * 1000; // 1分钟的毫秒数
        int tHour = 60 * tMin; // 一个小时的毫秒数
        int tDay = 24 * tHour; // 一天的毫秒数
        if (time != 0) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA); // 最终显示的格式类型
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm", Locale.CHINA);
                Date tDate = new Date(time); // 传进来的时间的Date
                long tMill = tDate.getTime(); //传进来的时间的 毫秒
                Date today = new Date(); // 当前时间的Date
                SimpleDateFormat sdfToday = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//根据当前时间的Date得到今日0点的毫秒数
                long todayMill = sdfToday.parse(sdfToday.format(today)).getTime();
                if (tMill - todayMill >= 0) {
                    //今天
                    display = "今天" + sdf1.format(tDate);
                } else if ((tMill - todayMill < 0) && ((tMill - todayMill) >= (todayMill - tDay))) {
                    //昨天
                    display = "昨天" + sdf1.format(tDate);
                } else {
                    //其他
                    display = sdf.format(tDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return display;
    }

    /**
     * 获取指定年月的日期天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDateOfMonth(int year, int month) {
        if (Arrays.asList(months_big).contains(String.valueOf(month))) {
            return 31;
        } else if (Arrays.asList(months_little).contains(String.valueOf(month))) {
            return 30;
        } else {
            return isLeapYear(year) ? 29 : 28;
        }
    }


    /**
     * 获取指定年月的日期天数
     *
     * @return
     */
    public static int getDateOfMonth(Date date) {
        if (Arrays.asList(months_big).contains(String.valueOf(getMoth(date)))) {
            return 31;
        } else if (Arrays.asList(months_little).contains(String.valueOf(getMoth(date)))) {
            return 30;
        } else {
            return isLeapYear(getYears(date)) ? 29 : 28;
        }
    }

    /**
     * 是否为润年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }


    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static int getYears(Date date) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        return instance.get(Calendar.YEAR);
    }

    public static int getMoth(Date date) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        return instance.get(Calendar.MONTH) + 1;
    }

    public static String getMothchineseNum(Date date) {
        final String num[] = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        return num[instance.get(Calendar.MONTH)];
    }

    public static int getDay(Date date) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定时间前几年的时间
     *
     * @param date
     * @param beforYear
     * @return
     */
    public static Date getDateBeforYears(Date date, int beforYear) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.YEAR, -beforYear);
        return instance.getTime();
    }

    /**
     * 获取指定时间前几天的时间
     *
     * @param date
     * @param beforDay
     * @return
     */
    public static Date getDateBeforDay(Date date, int beforDay) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_YEAR, -beforDay);
        return instance.getTime();
    }

    /**
     * 获取指定天数的日期
     * @param date
     * @param day
     * @return
     */
    public static Date getDateByDay(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.DAY_OF_MONTH, day);
        return instance.getTime();
    }

    /**
     * 获取当日期的月份第一天
     *
     * @param date
     * @return
     */
    public static Date getDateOneBelongMoth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        return instance.getTime();
    }

    /**
     * 判断两个年份是否间隔指定年
     *
     * @param startDate
     * @param endDate
     * @param betweenYear
     * @return
     */
    public static boolean judgeDateBetweenYear(Date startDate, Date endDate, int betweenYear) {
        int years = getYears(endDate) - getYears(startDate);
        int moths = getMoth(endDate) - getMoth(startDate);
        int days = getDay(endDate) - getDay(startDate);
        if (years > betweenYear) {
            return false;
        } else if (years < betweenYear) {
            return true;
        } else {
            if (moths > 0) {
                return false;
            } else if (moths < 0) {
                return true;
            } else {
                if (days > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * 获取当前月的后一个月
     *
     * @param date
     * @return
     */
    public static Date getAfterMoth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        return instance.getTime();
    }

    /**
     * 获取当前月的前一个月
     *
     * @param date
     * @return
     */
    public static Date getBeforeMoth(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH, -1);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        return instance.getTime();
    }

    /**
     * 是否为今天
     *
     * @param dateString 日期字符串
     * @param pattern    格式
     * @return 是否为今天
     * @throws ParseException 日期字符串解析失败
     */
    public static boolean isToday(@NonNull String dateString, @NonNull String pattern)
            throws ParseException {
        Date date = parse(dateString, pattern);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return isSameDay(calendar, Calendar.getInstance(Locale.CHINA));
    }

    public static boolean isSameDay(@NonNull Calendar calOne, @NonNull Calendar calTwo) {
        int year = calOne.get(Calendar.YEAR);
        int month = calOne.get(Calendar.MONTH);
        int dayOfMonth = calOne.get(Calendar.DAY_OF_MONTH);

        int anotherYear = calTwo.get(Calendar.YEAR);
        int anotherMonth = calTwo.get(Calendar.MONTH);
        int anotherDayOfMonth = calTwo.get(Calendar.DAY_OF_MONTH);
        return year == anotherYear && month == anotherMonth && dayOfMonth == anotherDayOfMonth;
    }

    /**
     * 解析
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期
     * @throws ParseException 解析异常
     */
    @NonNull
    public static Date parse(@NonNull String dateStr, @NonNull String pattern)
            throws ParseException {
        return getSimpleDateFormat(pattern).parse(dateStr);
    }

    /**
     * 获得简单日期格式化
     *
     * @param pattern 格式
     * @return 简单日期格式化
     */
    @NonNull
    private static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA);
    }

    /**
     * 格式化
     *
     * @param date    日期
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(@NonNull Date date, @NonNull String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

    /**
     * 转换成时间戳
     * @return
     */
    public static long getTime(String dateTime,String pattern){
        long timestamp = 0;
        try {
            Date msgDate = DateUtil.parse(dateTime, pattern);
            timestamp =  msgDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static long getTimeMils(Date date,String pattern){
        return getTime(formLocalTime(pattern, date),pattern);
    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time 传进来的时间毫秒数 使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatTime(String time) {
        long time1 = getTime(time, PATTERN_YYYY_MM_DD_HH_MM_SS);
        String display = ""; // 要显示的文字
        int tMin = 60 * 1000; // 1分钟的毫秒数
        int tHour = 60 * tMin; // 一个小时的毫秒数
        int tDay = 24 * tHour; // 一天的毫秒数

        String shortstring = null;
        String time2 = timestampToStr(time1);
        Date date = getDateByString(time2);
        if(date == null) return shortstring;

        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (now - time1)/1000;
        if(deltime > 365*24*60*60) {
            shortstring = (int)(deltime/(365*24*60*60)) + "年前";
        } else if(deltime > 30*24*60*60){
            shortstring = (int)(deltime/(30*24*60*60)) + "月前";
        }else if(deltime > 24*60*60) {
            shortstring = (int)(deltime/(24*60*60)) + "天前";
        } else if(deltime > 60*60) {
            shortstring = (int)(deltime/(60*60)) + "小时前";
        } else if(deltime > 60) {
            shortstring = (int)(deltime/(60)) + "分前";
        } else if(deltime > 1) {
            shortstring = "刚刚";
        }
        return shortstring;
    }

    public static Date getDateByString(String time) {
        Date date = null;
        if (time == null)
            return date;
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //Timestamp转化为String:
    public static String timestampToStr(long dateline){
        Timestamp timestamp = new Timestamp(dateline*1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        return df.format(timestamp);
    }
}
