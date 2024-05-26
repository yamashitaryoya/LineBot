package com.example.linebot.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

//気象情報APIに接続し、降水量データを取得するクラス
public class WeatherForecastAPI {

    private String apiKey;
    private String requestUrl;

    public WeatherForecastAPI(String coordinates) {
        this.apiKey = "dj00aiZpPWhXajZzRHZFWDY3ZiZzPWNvbnN1bWVyc2VjcmV0Jng9ZTI-";
        this.requestUrl = "https://map.yahooapis.jp/weather/V1/place" +
                "?coordinates=" + coordinates +
                "&output=json" +
                "&appid=" + apiKey;
    }

    public ArrayList<WeatherData> getWeather() {
        try {
            //HTTP接続の設定
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //レスポンスの確認
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){

                //レスポンスの処理 try-with-resources
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                    JSONObject responseObj = new JSONObject(reader.lines().collect(Collectors.joining()));
                    return getRainFallData(responseObj);
                }
            }else {
                System.out.println("エラーレスポンス: " + responseCode);
                return null;
            }
        }catch (IOException e) {
            System.out.println("IOExceptionが発生");
            return null;
        }
    }

    private ArrayList<WeatherData> getRainFallData(JSONObject obj) {
        try {
            ArrayList<WeatherData> rainFallDataList = new ArrayList<>();

            //JSON中の日付と降水量のデータを抽出
            JSONArray weatherArray = obj
                    .getJSONArray("Feature")
                    .getJSONObject(0)
                    .getJSONObject("Property")
                    .getJSONObject("WeatherList")
                    .getJSONArray("Weather");

            for (int i=0; i<weatherArray.length(); i++) {
                JSONObject weatherData = weatherArray.getJSONObject(i);

                //Dateのデータを取得
                String date = weatherData.getString("Date");

                //Rainfallのデータを取得
                double rainfall = weatherData.getDouble("Rainfall");

                //WeatherDataにインスタンス化
                WeatherData wd = new WeatherData(date, rainfall);

                //ArrayListに格納
                rainFallDataList.add(wd);
            }
            return rainFallDataList;

        }catch (JSONException e) {
            System.out.println("降水量の取得に失敗");
            return null;
        }
    }
}
