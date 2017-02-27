package com.example.android.sunshine.app.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.android.sunshine.app.fragment.SettingsFragment;

public class SettingsActivity extends PreferenceActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
            getFragmentManager().executePendingTransactions();
        }
    }
}
