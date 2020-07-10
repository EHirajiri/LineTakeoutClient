package jp.co.greensys.takeout.web.rest;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
@LineMessageHandler
public class BotResource {
    private final Logger log = LoggerFactory.getLogger(BotResource.class);

    public BotResource() {}

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        log.debug("Called handleTextMessageEvent. event:{}", event);
        //入力されたテキストの取得
        String text = event.getMessage().getText();
    }
}
