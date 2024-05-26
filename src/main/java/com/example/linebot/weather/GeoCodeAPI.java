package com.example.linebot.weather;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

//ジオコーダAPIに接続し、地名を座標に変換するクラス
public class GeoCodeAPI {
    String apiKey;
    String requestUrl;

    public GeoCodeAPI(String area) {
        this.apiKey = "dj00aiZpPWhXajZzRHZFWDY3ZiZzPWNvbnN1bWVyc2VjcmV0Jng9ZTI-";
        this.requestUrl = "https://map.yahooapis.jp/geocode/V1/geoCoder" +
                "?appid=" + apiKey +
                "&output=json" +
                "&query=" + URLEncoder.encode(area, StandardCharsets.UTF_8); //LINEで入力した住所をURL用に書き換える
    }

    public String getCoordinates() {
        try {
            //HTTP接続の設定
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // レスポンスの確認
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //レスポンスの処理 try-with-resources
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    JSONObject responseObj = new JSONObject(reader.lines().collect(Collectors.joining()));
                    return getStCoordinates(responseObj);
                }
            } else {
                System.out.println("エラーレスポンス: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.out.println("IOExceptionが発生");
            return null;
        }
    }

    private String getStCoordinates(JSONObject obj) {
        try {
            //JSON中にある座標データをString型で抽出
            String coordinates = obj
                    .getJSONArray("Feature")
                    .getJSONObject(0)
                    .getJSONObject("Geometry")
                    .getString("Coordinates");
            return coordinates;

        } catch (JSONException e) {
            System.out.println("座標の取得に失敗");
            return null;
        }
    }
}
