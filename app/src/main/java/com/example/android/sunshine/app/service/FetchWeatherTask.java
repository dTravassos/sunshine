package com.example.android.sunshine.app.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.android.sunshine.app.BuildConfig;
import com.example.android.sunshine.app.helpers.WeatherDataParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FetchWeatherTask extends AsyncTask<String, Integer, String[]> {

    ArrayAdapter mAdapter;

    Context context;

    public FetchWeatherTask(Context context, ArrayAdapter adapter) {
        this.mAdapter = adapter;
        this.context = context;
    }

    @Override
    protected String[] doInBackground(String... params) {

        if (params.length != 0) {
            return getForecast(params[0], params[1]);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] strings) {

        if(strings == null) { return; }

        mAdapter.clear();
        mAdapter.addAll(Arrays.asList(strings));
    }

    public String[] getForecast(String cityId, String unit) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try {
            Uri uri= Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily?")
                    .buildUpon()
                    .appendQueryParameter("id", cityId)
                    .appendQueryParameter("mode","json")
                    .appendQueryParameter("units", unit)
                    .appendQueryParameter("cnt","7")
                    .appendQueryParameter("APPID", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .build();


            //URL url = new URL(baseUrl.concat(apiKey));
            URL url = new URL(uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            forecastJsonStr = buffer.toString();
        } catch (Exception e) {
            Log.e("ForecastFragment", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }

        WeatherDataParser parser = new WeatherDataParser(context, unit);
        try {
            return parser.getWeatherDataFromJson(forecastJsonStr, 7);
        } catch (JSONException e) {
            Log.e("Task", e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

}
