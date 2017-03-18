package com.example.android.sunshine.app.data.table;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.sunshine.app.data.WeatherContract;

public final class WeatherEntry implements BaseColumns {

    public static final Uri CONTENT_URI = WeatherContract.BASE_CONTENT_URI.buildUpon().appendPath( WeatherContract.PATH_WEATHER).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/" + WeatherContract.CONTENT_AUTHORITY + "/" + WeatherContract.PATH_WEATHER;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/" + WeatherContract.CONTENT_AUTHORITY + "/" + WeatherContract.PATH_WEATHER;

    //SQL
    public static final String TABLE_NAME = "weather";

    public static final String COLUMN_LOC_KEY = "location_id";

    public static final String COLUMN_DATE = "date";

    public static final String COLUMN_WEATHER_ID = "weather_id";

    public static final String COLUMN_SHORT_DESC = "short_desc";

    public static final String COLUMN_MIN_TEMP = "min";

    public static final String COLUMN_MAX_TEMP = "max";

    public static final String COLUMN_HUMIDITY = "humidity";

    public static final String COLUMN_PRESSURE = "pressure";

    public static final String COLUMN_WIND_SPEED = "wind";

    public static final String COLUMN_DEGREES = "degrees";

    public static Uri buildWeatherUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static Uri buildWeatherLocation(String locationSetting) {
        return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
    }

    public static Uri buildWeatherLocationWithStartDate(
            String locationSetting, long startDate) {
        long normalizedDate = WeatherContract.normalizeDate(startDate);
        return CONTENT_URI.buildUpon().appendPath(locationSetting)
                .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
    }

    public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
        return CONTENT_URI.buildUpon().appendPath(locationSetting)
                .appendPath(Long.toString( WeatherContract.normalizeDate(date))).build();
    }

    public static String getLocationSettingFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public static long getDateFromUri(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(2));
    }

    public static long getStartDateFromUri(Uri uri) {
        String dateString = uri.getQueryParameter(COLUMN_DATE);
        if (null != dateString && dateString.length() > 0)
            return Long.parseLong(dateString);
        else
            return 0;
    }
}
