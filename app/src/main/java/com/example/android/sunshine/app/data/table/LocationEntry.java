package com.example.android.sunshine.app.data.table;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.sunshine.app.data.WeatherContract;

public final class LocationEntry implements BaseColumns {

    public static final Uri CONTENT_URI = WeatherContract.BASE_CONTENT_URI.buildUpon().appendPath( WeatherContract.PATH_LOCATION).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + WeatherContract.CONTENT_AUTHORITY + "/" +  WeatherContract.PATH_LOCATION;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + WeatherContract.CONTENT_AUTHORITY + "/" +  WeatherContract.PATH_LOCATION;

    public static Uri buildLocationUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    //SQL
    public static final String TABLE_NAME = "location";

    public static final String COLUMN_LOCATION_SETTING = "location_setting";

    public static final String COLUMN_CITY_NAME = "city_name";

    public static final String COLUMN_COORD_LAT = "coord_lat";

    public static final String COLUMN_COORD_LONG = "coord_long";

}
