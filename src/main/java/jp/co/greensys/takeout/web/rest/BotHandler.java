package jp.co.greensys.takeout.web.rest;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
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
import jp.co.greensys.takeout.flex.DeliveryMessageSupplier;
import jp.co.greensys.takeout.flex.MenuFlexMessageSupplier;
import jp.co.greensys.takeout.flex.QuantityMessageSupplier;
import jp.co.greensys.takeout.service.ItemService;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

@LineMessageHandler
public class BotHandler {
    private final Logger log = LoggerFactory.getLogger(BotHandler.class);

    private final LineMessagingClient lineMessagingClient;

    private final ItemService itemService;

    public BotHandler(LineMessagingClient lineMessagingClient, ItemService itemService) {
        this.lineMessagingClient = lineMessagingClient;
        this.itemService = itemService;
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
                // 商品情報取得
                Page<ItemDTO> itemPage = itemService.findAll(null);
                if (itemPage.getTotalPages() > 0) {
                    lineMessagingClient.replyMessage(
                        new ReplyMessage(event.getReplyToken(), new MenuFlexMessageSupplier(itemPage.getContent()).get())
                    );
                } else {
                    lineMessagingClient.replyMessage(
                        new ReplyMessage(event.getReplyToken(), new TextMessage("現在提供できる商品がありません。"))
                    );
                }
                break;
            case "select":
                lineMessagingClient.replyMessage(
                    new ReplyMessage(event.getReplyToken(), new QuantityMessageSupplier(parser.getParameterValue("item")).get())
                );
                break;
            case "delivery":
                lineMessagingClient.replyMessage(
                    new ReplyMessage(
                        event.getReplyToken(),
                        new DeliveryMessageSupplier(parser.getParameterValue("item"), parser.getParameterValue("quantity")).get()
                    )
                );
                break;
            case "order":
                break;
            default:
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("エラーが発生しました")));
        }
    }
}
