package jp.co.greensys.takeout.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.flex.ReceiptAcceptMessageSupplier;
import jp.co.greensys.takeout.flex.ReceiptCancelMessageSupplier;
import jp.co.greensys.takeout.flex.ReceiptDeliveredMessageSupplier;
import jp.co.greensys.takeout.service.dto.NotifyOrderDeliveryDTO;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BotService {
    private final Logger log = LoggerFactory.getLogger(BotService.class);

    private final LineMessagingClient lineMessagingClient;

    @Value("${bot.server.host}")
    private String botServer;

    public BotService(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
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

    public void notifyOrderDeliveryState(String orderId, DeliveryState deliveryState) {
        log.debug("Request to notifyOrderDeliveryState. orderId:{}, deliveryState:{}", orderId, deliveryState);

        URI uri = null;
        try {
            uri = new URI(botServer + "/api/notifyOrderDeliveryState");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        log.debug("botServer:{}", botServer);
        RestTemplate client = new RestTemplate(new SimpleClientHttpRequestFactory());

        NotifyOrderDeliveryDTO body = new NotifyOrderDeliveryDTO();
        body.setOrderId(orderId);
        body.setDeliveryState(deliveryState.name());
        RequestEntity<?> req = new RequestEntity<>(body, getHttpHeaders(), HttpMethod.POST, uri);
        log.debug("req:" + req.toString());

        ResponseEntity<?> res = client.exchange(req, Map.class);
        log.debug("res status:" + res.getStatusCode());
        log.debug("res body:" + res.getBody());
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
