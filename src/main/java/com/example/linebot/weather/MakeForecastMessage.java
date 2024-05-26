package com.example.linebot.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//返信メッセージ作成クラス
public class MakeForecastMessage {

    private ArrayList<WeatherData> data;
    private String area;
    private StringBuffer message;

    public MakeForecastMessage(String area, ArrayList<WeatherData> data){
        this.message = new StringBuffer();
        this.area = area;
        this.data = data;
    }

    public String makeMessage(){
        message.append("<<" + area + ">>\n\n");
        message.append("-----現在の降水量(ml/h)-----\n\n");
        message.append(getHHmm(data.get(0).getDate()) + ": " + data.get(0).getRainFall() + "\n\n");
        message.append("------予想降水量(ml/h)------\n\n");
        for (int i=1;i< data.size();i++){
            message.append(getHHmm(data.get(i).getDate()) + ": " + data.get(i).getRainFall() + "\n");
        }
        return message.toString();
    }

    private String getHHmm(String date){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            Date d = df.parse(date);
            df.applyLocalizedPattern("HH時mm分");
            return df.format(d);
        } catch (ParseException e) {
            System.out.println("dateの解析にエラーがあります");
            return "不明";
        }
    }
}
