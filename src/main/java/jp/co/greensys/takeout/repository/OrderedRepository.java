package jp.co.greensys.takeout.repository;

import java.util.List;
import java.util.Optional;
import jp.co.greensys.takeout.domain.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ordered entity.
 */
@Repository
public interface OrderedRepository extends JpaRepository<Ordered, Long> {
    @Query(
        value = "select distinct ordered from Ordered ordered left join fetch ordered.orderItems",
        countQuery = "select count(distinct ordered) from Ordered ordered"
    )
    Page<Ordered> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct ordered from Ordered ordered left join fetch ordered.orderItems")
    List<Ordered> findAllWithEagerRelationships();

    @Query("select ordered from Ordered ordered left join fetch ordered.orderItems where ordered.id =:id")
    Optional<Ordered> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Ordered> findOneByOrderId(@Param("orderId") String orderId);
}
