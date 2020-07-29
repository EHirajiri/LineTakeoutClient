package jp.co.greensys.takeout.service;

import java.util.Optional;
import jp.co.greensys.takeout.domain.Ordered;
import jp.co.greensys.takeout.repository.OrderedRepository;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.service.mapper.OrderedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ordered}.
 */
@Service
@Transactional
public class OrderedService {
    private final Logger log = LoggerFactory.getLogger(OrderedService.class);

    private final OrderedRepository orderedRepository;

    private final OrderedMapper orderedMapper;

    private final BotService botService;

    public OrderedService(OrderedRepository orderedRepository, OrderedMapper orderedMapper, BotService botService) {
        this.orderedRepository = orderedRepository;
        this.orderedMapper = orderedMapper;
        this.botService = botService;
    }

    /**
     * Save a ordered.
     *
     * @param orderedDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderedDTO save(OrderedDTO orderedDTO) {
        log.debug("Request to save Ordered : {}", orderedDTO);
        Ordered ordered = orderedMapper.toEntity(orderedDTO);
        ordered = orderedRepository.save(ordered);
        return orderedMapper.toDto(ordered);
    }

    /**
     * Get all the ordereds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ordereds");
        return orderedRepository.findAll(pageable).map(orderedMapper::toDto);
    }

    /**
     * Get all the ordereds with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderedDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderedRepository.findAllWithEagerRelationships(pageable).map(orderedMapper::toDto);
    }

    /**
     * Get one ordered by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderedDTO> findOne(Long id) {
        log.debug("Request to get Ordered : {}", id);
        return orderedRepository.findOneWithEagerRelationships(id).map(orderedMapper::toDto);
    }

    /**
     * Delete the ordered by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ordered : {}", id);
        orderedRepository.deleteById(id);
    }

    public OrderedDTO updateDeliveryState(OrderedDTO orderedDTO) {
        log.debug("Request to update Ordered : {}", orderedDTO);
        OrderedDTO result = save(orderedDTO);
        botService.pushMessage(orderedDTO);
        return result;
    }
}
