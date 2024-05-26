package com.example.linebot.replier;

import com.example.linebot.weather.GeoCodeAPI;
import com.example.linebot.weather.WeatherForecastAPI;
import com.example.linebot.weather.MakeForecastMessage;
import com.example.linebot.weather.WeatherData;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.ArrayList;

//テキストが送られてきたときの返信クラス
public class ForecastFromText implements Replire {

    private MessageEvent<TextMessageContent> event;
    private ArrayList<WeatherData> rainFallData;

    public ForecastFromText(MessageEvent<TextMessageContent> event) {
        this.event = event;
        rainFallData = new ArrayList<>();
    }

    @Override
    public Message reply() {
        //イベントからテキスト(地名)を取得する
        TextMessageContent tmc = event.getMessage();
        String area = tmc.getText();

        //地名から座標に変換　Yahoo!ジオコーダAPI
        GeoCodeAPI gca = new GeoCodeAPI(area);
        String coordinates = gca.getCoordinates();

        if(coordinates != null) {
            //座標から降水量データを取得 Yahoo!気象情報API
            WeatherForecastAPI wfa = new WeatherForecastAPI(coordinates);
            rainFallData = wfa.getWeather();

            if(rainFallData != null) {
                //降水量データからLINEメッセージを作成
                MakeForecastMessage mfm = new MakeForecastMessage(area, rainFallData);
                String replyMessage = mfm.makeMessage();
                return new TextMessage(replyMessage);

            }else {
                return new TextMessage("天気の取得ができません");
            }
        }else {
            return new TextMessage("場所が特定できません");
        }
    }
}