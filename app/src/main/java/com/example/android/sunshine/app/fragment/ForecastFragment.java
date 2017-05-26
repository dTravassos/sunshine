package com.example.android.sunshine.app.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.android.sunshine.app.adapter.ForecastAdapter;
import com.example.android.sunshine.app.data.table.WeatherEntry;
import com.example.android.sunshine.app.service.FetchWeatherTask;
import com.example.android.sunshine.app.model.Forecast;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.utils.Utility;

public class ForecastFragment extends Fragment {

    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_refresh == item.getItemId()) {
            loadWeather();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initAdapter(rootView);

        //loadWeather();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadWeather();
    }

    private void loadWeather() {
        FetchWeatherTask task = new FetchWeatherTask(getActivity().getApplicationContext(), mAdapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String location = preferences.getString(getString(R.string.prefs_location_key), getString(R.string.prefs_location_value_default));
        String unit = preferences.getString(getString(R.string.prefs_unit_key), getString(R.string.prefs_unit_value_default));

        task.execute(location, unit);
    }

    private void initAdapter(View rootView) {
        String locationSetting = Utility.getPreferredLocation(getActivity());
        String sortOrder = WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
    }

    private String transformText(Forecast forecast) {
        return forecast.getDay() + " - " + forecast.getWeather() + " - " + forecast.getMax() + " / " +forecast.getMin();
    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }
}