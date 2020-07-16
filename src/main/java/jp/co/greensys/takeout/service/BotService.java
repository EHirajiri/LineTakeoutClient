package jp.co.greensys.takeout.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.service.dto.NotifyOrderDeliveryDTO;
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

    @Value("${bot.server.host}")
    private String botServer;

    public BotService() {}

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
