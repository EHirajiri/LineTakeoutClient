package jp.co.greensys.takeout.web;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import jp.co.greensys.takeout.service.BotService;
import jp.co.greensys.takeout.service.CustomerService;
import jp.co.greensys.takeout.service.ItemService;
import jp.co.greensys.takeout.service.OrderedService;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LineMessageHandler
public class BotHandler {
    private final Logger log = LoggerFactory.getLogger(BotHandler.class);

    private final LineMessagingClient lineMessagingClient;

    private final CustomerService customerService;

    private final BotService botService;

    public BotHandler(LineMessagingClient lineMessagingClient, CustomerService customerService, BotService botService) {
        this.lineMessagingClient = lineMessagingClient;
        this.customerService = customerService;
        this.botService = botService;
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
            log.error("Error occurred.", e);
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

        QueryStringParser parser = new QueryStringParser(event.getPostbackContent().getData());
        log.debug("PostbackDataType: {}", parser.getParameterValue("type"));
        try {
            switch (parser.getParameterValue("type")) {
                // 営業時間
                case "business-hour":
                    botService.replyBusinessHour(event.getReplyToken());
                    break;
                // アクセス
                case "access":
                    botService.replyAccess(event.getReplyToken());
                    break;
                // メニュー
                case "menu":
                    botService.replyMenu(event.getReplyToken(), null);
                    break;
                case "re-menu":
                    botService.replyMenu(event.getReplyToken(), parser);
                    break;
                // 個数選択
                case "quantity":
                    botService.replyQuantity(event.getReplyToken(), parser);
                    break;
                // カート
                case "cart":
                    botService.replyCart(event.getReplyToken(), parser);
                    break;
                // 受取日時選択
                case "delivery-date":
                    botService.replyDeliveryDate(event.getReplyToken(), parser);
                    break;
                // 問い合わせ
                case "customer-support":
                    lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("未対応の機能です")));
                    break;
                default:
                    lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("不正な操作です")));
            }
        } catch (Exception e) {
            log.error("Error occurred.", e);
            lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), new TextMessage("エラーが発生しました。")));
        }
    }
}
