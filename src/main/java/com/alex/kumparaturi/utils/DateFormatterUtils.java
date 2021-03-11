package com.alex.kumparaturi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatterUtils {
    public static String getNowFormatted() {
        Date now = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowFormated = DATE_FORMAT.format(now);

        return nowFormated;
    }
}
