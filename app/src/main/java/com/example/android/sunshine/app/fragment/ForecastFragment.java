package com.example.android.sunshine.app.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.sunshine.app.service.FetchWeatherTask;
import com.example.android.sunshine.app.model.Forecast;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.activity.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {

    ArrayAdapter mAdapter;

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
        List<String> forecasts = new ArrayList<>();

        mAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, forecasts);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), ((TextView) view).getText() , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, ((TextView) view).getText());
                //intent.setData(((TextView) view).getText());
                getActivity().startActivity(intent);
            }
        });
    }

    private String transformText(Forecast forecast) {
        return forecast.getDay() + " - " + forecast.getWeather() + " - " + forecast.getMax() + " / " +forecast.getMin();
    }
}