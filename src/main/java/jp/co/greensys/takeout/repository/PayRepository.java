package jp.co.greensys.takeout.repository;

import jp.co.greensys.takeout.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Pay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {}
