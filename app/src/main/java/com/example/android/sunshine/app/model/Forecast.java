package com.example.android.sunshine.app.model;

public class Forecast {

    private String day;

    private String weather;

    private String min;

    private String max;

    public Forecast(String day, String weather, String min, String max) {
        this.day = day;
        this.weather = weather;
        this.min = min;
        this.max = max;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

}
