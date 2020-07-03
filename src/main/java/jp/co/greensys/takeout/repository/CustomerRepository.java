package jp.co.greensys.takeout.repository;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import jp.co.greensys.takeout.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findOneByUserId(@NotNull String userId);
}
