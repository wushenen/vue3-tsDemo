package com.unicom.quantum.component.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by duhc on 2017/4/18.
 */
public class DateUtils {
private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
    private static SimpleDateFormat dateStr = new SimpleDateFormat("yyyyMMddHHmmss");

    private static SimpleDateFormat datetime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat datetime1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat datetime2 = new SimpleDateFormat(
            "yyyyMMddHH:mm:ss");
//    private static SimpleDateFormat formatZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
private static SimpleDateFormat formatZ ;
static{
    formatZ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    formatZ.setTimeZone(TimeZone.getTimeZone("GMT+8"));
}

    public static Date getNowDate() {
        return new Date();
    }
    // ****************************当前时间相关****************************
    public static String getZTimeStr(Date date){
        if(date ==null){
            return "";
        }
        return formatZ.format(date);
    }
    /**
     * 获得以 yyyy-MM-dd 为形式的当前时间字符串
     *
     * @return String
     */
    public static String getCurrentTimeByDay() {
        String time = date.format(new Date(System.currentTimeMillis()));
        return time;
    }
    /**
     * 获得以 yyyyMMddhhmmss 为形式的当前时间字符串
     *
     * @return String
     */
    public static String getCurrentTime(int len) {
    	String time = dateStr.format(new Date(System.currentTimeMillis()));
    	time = time.substring(0,len);
        return time;
    }
    /**
     * yyyyMMddhhmmss 轉為yyyy-MM-dd字符串
     *
     * @return String
     * @throws ParseException 
     */
    public static String toTime(String t) throws ParseException {
    	Date date2 = dateStr.parse(t); //注意:指定的字符串格式必须要与SimpleDateFormat的模式要一致。
    	String time = date.format(date2);
        return time;
    }
    /**
     * 获得以 yyyy-MM-dd HH:mm:ss 为形式的当前时间字符串
     *
     * @return String
     */
    public static String getCurrentTimeBySecond() {
        String time = datetime.format(new Date(System.currentTimeMillis()));
        return time;
    }

    /**
     * 获得给定格式的当前时间字符串
     *
     * @param give String 给定的时间格式
     * @return String
     */
    public static String getCurrentTime(String give) {
        SimpleDateFormat temp = new SimpleDateFormat(give);
        return temp.format(new Date(System.currentTimeMillis()));
    }

    // ****************************String转换为Date****************************

    /**
     * 将String转化成date
     *
     * @throws ParseException
     */
    public static Date pStringToDate(String str, String sfgs)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(sfgs);
        return sf.parse(str);
    }

    /**
     * 将String转化成date 格式为yyyy-MM-dd hh:mm:ss
     *
     * @throws ParseException
     */
    public static Date pStringToDate(String str) throws ParseException {
        return datetime.parse(str);
    }
    /**
     * 将String转化成date 格式为yyyyMMdd hh:mm:ss
     *
     * @throws ParseException
     */
    public static Date pStringToDate2(String str) throws ParseException {
        return datetime2.parse(str);
    }

    // ****************************Date转换为String****************************

    /**
     * 转换成日期格式的字符串 格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String dateFormat(Date o) {
        if (o == null) {
            return "";
        }
        return date.format(o);
    }

    /**
     * 转换成时间格式的字符串 格式为yyyy-MM-dd hh:mm:ss
     *
     * @return String
     */
    public static String dateTimeFormat(Date o) {
        if (o == null) {
            return "";
        }
        return datetime.format(o);
    }
    /**
     * 转换成时间格式的字符串 格式为yyyy-MM-dd hh:mm:ss
     *
     * @return String
     */
    public static String dateTimeFormat2(Date o) {
        if (o == null) {
            return "";
        }
        return datetime2.format(o);
    }

    /**
     * 转换成给定时间格式的字符串
     *
     * @return String
     */
    public static String getDateFormat(Date d, String format) {
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 日期格式化(yyyy年MM月dd日)
     *
     * @return String
     */
    public static String fDateCNYR(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日");
    }

    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm)
     *
     * @return String
     */
    public static String fDateCNYRS(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日 HH点");
    }

    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm)
     *
     * @return String
     */
    public static String fDateCNYRSF(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日 HH:mm");
    }

    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm:ss)
     *
     * @return String
     */
    public static String fDateCNYRSFM(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日 HH:mm:ss");
    }

    // ****************************时间格式的String转换为String****************************

    /**
     * 根据给定的时间格式字符串截取给定格式的字符串
     *
     * @param d      String 给定时间格式为yyyy-MM-dd HH:mm:ss
     * @param format String 给定的格式
     * @return String
     */
    public static String getDateFormat(String d, String format)
            throws ParseException {
        Date date = datetime.parse(d);
        return getDateFormat(date, format);
    }

    // ****************************时间格式的String转换为long****************************

    /**
     * 通过字符串获得long型时间
     *
     * @return long
     */
    public static long getDateFromStr(String dateStr) {
        long temp = 0L;
        Date date = null;
        try {
            date = datetime.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return temp;
        }
        temp = date.getTime();
        return temp;
    }

    // ****************************Date转换为给定格式的Date****************************

    /**
     * 日期格式化（2014-03-04）
     *
     * @return Date
     * @throws ParseException
     */
    public static Date fDate(Date dat) throws ParseException {
        String dateStr = date.format(dat);
        return date.parse(dateStr);
    }

    /**
     * 通过开始时间和间隔获得结束时间。
     *
     * @return String
     */
    public static String getEndTime(String start, int span) {
        if (isNullOrNone(start) || span == 0) {
            return null;
        }
        long temp = getDateFromStr(start);
        temp += span * 60L * 1000L;
        return datetime.format(new Date(temp));
    }

    /**
     * 格式化字符串，将2013-10-20 00:00:00.000000简化为2013-10-20 00:00:00
     *
     * @return String
     * @throws ParseException
     */
    public static String getFormatStringDay(String str) throws ParseException {
        Date date = datetime.parse(str);
        return datetime.format(date);
    }

    /**
     * 判断是否为空
     *
     * @return boolean
     */
    public static boolean isNullOrNone(String src) {
        if (null == src || "".equals(src)) {
            return true;
        }
        return false;
    }

    /**
     * 如果字符串长度大于25则截取前25个字符串后续改成省略号
     *
     * @return String
     */
    public static String showCount(String str) {
        if (str != null) {
            if (str.length() > 25) {
                str = str.substring(0, 25);
                str = str + "...";
            }
        } else {
            str = "";
        }
        return str;
    }

    /**
     * 是否符合日期格式yyyy-MM-dd
     *
     * @param day String 日期字符串
     * @return boolean
     */
    public static boolean isFormatDay(String day) {
        return day
                .matches("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
    }

    /**
     * 是否符合时间格式HH:mm:ss
     *
     * @param time String 时间字符串
     * @return boolean
     */
    public static boolean isFormatTime(String time) {
        return time
                .matches("(0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])(\\.000000)?");
    }

    /**
     * 是否符合时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param time String 时间字符串
     * @return boolean
     */
    public static boolean isFormat(String time) {
        String[] temp = time.split(" ");
        return isFormatDay(temp[0]) && isFormatTime(temp[1]);
    }
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
     * 获取当月最后一天
     *
     * @return String
     */
    public static String lastDayOfMoth(Date date, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        date = cal.getTime();
        ;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }
    /**
     * 获取当前日期前一天
     *
     * @return Date
     */
    public static Date getSpecifiedDayBefore(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        date = c.getTime();
        return date;
    }
    /**
     * 比较两个日期的大小
     *
     * @return boolean
     * @author zhangss 2016-5-18 13:47:16
     */
    public boolean bjDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return true;
        }
        return false;
    }
    /**
     * 相隔天数
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getDayDiffer(Date startDate, Date endDate) throws ParseException {
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        return (int)  ((endDateTime - startDateTime) / (1000 * 3600 * 24));
    }
    /**
     *格式为integer[unit]，其中integer表示时间长度，unit表示时间单位。
     * 合法的unit单位为：d（天）、h（小时）、m（分钟）、s（秒）。7d或者604800s均表示7天的周期。取值：7~730天。
     * @param integerUnit
     * @return
     * @throws Exception
     */
    public static Long integerUnit(String integerUnit) throws Exception {
        Long time = 0L;
        Integer integer = Integer.parseInt(integerUnit.substring(0,integerUnit.length()-1));
        String unit = integerUnit.substring(integerUnit.length()-1);
        switch (unit){
            case "d":
                time = integer * 24 * 60 * 60 *1000L;
                break;
            case "h":
                time = integer * 60 * 60 *1000L;
                break;
            case "m":
                time = integer * 60 *1000L;
                break;
            case "s":
                time = integer *1000L;
                break;
        }
        if(time < 7*24*60*60*1000L || time > 730*24*60*60*1000L){
            time = 0L;
        }
        return time;
    }

}
