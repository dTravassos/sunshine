package com.example.android.sunshine.app.data;

import android.net.Uri;
import android.text.format.Time;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.sunshine.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "weather";

    public static final String PATH_LOCATION = "location";

    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }
}