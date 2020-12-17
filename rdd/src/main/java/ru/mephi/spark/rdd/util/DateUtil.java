package ru.mephi.spark.rdd.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date roundMinutes(Date date, Integer scale) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % scale;
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, unroundedMinutes - mod);
        Date result = calendar.getTime();

        return result;
    }

    public static Date roundSeconds(Date date, Integer scale) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int unroundedSeconds = calendar.get(Calendar.SECOND);
        int mod = unroundedSeconds % scale;
        calendar.set(Calendar.SECOND, unroundedSeconds - mod);
        calendar.set(Calendar.MILLISECOND, 0);
        Date result = calendar.getTime();

        return result;
    }

    public static Date getWindowStartMinutes(Calendar now, Integer window) {
        Calendar calendar = (Calendar) now.clone();
        calendar.add(Calendar.MINUTE, -window);
        return calendar.getTime();
    }

    public static Date getWindowEndMinutes(Calendar now, Integer window) {
        Calendar calendar = (Calendar) now.clone();
        calendar.add(Calendar.MINUTE, window);
        return calendar.getTime();
    }

    public static Date getWindowStartSeconds(Calendar now, Integer window) {
        Calendar calendar = (Calendar) now.clone();
        calendar.add(Calendar.SECOND, -window);
        return calendar.getTime();
    }

    public static Date getWindowEndSeconds(Calendar now, Integer window) {
        Calendar calendar = (Calendar) now.clone();
        calendar.add(Calendar.SECOND, window);
        return calendar.getTime();
    }
}
