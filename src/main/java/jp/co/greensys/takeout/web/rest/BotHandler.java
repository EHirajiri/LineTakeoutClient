package jp.co.greensys.takeout.web.rest;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LineMessageHandler
public class BotHandler {
    private final Logger log = LoggerFactory.getLogger(BotHandler.class);

    public BotHandler() {}

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        log.debug("handleFollowEvent: {}", event);
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
        log.debug("PostbackDataType:{}", parser.getParameterValue("type"));
        switch (parser.getParameterValue("type")) {
            case "menu":
                log.debug("postback menu.");
                break;
        }
    }
}
