package com.example.linebot.replier;

import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

// フォローされた時用の返信クラス
public class Follow implements Replire {

    private FollowEvent event;

    public Follow(FollowEvent event) {
        this.event = event;
    }

    @Override
    public Message reply() {
        String text = ("地名を送信すると降水量予報を返信します。\n位置情報での送信も可能です。");
        return new TextMessage(text);
    }

}
