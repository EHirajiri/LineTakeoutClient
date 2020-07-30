package jp.co.greensys.takeout.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import java.util.Optional;
import jp.co.greensys.takeout.domain.Ordered;
import jp.co.greensys.takeout.flex.ReceiptAcceptMessageSupplier;
import jp.co.greensys.takeout.flex.ReceiptCancelMessageSupplier;
import jp.co.greensys.takeout.flex.ReceiptDeliveredMessageSupplier;
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

    private final LineMessagingClient lineMessagingClient;

    public OrderedService(OrderedRepository orderedRepository, OrderedMapper orderedMapper, LineMessagingClient lineMessagingClient) {
        this.orderedRepository = orderedRepository;
        this.orderedMapper = orderedMapper;
        this.lineMessagingClient = lineMessagingClient;
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
        pushMessage(orderedDTO);
        return result;
    }

    private void pushMessage(OrderedDTO orderedDTO) {
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
}
