package jp.co.greensys.takeout.web.rest;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import jp.co.greensys.takeout.flex.MenuFlexMessageSupplier;
import jp.co.greensys.takeout.flex.QuantityMessageSupplier;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LineMessageHandler
public class BotHandler {
    private final Logger log = LoggerFactory.getLogger(BotHandler.class);

    private final LineMessagingClient lineMessagingClient;

    public BotHandler(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }

    @EventMapping
    public Message handleFollowEvent(FollowEvent event) {
        log.debug("handleFollowEvent: {}", event);
        return new TextMessage("友達登録ありがとうございます");
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.debug("handleUnfollowEvent: {}", event);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.debug("handleTextMessageEvent: {}", event);
        final String originalMessageText = event.getMessage().getText();
        return new TextMessage(originalMessageText);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        log.debug("handleDefaultMessageEvent: {}", event);
    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        log.debug("handlePostbackEvent: {}", event);
        QueryStringParser parser = new QueryStringParser(event.getPostbackContent().getData());
        log.debug("PostbackDataType: {}", parser.getParameterValue("type"));
        switch (parser.getParameterValue("type")) {
            case "menu":
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new MenuFlexMessageSupplier().get()));
                break;
            case "select":
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new QuantityMessageSupplier().get()));
                break;
        }
    }
}
