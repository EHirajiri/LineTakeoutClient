package jp.co.greensys.takeout.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import jp.co.greensys.takeout.service.PayService;
import jp.co.greensys.takeout.service.dto.PayDTO;
import jp.co.greensys.takeout.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link jp.co.greensys.takeout.domain.Pay}.
 */
@RestController
@RequestMapping("/api")
public class PayResource {
    private final Logger log = LoggerFactory.getLogger(PayResource.class);

    private static final String ENTITY_NAME = "pay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayService payService;

    public PayResource(PayService payService) {
        this.payService = payService;
    }

    /**
     * {@code POST  /pays} : Create a new pay.
     *
     * @param payDTO the payDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payDTO, or with status {@code 400 (Bad Request)} if the pay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pays")
    public ResponseEntity<PayDTO> createPay(@Valid @RequestBody PayDTO payDTO) throws URISyntaxException {
        log.debug("REST request to save Pay : {}", payDTO);
        if (payDTO.getId() != null) {
            throw new BadRequestAlertException("A new pay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayDTO result = payService.save(payDTO);
        return ResponseEntity
            .created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pays} : Updates an existing pay.
     *
     * @param payDTO the payDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payDTO,
     * or with status {@code 400 (Bad Request)} if the payDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pays")
    public ResponseEntity<PayDTO> updatePay(@Valid @RequestBody PayDTO payDTO) throws URISyntaxException {
        log.debug("REST request to update Pay : {}", payDTO);
        if (payDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayDTO result = payService.save(payDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pays} : get all the pays.
     *
     * @param pageable the pagination information.
     * @param orderId the orderId
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pays in body.
     */
    @GetMapping("/pays")
    public ResponseEntity<List<PayDTO>> getAllPays(Pageable pageable, @RequestParam(name = "orderId", required = false) String orderId) {
        log.debug("REST request to get a page of Pays");
        Page<PayDTO> page = payService.findAll(pageable, orderId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pays/:id} : get the "id" pay.
     *
     * @param id the id of the payDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pays/{id}")
    public ResponseEntity<PayDTO> getPay(@PathVariable Long id) {
        log.debug("REST request to get Pay : {}", id);
        Optional<PayDTO> payDTO = payService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payDTO);
    }

    /**
     * {@code DELETE  /pays/:id} : delete the "id" pay.
     *
     * @param id the id of the payDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pays/{id}")
    public ResponseEntity<Void> deletePay(@PathVariable Long id) {
        log.debug("REST request to delete Pay : {}", id);
        payService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
