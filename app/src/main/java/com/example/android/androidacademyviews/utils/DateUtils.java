package com.example.android.androidacademyviews.utils;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String ago(Date dateTime) {
        Date now = new Date();
        int hour = dateTime.getHours();
        int min = dateTime.getMinutes();
        if (dateTime.getDate() == now.getDate()) {
            int diff = now.getHours() - dateTime.getHours();
            if (diff < 1) {
                diff = now.getMinutes() - dateTime.getMinutes();
                return String.format("%d min. ago, %02d:%02d", diff, hour, min);
            }
            return String.format("%d hr. ago, %02d:%02d", diff, hour, min);
        } else if (dateTime.getDate() == now.getDate() - 1) {
            return String.format("Yesterday, %02d:%02d", hour, min);
        } else {
            String monthName = new DateFormatSymbols(Locale.ENGLISH).getMonths()[dateTime.getMonth()];
            return String.format("%s %d, %02d:%02d", monthName, dateTime.getDate(), hour, min);
        }
    }
}
