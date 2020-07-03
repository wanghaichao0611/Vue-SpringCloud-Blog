package com.article.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTo {

    /**
     * 获取当天0点0分0秒（00:00:00）
     *
     * @return
     */
    public static Date getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = cal.getTime();
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //return formatter.format(beginOfDate);
        return beginOfDate;
    }

    //获取当天23:59:59
    public static Date getTonight(){
        Calendar calendar2 = Calendar.getInstance();

        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        Date endOfDate = calendar2.getTime();
        return endOfDate;
     }

    //获取上一年这个时间用于测试
    public static Date lastYear(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -1);
        Date last = cal.getTime();
        return last;
    }

    //获取当前与明日凌晨相差的秒数
    public static Long getSeconds() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    //减少一天的结果
    public static Date getTimeFrom(Date from){
        //获取时间
        Calendar cal = Calendar.getInstance();
        //设置起始时间
        cal.setTime(from);
        //减少一天
        cal.add(Calendar.DATE, -1);
        //返回减少一天的结果
        from = cal.getTime();
        return from;
    }

}
