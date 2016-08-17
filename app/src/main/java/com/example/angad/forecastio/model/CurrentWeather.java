package com.example.angad.forecastio.model;

public class CurrentWeather {
    private  String mIcon;
    private  String mLocation;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPerciChange;
    private  String mSummary;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getPerciChange() {
        return mPerciChange;
    }

    public void setPerciChange(double perciChange) {
        mPerciChange = perciChange;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
