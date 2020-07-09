package jp.co.greensys.takeout.repository;

import java.util.Optional;
import jp.co.greensys.takeout.domain.Pay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Pay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {
    Page<Pay> findByOrderedOrderId(String orderId, Pageable pageable);

    Optional<Pay> findByTransactionId(Long transactionId);
}
