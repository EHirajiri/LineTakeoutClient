package jp.co.greensys.takeout.web;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.domain.enumeration.InfomationKey;
import jp.co.greensys.takeout.flex.BusinessHourMessageSupplier;
import jp.co.greensys.takeout.flex.DeliveryMessageSupplier;
import jp.co.greensys.takeout.flex.MenuFlexMessageSupplier;
import jp.co.greensys.takeout.flex.OrderMessageSupplier;
import jp.co.greensys.takeout.flex.QuantityMessageSupplier;
import jp.co.greensys.takeout.flex.ReceiptConfirmMessageSupplier;
import jp.co.greensys.takeout.service.CustomerService;
import jp.co.greensys.takeout.service.InformationService;
import jp.co.greensys.takeout.service.ItemService;
import jp.co.greensys.takeout.service.OrderedService;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import jp.co.greensys.takeout.service.dto.InformationDTO;
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

    private final InformationService informationService;

    private static final String BUSINESS_HOUR_SEARCH_KEY = InfomationKey.BUSINESS_HOUR + "%";

    public BotHandler(
        LineMessagingClient lineMessagingClient,
        CustomerService customerService,
        ItemService itemService,
        OrderedService orderedService,
        InformationService informationService
    ) {
        this.lineMessagingClient = lineMessagingClient;
        this.customerService = customerService;
        this.itemService = itemService;
        this.orderedService = orderedService;
        this.informationService = informationService;
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
            customerDTO.setFollow(true);
            customerService.save(customerDTO);

            // リッチメニュー設定
            lineMessagingClient.setDefaultRichMenu("richmenu-0264f2410e78f3e7a464ca982573fe39");

            return new TextMessage("友達登録ありがとうございます");
        } catch (Exception e) {
            return new TextMessage("エラーが発生しました");
        }
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.debug("handleUnfollowEvent: {}", event);
        Optional<CustomerDTO> customer = customerService.findByUserId(event.getSource().getUserId());
        if (customer.isPresent()) {
            customer.get().setFollow(false);
            customerService.save(customer.get());
        }
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
            // 営業時間
            case "business-hour":
                List<InformationDTO> informationList = informationService.findByKeyLike(BUSINESS_HOUR_SEARCH_KEY, Pageable.unpaged());
                lineMessagingClient.replyMessage(
                    new ReplyMessage(event.getReplyToken(), new BusinessHourMessageSupplier(informationList).get())
                );
                break;
            // アクセス
            case "access":
                lineMessagingClient.replyMessage(
                    new ReplyMessage(
                        event.getReplyToken(),
                        new LocationMessage(
                            "Brown's Burger Shop",
                            "〒150-0002 東京都渋谷区渋谷２丁目２１−１",
                            35.65910807942215,
                            139.70372892916203
                        )
                    )
                );
                break;
            // 注文
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
