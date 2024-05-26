package com.example.linebot;

import com.example.linebot.replier.Follow;
import com.example.linebot.replier.ForecastFromLocation;
import com.example.linebot.replier.ForecastFromText;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.MessageEvent;

@LineMessageHandler
public class Callback {
    private static final Logger log = LoggerFactory.getLogger(Callback.class);

    // フォローイベントに対応する
    @EventMapping
    public Message handleFollow(FollowEvent event) {
        // フォローイベント発生時に初期状態を設定
        Follow follow = new Follow(event);
        return follow.reply();
    }


    // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
    @EventMapping
    public Message handleText(MessageEvent<TextMessageContent> event) {

        ForecastFromText forecastFromText = new ForecastFromText(event);
        return forecastFromText.reply();
    }


    //位置情報を送信した時に対応する
    @EventMapping
    public Message handleLocation(MessageEvent<LocationMessageContent> event){

        ForecastFromLocation forecastFromLocation = new ForecastFromLocation(event);
        return forecastFromLocation.reply();
    }
}

