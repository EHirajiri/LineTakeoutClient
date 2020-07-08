package jp.co.greensys.takeout.repository;

import java.util.Optional;
import jp.co.greensys.takeout.domain.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ordered entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderedRepository extends JpaRepository<Ordered, Long> {
    Optional<Ordered> findByOrderId(String orderId);

    Page<Ordered> findByOrderId(String orderId, Pageable pageable);
}
