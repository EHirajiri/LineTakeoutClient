package jp.co.greensys.takeout.service;

import io.micrometer.core.instrument.util.StringUtils;
import java.util.Optional;
import jp.co.greensys.takeout.domain.Customer;
import jp.co.greensys.takeout.domain.Ordered;
import jp.co.greensys.takeout.domain.Pay;
import jp.co.greensys.takeout.repository.CustomerRepository;
import jp.co.greensys.takeout.repository.OrderedRepository;
import jp.co.greensys.takeout.repository.PayRepository;
import jp.co.greensys.takeout.service.dto.PayDTO;
import jp.co.greensys.takeout.service.mapper.PayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pay}.
 */
@Service
@Transactional
public class PayService {
    private final Logger log = LoggerFactory.getLogger(PayService.class);

    private final PayRepository payRepository;

    private final PayMapper payMapper;

    private final CustomerRepository customerRepository;

    private final OrderedRepository orderedRepository;

    private final BotService botService;

    public PayService(
        PayRepository payRepository,
        PayMapper payMapper,
        CustomerRepository customerRepository,
        OrderedRepository orderedRepository,
        BotService botService
    ) {
        this.payRepository = payRepository;
        this.payMapper = payMapper;
        this.customerRepository = customerRepository;
        this.orderedRepository = orderedRepository;
        this.botService = botService;
    }

    /**
     * Save a pay.
     *
     * @param payDTO the entity to save.
     * @return the persisted entity.
     */
    public PayDTO save(PayDTO payDTO) {
        log.debug("Request to save Pay : {}", payDTO);
        Pay pay = payMapper.toEntity(payDTO);

        // 顧客情報取得
        if (StringUtils.isNotEmpty(payDTO.getCustomerUserId())) {
            Optional<Customer> customer = customerRepository.findByUserId(payDTO.getCustomerUserId());
            if (customer.isPresent()) {
                pay.setCustomer(customer.get());
            } else {
                throw new IllegalArgumentException("Invalid userId. userId=" + payDTO.getCustomerUserId());
            }
        }

        // 注文情報取得
        if (StringUtils.isNotEmpty(payDTO.getOrderedOrderId())) {
            Optional<Ordered> ordered = orderedRepository.findByOrderId(payDTO.getOrderedOrderId());
            if (ordered.isPresent()) {
                pay.setOrdered(ordered.get());
            } else {
                throw new IllegalArgumentException("Invalid orderId. orderId=" + payDTO.getOrderedOrderId());
            }
        }

        pay = payRepository.save(pay);

        // LINE BOTへ通知
        switch (pay.getDeliveryState()) {
            case READY:
                botService.notifyOrderDeliveryState(pay.getOrdered().getOrderId(), pay.getDeliveryState());
                break;
        }
        return payMapper.toDto(pay);
    }

    /**
     * Get all the pays.
     *
     * @param pageable the pagination information.
     * @param orderId the orderId
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PayDTO> findAll(Pageable pageable, String orderId) {
        log.debug("Request to get all Pays");
        if (StringUtils.isEmpty(orderId)) {
            return payRepository.findAll(pageable).map(payMapper::toDto);
        } else {
            return payRepository.findByOrderedOrderId(orderId, pageable).map(payMapper::toDto);
        }
    }

    /**
     * Get one pay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PayDTO> findOne(Long id) {
        log.debug("Request to get Pay : {}", id);
        return payRepository.findById(id).map(payMapper::toDto);
    }

    /**
     * Delete the pay by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pay : {}", id);
        payRepository.deleteById(id);
    }

    /**
     * Get one pay by transactionId.
     *
     * @param transactionId the transactionId of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PayDTO> findOneByTransactionId(Long transactionId) {
        log.debug("Request to get Pay : {}", transactionId);
        return payRepository.findByTransactionId(transactionId).map(payMapper::toDto);
    }
}
