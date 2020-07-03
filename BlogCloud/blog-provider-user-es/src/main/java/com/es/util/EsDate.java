package com.es.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EsDate {

    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    public static final String simple                  = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss,字符串
     * @return
     */
    public  static String getDateFormatter(Date date){
        DateFormat df=new SimpleDateFormat(simple);
        return df.format(date);
    }

    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss,日期
     * @return
     */
    public static Date getDate(String time){
        Date date=new Date();
       try {
           date = new SimpleDateFormat(simple).parse(time);
       }catch (ParseException e){
           e.printStackTrace();
       }
       return date;
    }
}
