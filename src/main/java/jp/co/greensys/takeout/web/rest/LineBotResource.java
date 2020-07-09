package jp.co.greensys.takeout.web.rest;

import java.net.URISyntaxException;
import java.util.Map;
import javax.validation.Valid;
import jp.co.greensys.takeout.service.dto.LineBotOrderedDTO;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LineBotResource {
    private final Logger log = LoggerFactory.getLogger(LineBotResource.class);

    public LineBotResource() {}

    @PostMapping("/linebot/ordereds")
    public ResponseEntity<OrderedDTO> createOrdered(@Valid @RequestBody LineBotOrderedDTO orderedDTO) throws URISyntaxException {
        log.debug("REST request to save Ordered : {}", orderedDTO);
        return null;
    }
}
