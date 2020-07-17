package jp.co.greensys.takeout.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import jp.co.greensys.takeout.service.BotService;
import jp.co.greensys.takeout.service.CustomerService;
import jp.co.greensys.takeout.service.OrderedService;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.service.mapper.CustomerMapper;
import jp.co.greensys.takeout.service.mapper.OrderedMapper;
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

    private final CustomerMapper customerMapper;

    private final OrderedService orderedService;

    private final OrderedMapper orderedMapper;

    public BotResource(
        BotService botService,
        CustomerService customerService,
        CustomerMapper customerMapper,
        OrderedService orderedService,
        OrderedMapper orderedMapper
    ) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.orderedService = orderedService;
        this.orderedMapper = orderedMapper;
    }

    @PostMapping("/follow")
    public ResponseEntity<Map> createItem(@RequestBody Map body) throws URISyntaxException {
        log.debug("REST request to save follow user : {}", body);
        CustomerDTO result = customerService.save(customerMapper.toCustomerDTO(body));
        return ResponseEntity.created(new URI("/api/bot/follow")).body(customerMapper.toMap(result));
    }

    @PostMapping("/order")
    public ResponseEntity<Map> createOrder(@RequestBody Map body) throws URISyntaxException {
        log.debug("REST request to save order : {}", body);
        OrderedDTO result = orderedService.save(orderedMapper.toCustomerDTO(body));
        log.debug("result:{}", result);
        return ResponseEntity.created(new URI("/api/bot/order")).body(orderedMapper.toMap(result));
    }
}
