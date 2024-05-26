package com.example.linebot.replier;

import com.example.linebot.weather.MakeForecastMessage;
import com.example.linebot.weather.WeatherData;
import com.example.linebot.weather.WeatherForecastAPI;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.ArrayList;

//位置情報が送信されたときの返信用クラス
public class ForecastFromLocation implements  Replire{

    private MessageEvent<LocationMessageContent> event;
    private ArrayList<WeatherData> rainFallData;

    public ForecastFromLocation(MessageEvent<LocationMessageContent> event){
        this.event = event;
        rainFallData = new ArrayList<>();
    }

    @Override
    public Message reply() {
        //イベントから地名・座標を取得する
        LocationMessageContent lmc = event.getMessage();
        String area = lmc.getAddress(); //地名の取得
        String longitude = String.valueOf(lmc.getLongitude()); //経度の取得
        String latitude = String.valueOf(lmc.getLatitude()); //緯度の取得
        String coordinates = longitude + "," + latitude;

        //座標から降水量データを取得 気象情報API
        WeatherForecastAPI wfa = new WeatherForecastAPI(coordinates);
        rainFallData = wfa.getWeather();

        if(rainFallData != null) {
            //降水量データからLINEメッセージを作成
            MakeForecastMessage mfm = new MakeForecastMessage(area, rainFallData);
            String replyMessage = mfm.makeMessage();

            return new TextMessage(replyMessage);
        } else {
            return new TextMessage("天気の取得が出来ません");
        }
    }
}
