package com.uni.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {


    @AllArgsConstructor
    public enum FormatType{
        MONTH("yyyy-MM"),
        SIMPLE("yyyy-MM-dd"),
        COMMON("yyyy-MM-dd HH:mm:ss"),
        BCD("yyMMddHHmmss");
        @Setter@Getter
        private String type;
    }

    /**
     * 当前时间转换为少8小时的Iot上报的时间
     * @return
     */
    public static Date transferIotDate() {
        Date date = new Date();//入库时间
        long localTime = date.getTime();
        long localOffset=date.getTimezoneOffset()*60000; //获得当地时间偏移的毫秒数
        long utc = localTime - localOffset; //utc即GMT时间
        long offset =16; //以北京时间为例，东8区
        long beijing = utc - (3600000*offset);
        date = new Date(beijing);
        return date;
    }
    /**
     * 存入mongo的时间加8小时
     * @return
     */
    public static Date transferMongoDate() {
    	Date date = new Date();//入库时间
        long localTime = date.getTime();
        long localOffset=date.getTimezoneOffset()*60000; //获得当地时间偏移的毫秒数
        long utc = localTime + localOffset; //utc即GMT时间
        long offset =16; //以北京时间为例，东8区
        long beijing = utc + (3600000*offset);
        date = new Date(beijing);
        return date;
    }

    /**
     * 得到当前时间
     * @param formatType
     * @return
     */
    public static String getCurrentDateStr(FormatType formatType) {
        if (formatType == null) {
            formatType = FormatType.SIMPLE;
        }
        return new SimpleDateFormat(formatType.getType()).format(new Date());
    }
    /**
     * str转为date
     * @param dateStr
     * @param formatType
     * @return
     */
    public static Date parse(String dateStr, FormatType formatType) {
        if (formatType == null) {
            formatType = FormatType.SIMPLE;
        }
        try {
            return new SimpleDateFormat(formatType.getType()).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    /**
     * 日期转为str
     * @param date
     * @param formatType
     * @return
     */
    public static String format(Date date, FormatType formatType) {
        if (formatType == null) {
            formatType = FormatType.SIMPLE;
        }
        return new SimpleDateFormat(formatType.getType()).format(date);
    }
    /**
     * 日期转为str
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date,String format) {
        if (format == null) {
            format = FormatType.SIMPLE.getType();
        }
        return new SimpleDateFormat(format).format(date);
    }
    /**
     * 两个时间对减 date1-date2
     * @param date1
     * @param date2
     * @return
     */
    public static long compare(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }
    /**
     * 往后推移几天
     * @param date
     * @param days  负数向前移动 正数向后移动
     * @return
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            date = new Date();
        }
        return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
    }
    /*
     * 20181228T083341Z格式装成utc格式：2018-12-28T08:33:41Z
     * */
    public static String getDate(String date) {
        date=date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8)+"T"+
                date.substring(9,11)+":"+date.substring(11,13)+":"+date.substring(13,15)+"Z";
        return date;
    }
    /*
     * 2018-12-28T08:33:41Z格式的utc时间转默认GMT时间2018-12-28 16:33:51
     * 默认为GMT时区，显示的时间会加上本地时区的偏移（8 小时）
     * */
    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));// 时区定义并进行时间获取
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }
    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null){
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    /**
     * 日期时间（LocalDateTime）按指定格式转字符串
     *
     * @param value
     * @param format
     * @return
     */
    public static String localDateTimeToStr(LocalDateTime value, String format) {
        String dateString;
        if (value == null) {
            dateString = "";
        } else {
            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern(format);
            dateString = value.format(formatDate);
        }

        return dateString;
    }

    /**
     * 日期转星期
     *
     * @param date
     * @return
     */
    public static String dateToWeek(String date) {
        // 获得一个日历
        Calendar cal = Calendar.getInstance();
        Date datet = DateUtils.parse(date, FormatType.SIMPLE);
        try {
            cal.setTime(datet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 指示一个星期中的某天。
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        switch (w){
            case 0:
                return "星期天";
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            default:
                return "";
        }
    }
}
