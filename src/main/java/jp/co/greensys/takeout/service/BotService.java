package jp.co.greensys.takeout.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jp.co.greensys.takeout.domain.enumeration.InfomationKey;
import jp.co.greensys.takeout.flex.*;
import jp.co.greensys.takeout.flex.dto.CartDTO;
import jp.co.greensys.takeout.service.dto.InformationDTO;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.JsonUtil;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final Logger log = LoggerFactory.getLogger(BotService.class);

    private final String BUSINESS_HOUR_SEARCH_KEY = InfomationKey.BUSINESS_HOUR + "%";

    private final LineMessagingClient lineMessagingClient;

    private final InformationService informationService;

    private final ItemService itemService;

    public BotService(LineMessagingClient lineMessagingClient, InformationService informationService, ItemService itemService) {
        this.lineMessagingClient = lineMessagingClient;
        this.informationService = informationService;
        this.itemService = itemService;
    }

    public void pushMessage(OrderedDTO orderedDTO) {
        log.debug("pushMessage : {}", orderedDTO);
        Message message;
        switch (orderedDTO.getDeliveryState()) {
            case ACCEPT:
                message = new ReceiptAcceptMessageSupplier(orderedDTO).get();
                break;
            case DELIVERED:
                message = new ReceiptDeliveredMessageSupplier(orderedDTO).get();
                break;
            case CANCEL:
                message = new ReceiptCancelMessageSupplier(orderedDTO).get();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + orderedDTO.getDeliveryState());
        }

        lineMessagingClient.pushMessage(new PushMessage(orderedDTO.getCustomerUserId(), message));
    }

    public void replyBusinessHour(String replyToken) {
        List<InformationDTO> informationList = informationService.findByKeyLike(BUSINESS_HOUR_SEARCH_KEY, Pageable.unpaged());
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new BusinessHourMessageSupplier(informationList).get()));
    }

    public void replyAccess(String replyToken) {
        lineMessagingClient.replyMessage(
            new ReplyMessage(
                replyToken,
                new LocationMessage(
                    "Brown's Burger Shop",
                    "〒150-0002 東京都渋谷区渋谷２丁目２１−１",
                    35.65910807942215,
                    139.70372892916203
                )
            )
        );
    }

    public void replyMenu(String replyToken, QueryStringParser parser) {
        Message message;
        Page<ItemDTO> itemPage = itemService.findAll(Pageable.unpaged());
        if (itemPage.getTotalPages() > 0) {
            message = new MenuFlexMessageSupplier(itemPage.getContent(), parser).get();
        } else {
            message = new TextMessage("現在提供できる商品がありません");
        }
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message));
    }

    public void replyQuantity(String replyToken, QueryStringParser parser) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new QuantityMessageSupplier(parser).get()));
    }

    public void replyCart(String replyToken, QueryStringParser parser) {
        // カート内の商品情報取得
        List<String> carts = parser.getParameterValues("cart");
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (String cart : carts) {
            CartDTO cartDTO = JsonUtil.parse(CartDTO.class, cart);
            Optional<ItemDTO> itemDTO = itemService.findOne(cartDTO.getId());
            if (itemDTO.isPresent()) {
                itemDTO.get().setQuantity(cartDTO.getQuantity());
                itemDTOList.add(itemDTO.get());
            }
        }

        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new CartMessageSupplier(parser, itemDTOList).get()));
    }

    public void replyDeliveryDate(String replyToken, QueryStringParser parser) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new DeliveryDateMessageSupplier(parser).get()));
    }

    public void replyDeliveryTime(String replyToken, QueryStringParser parser) {
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new DeliveryTimeMessageSupplier(parser).get()));
    }

    public void replyRegister(String replyToken, QueryStringParser parser) {
        // カート内の商品情報取得
        List<String> carts = parser.getParameterValues("cart");
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (String cart : carts) {
            CartDTO cartDTO = JsonUtil.parse(CartDTO.class, cart);
            Optional<ItemDTO> itemDTO = itemService.findOne(cartDTO.getId());
            if (itemDTO.isPresent()) {
                itemDTO.get().setQuantity(cartDTO.getQuantity());
                itemDTOList.add(itemDTO.get());
            }
        }
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new RegisterMessageSupplier(parser, itemDTOList).get()));
    }

    public void replyOrder(String replyToken, QueryStringParser parser) {
        // 注文情報登録
        // TODO
        lineMessagingClient.replyMessage(new ReplyMessage(replyToken, new ReceiptConfirmMessageSupplier(null).get()));
    }
    //    public void postbackEvent(PostbackEvent event) {
    //        QueryStringParser parser = new QueryStringParser(event.getPostbackContent().getData());
    //        log.debug("PostbackDataType: {}", parser.getParameterValue("type"));
    //        switch (parser.getParameterValue("type")) {
    //            case "order":
    //                // 商品情報取得
    //                ItemDTO itemDTO = itemService.findOne(Long.valueOf(parser.getParameterValue("item"))).get();
    //                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new OrderMessageSupplier(itemDTO, parser).get()));
    //                break;
    //            case "ordered":
    //                // 注文情報登録
    //                OrderedDTO orderedDTO = new OrderedDTO();
    //                orderedDTO.setOrderId(parser.getParameterValue("orderId"));
    //                orderedDTO.setQuantity(Integer.parseInt(parser.getParameterValue("quantity")));
    //                orderedDTO.setUnitPrice(Integer.parseInt(parser.getParameterValue("unitPrice")));
    //                orderedDTO.setTotalFee(Integer.parseInt(parser.getParameterValue("totalFee")));
    //                orderedDTO.setDeliveryState(DeliveryState.CONFIRMING);
    //                orderedDTO.setDeliveryDate(Instant.ofEpochMilli(Long.parseLong(parser.getParameterValue("deliveryDate"))));
    //                orderedDTO.setItemId(Long.parseLong(parser.getParameterValue("item")));
    //                orderedDTO.setCustomerUserId(event.getSource().getUserId());
    //                OrderedDTO result = orderedService.save(orderedDTO);
    //
    //                lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new ReceiptConfirmMessageSupplier(result).get()));
    //
    //                break;
    //        }
    //    }
}
