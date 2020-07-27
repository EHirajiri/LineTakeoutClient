package jp.co.greensys.takeout.web;

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
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.flex.DeliveryMessageSupplier;
import jp.co.greensys.takeout.flex.MenuFlexMessageSupplier;
import jp.co.greensys.takeout.flex.OrderMessageSupplier;
import jp.co.greensys.takeout.flex.QuantityMessageSupplier;
import jp.co.greensys.takeout.flex.ReceiptConfirmMessageSupplier;
import jp.co.greensys.takeout.service.CustomerService;
import jp.co.greensys.takeout.service.ItemService;
import jp.co.greensys.takeout.service.OrderedService;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@LineMessageHandler
public class BotHandler {
    private final Logger log = LoggerFactory.getLogger(BotHandler.class);

    private final LineMessagingClient lineMessagingClient;

    private final CustomerService customerService;

    private final ItemService itemService;

    private final OrderedService orderedService;

    public BotHandler(
        LineMessagingClient lineMessagingClient,
        CustomerService customerService,
        ItemService itemService,
        OrderedService orderedService
    ) {
        this.lineMessagingClient = lineMessagingClient;
        this.customerService = customerService;
        this.itemService = itemService;
        this.orderedService = orderedService;
    }

    @EventMapping
    public Message handleFollowEvent(FollowEvent event) {
        log.debug("handleFollowEvent: {}", event);

        try {
            // ユーザープロフィール取得
            CompletableFuture<UserProfileResponse> profile = lineMessagingClient.getProfile(event.getSource().getUserId());

            // 顧客情報登録
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUserId(event.getSource().getUserId());
            customerDTO.setNickname(profile.get().getDisplayName());
            customerDTO.setLanguage(profile.get().getLanguage());
            customerService.save(customerDTO);

            return new TextMessage("友達登録ありがとうございます");
        } catch (Exception e) {
            return new TextMessage("エラーが発生しました");
        }
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
        try {
            postbackEvent(event);
        } catch (Exception e) {
            log.error("Error occurred.", e);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("エラーが発生しました。")));
        }
    }

    private void postbackEvent(PostbackEvent event) {
        QueryStringParser parser = new QueryStringParser(event.getPostbackContent().getData());
        log.debug("PostbackDataType: {}", parser.getParameterValue("type"));
        switch (parser.getParameterValue("type")) {
            case "menu":
                // 商品情報取得
                Page<ItemDTO> itemPage = itemService.findAll(Pageable.unpaged());
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
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new QuantityMessageSupplier(parser).get()));
                break;
            case "delivery":
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new DeliveryMessageSupplier(parser).get()));
                break;
            case "order":
                // 商品情報取得
                ItemDTO itemDTO = itemService.findOne(Long.valueOf(parser.getParameterValue("item"))).get();
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new OrderMessageSupplier(itemDTO, parser).get()));
                break;
            case "ordered":
                // 注文情報登録
                OrderedDTO orderedDTO = new OrderedDTO();
                orderedDTO.setOrderId(parser.getParameterValue("orderId"));
                orderedDTO.setQuantity(Integer.parseInt(parser.getParameterValue("quantity")));
                orderedDTO.setUnitPrice(Integer.parseInt(parser.getParameterValue("unitPrice")));
                orderedDTO.setTotalFee(Integer.parseInt(parser.getParameterValue("totalFee")));
                orderedDTO.setDeliveryState(DeliveryState.CONFIRMING);
                orderedDTO.setDeliveryDate(Instant.ofEpochMilli(Long.parseLong(parser.getParameterValue("deliveryDate"))));
                orderedDTO.setItemId(Long.parseLong(parser.getParameterValue("item")));
                orderedDTO.setCustomerUserId(event.getSource().getUserId());
                OrderedDTO result = orderedService.save(orderedDTO);

                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new ReceiptConfirmMessageSupplier(result).get()));

                break;
            default:
                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("エラーが発生しました")));
        }
    }
}
