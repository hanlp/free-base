package com.util;

import com.google.common.collect.Lists;

import java.util.Date;

public class PrintUtil {

    private static final String key = "{}";
    private static final String regex = "(\\{\\})";

    public static void println(String msg, Object... args) {
        String resolveMsg = resolveMsg(msg, args);
        StringBuilder b = new StringBuilder()
                .append("[")
                .append(DateUtil.get4y2M2d2H2m2s3S(new Date()))
                .append("] ")
                .append(resolveMsg);
        System.out.println(b.toString());
    }

    private static String resolveMsg(String msg, Object... args) {
        try {
            if (msg == null) {
                return null;
            }
            if (msg.contains(key)) {
                for (Object a : args) {
                    msg = msg.replaceFirst(regex, a == null ? "null" : a.toString());
                }
                return msg;
            } else {
                return msg;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static void println(Object object) {
        if (object == null) {
            println(null, Lists.newArrayList().toArray());
        } else {
            println(object.toString(), Lists.newArrayList().toArray());
        }
    }
}
