package com.util;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {


    public static final String FORMAT_4y2M2d2H2m2s = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER_4y2M2d2H2m2s = DateTimeFormat.forPattern(FORMAT_4y2M2d2H2m2s).
            withZone(DateTimeZone.getDefault());

    public static final String FORMAT_4y2M2d2H2m2s3S = "yyyy-MM-dd HH:mm:ss SSS";
    public static final DateTimeFormatter FORMATTER_4y2M2d2H2m2s3S = DateTimeFormat.forPattern(FORMAT_4y2M2d2H2m2s3S).
            withZone(DateTimeZone.getDefault());


    public static final String FORMAT_4y2M2d = "yyyy-MM-dd";
    public static final DateTimeFormatter FORMATTER_4y2M2d = DateTimeFormat.forPattern(FORMAT_4y2M2d).
            withZone(DateTimeZone.getDefault());


    public static String get4y2M2d2H2m2s3S(Date date) {
        return LocalDateTime.fromDateFields(date).toString(FORMATTER_4y2M2d2H2m2s3S);
    }


    public static String getNowTime() {
        return get4y2M2d2H2m2s(new Date());
    }

    public static String get4y2M2d2H2m2s(Date date) {
        return LocalDateTime.fromDateFields(date).toString(FORMATTER_4y2M2d2H2m2s);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ThreadUtil.sleep(1);
            String y2M2d2H2m2sS = get4y2M2d2H2m2s3S(new Date());
            System.out.println(y2M2d2H2m2sS);
        }
    }

}
