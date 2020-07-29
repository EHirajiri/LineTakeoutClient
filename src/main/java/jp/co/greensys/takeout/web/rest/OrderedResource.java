package jp.co.greensys.takeout.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import jp.co.greensys.takeout.service.OrderedService;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link jp.co.greensys.takeout.domain.Ordered}.
 */
@RestController
@RequestMapping("/api")
public class OrderedResource {
    private final Logger log = LoggerFactory.getLogger(OrderedResource.class);

    private static final String ENTITY_NAME = "ordered";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderedService orderedService;

    public OrderedResource(OrderedService orderedService) {
        this.orderedService = orderedService;
    }

    /**
     * {@code POST  /ordereds} : Create a new ordered.
     *
     * @param orderedDTO the orderedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderedDTO, or with status {@code 400 (Bad Request)} if the ordered has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordereds")
    public ResponseEntity<OrderedDTO> createOrdered(@Valid @RequestBody OrderedDTO orderedDTO) throws URISyntaxException {
        log.debug("REST request to save Ordered : {}", orderedDTO);
        if (orderedDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordered cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderedDTO result = orderedService.save(orderedDTO);
        return ResponseEntity
            .created(new URI("/api/ordereds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordereds} : Updates an existing ordered.
     *
     * @param orderedDTO the orderedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderedDTO,
     * or with status {@code 400 (Bad Request)} if the orderedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordereds")
    public ResponseEntity<OrderedDTO> updateOrdered(@Valid @RequestBody OrderedDTO orderedDTO) throws URISyntaxException {
        log.debug("REST request to update Ordered : {}", orderedDTO);
        if (orderedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderedDTO result = orderedService.save(orderedDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderedDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ordereds} : get all the ordereds.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordereds in body.
     */
    @GetMapping("/ordereds")
    public ResponseEntity<List<OrderedDTO>> getAllOrdereds(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Ordereds");
        Page<OrderedDTO> page;
        if (eagerload) {
            page = orderedService.findAllWithEagerRelationships(pageable);
        } else {
            page = orderedService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordereds/:id} : get the "id" ordered.
     *
     * @param id the id of the orderedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordereds/{id}")
    public ResponseEntity<OrderedDTO> getOrdered(@PathVariable Long id) {
        log.debug("REST request to get Ordered : {}", id);
        Optional<OrderedDTO> orderedDTO = orderedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderedDTO);
    }

    /**
     * {@code DELETE  /ordereds/:id} : delete the "id" ordered.
     *
     * @param id the id of the orderedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordereds/{id}")
    public ResponseEntity<Void> deleteOrdered(@PathVariable Long id) {
        log.debug("REST request to delete Ordered : {}", id);
        orderedService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/ordereds/deliveryState")
    public ResponseEntity<OrderedDTO> updateDeliveryState(@Valid @RequestBody OrderedDTO orderedDTO) throws URISyntaxException {
        log.debug("REST request to accept Ordered : {}", orderedDTO);
        if (orderedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderedDTO result = orderedService.updateDeliveryState(orderedDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderedDTO.getId().toString()))
            .body(result);
    }
}
