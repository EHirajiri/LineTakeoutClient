package jp.co.greensys.takeout.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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

    public PayService(
        PayRepository payRepository,
        PayMapper payMapper,
        CustomerRepository customerRepository,
        OrderedRepository orderedRepository
    ) {
        this.payRepository = payRepository;
        this.payMapper = payMapper;
        this.customerRepository = customerRepository;
        this.orderedRepository = orderedRepository;
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
        if (payDTO.getCustomerUserId() != null) {
            Optional<Customer> customer = customerRepository.findByUserId(payDTO.getCustomerUserId());
            if (customer.isPresent()) {
                pay.setCustomer(customer.get());
            } else {
                throw new IllegalArgumentException("Invalid userId. userId=" + payDTO.getCustomerUserId());
            }
        }

        // 注文情報取得
        Optional<Ordered> ordered = orderedRepository.findByOrderId(payDTO.getOrderedOrderId());
        if (ordered.isPresent()) {
            pay.setOrdered(ordered.get());
        } else {
            throw new IllegalArgumentException("Invalid orderId. orderId=" + payDTO.getOrderedOrderId());
        }

        pay = payRepository.save(pay);
        return payMapper.toDto(pay);
    }

    /**
     * Get all the pays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pays");
        return payRepository.findAll(pageable).map(payMapper::toDto);
    }

    /**
     *  Get all the pays where Ordered is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PayDTO> findAllWhereOrderedIsNull() {
        log.debug("Request to get all pays where Ordered is null");
        return StreamSupport
            .stream(payRepository.findAll().spliterator(), false)
            .filter(pay -> pay.getOrdered() == null)
            .map(payMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
}
