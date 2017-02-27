package com.example.android.sunshine.app.helpers;

import android.content.Context;
import android.text.format.Time;

import com.example.android.sunshine.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


public class WeatherDataParser {

    private Context ctx;

    private String unitType;

    public WeatherDataParser(Context ctx, String unitType) {
        this.ctx = ctx;
        this.unitType = unitType;
    }

    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex) throws JSONException {

        JSONObject object = new JSONObject(weatherJsonStr);

        JSONArray array = (JSONArray) object.get("list");

        JSONObject day = array.getJSONObject(dayIndex);

        JSONObject temp = day.getJSONObject("temp");

        Double max = (Double)temp.get("max");

        return max;
    }

    private String getReadableDateString(long time){
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    private String formatHighLows(double high, double low) {

        if (unitType.equals(ctx.getString(R.string.pref_unit_value_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        }

        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    public String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime;

            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);


            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        return resultStrs;

    }

}
