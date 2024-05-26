package com.example.linebot.weather;

//日付と降水量をフィールドに持つクラス
public class WeatherData {

    private String date;
    private double rainFall;

    public WeatherData(String date, double rainFall) {
        this.date = date;
        this.rainFall = rainFall;
    }

    public String getDate() {
        return date;
    }

    public double getRainFall() {
        return rainFall;
    }
}

