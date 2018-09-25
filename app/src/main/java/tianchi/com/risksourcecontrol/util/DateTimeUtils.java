package tianchi.com.risksourcecontrol.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间、日期各类型装换工具类
 * Created by Kevin on 2017/12/22.
 */

public class DateTimeUtils {
    public static final String FULL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String FULL_TIME_FORMAT = " HH:mm:ss";
    public static final String FULL_DATE_TIME_CHN_FORMAT = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * 设置当前的默认时间
     *
     * @return String "yyyy-MM-dd"
     * @datetime 2018/2/8  9:39.
     */
    public static String setCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    /**
     * 设置当前的默认时间
     *
     * @return date
     * @datetime 2018/2/8  9:39.
     */
    public static Date getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date date, String formatType) {
        return new SimpleDateFormat(formatType).format(date);
    }


    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
//        ParsePosition position = new ParsePosition(8);
        Date date = formatter.parse(strTime);
        return date;
    }

    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate2(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
//        ParsePosition position = new ParsePosition(8);
//        int i = strTime.length();
        Date date = formatter.parse(strTime);
        return date;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static String longToDate2(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
//        Date date = stringToDate2(sDateTime, formatType); // 把String类型转换为Date类型
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = formatter.parse(sDateTime);
        return sDateTime;
    }


    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

}
