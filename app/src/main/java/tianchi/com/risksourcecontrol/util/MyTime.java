package tianchi.com.risksourcecontrol.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTime {
    //获取时间
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        return time;
    }

    //获取时间戳
    public static String getTimeData() {
        Date date = new Date();
        return date.getTime() / 1000 + "";
    }

    //将时间戳转化为日期格式
    public static String Totime(String dataline) {
        if (dataline != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");/* HH:mm:ss*/
            String sd = sdf.format(new Date(Long.parseLong(dataline) * 1000));
            return sd;
        }
        return "";
    }


    //将字符串转化为时间戳
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 计算时间差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static String getTimeExpend(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long days;
        long hours;
        long minutes;
        long second;
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(startTime);
            d2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getSeconds() - d1.getSeconds();//这样得到的差值是秒级别

        days = diff / (60 * 60 * 24);
        hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
        minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / (60);
        second = (diff - days * (60 * 60 * 24) - hours * (60 * 60) - minutes * (60));

        return "" + days + "天" + hours + "小时" + minutes + "分" + second + "秒";
    }

    /**
     * 计算时间差
     *
     * @param time 时间（秒）
     * @return
     */
    public static String getTimeExpend(String time) {
        long days;
        long hours;
        long minutes;
        long second;

        long diff = Long.parseLong(time);//这样得到的差值是秒级别

        days = diff / (60 * 60 * 24);
        hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
        minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / (60);
        second = (diff - days * (60 * 60 * 24) - hours * (60 * 60) - minutes * (60));

        return "" + days + "天" + hours + "小时" + minutes + "分" + second + "秒";
    }

}