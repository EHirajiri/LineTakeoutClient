package jp.co.greensys.takeout.repository;

import jp.co.greensys.takeout.domain.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Information entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {
    Page<Information> findByKeyLike(String key, Pageable pageable);
}
