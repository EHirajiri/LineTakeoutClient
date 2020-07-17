package jp.co.greensys.takeout.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.validation.Valid;
import jp.co.greensys.takeout.service.BotService;
import jp.co.greensys.takeout.service.CustomerService;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/bot")
public class BotResource {
    private final Logger log = LoggerFactory.getLogger(BotResource.class);

    private static final String ENTITY_NAME = "item";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    public BotResource(BotService botService, CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/follow")
    public ResponseEntity<CustomerDTO> createItem(@Valid @RequestBody Map request) throws URISyntaxException {
        log.debug("REST request to save follow user : {}", request);
        CustomerDTO result = customerService.save(convertDto(request));
        return ResponseEntity
            .created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private CustomerDTO convertDto(Map map) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUserId((String) ((Map) map.get("user_id")).get("value"));
        dto.setNickname((String) ((Map) map.get("nickname")).get("value"));
        dto.setLanguage((String) ((Map) map.get("language")).get("value"));
        return dto;
    }
}
