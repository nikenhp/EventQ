package com.niken.eventq.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormator {
    private static String format(String date, String format) {
        String result = "";

        DateFormat old = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date oldDate = old.parse(date);
            DateFormat newFormat = new SimpleDateFormat(format);
            result = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDate(String date) {
        return format(date, AppConfig.DATE_FORMAT);
    }

    public static String getDateDay(String date) {
        return format(date, AppConfig.DATE_FORMAT_DAY);
    }
}
