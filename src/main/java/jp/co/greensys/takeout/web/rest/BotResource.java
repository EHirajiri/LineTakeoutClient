package jp.co.greensys.takeout.web.rest;

import com.google.common.collect.ImmutableMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import jp.co.greensys.takeout.service.BotService;
import jp.co.greensys.takeout.service.CustomerService;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bot")
public class BotResource {
    private final Logger log = LoggerFactory.getLogger(BotResource.class);

    private final CustomerService customerService;

    public BotResource(BotService botService, CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/follow")
    public ResponseEntity<Map> createItem(@RequestBody Map body) throws URISyntaxException {
        log.debug("REST request to save follow user : {}", body);
        CustomerDTO result = customerService.save(toCustomerDTO(body));
        return ResponseEntity.created(new URI("/api/bot/follow")).body(toMap(result));
    }

    private CustomerDTO toCustomerDTO(Map body) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUserId((String) ((Map) body.get("user_id")).get("value"));
        dto.setNickname((String) ((Map) body.get("nickname")).get("value"));
        dto.setLanguage((String) ((Map) body.get("language")).get("value"));
        return dto;
    }

    private Map toMap(CustomerDTO dto) {
        return ImmutableMap.of(
            "user_id",
            ImmutableMap.of("value", dto.getUserId()),
            "nickname",
            ImmutableMap.of("value", dto.getNickname()),
            "language",
            ImmutableMap.of("value", dto.getLanguage())
        );
    }
}
