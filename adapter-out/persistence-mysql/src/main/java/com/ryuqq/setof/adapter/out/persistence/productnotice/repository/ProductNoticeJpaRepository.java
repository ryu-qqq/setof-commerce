package com.ryuqq.setof.adapter.out.persistence.productnotice.repository;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductNoticeJpaRepository - ProductNotice JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductNoticeJpaRepository extends JpaRepository<ProductNoticeJpaEntity, Long> {

    /**
     * 상품그룹 ID로 상품고시 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 Entity
     */
    Optional<ProductNoticeJpaEntity> findByProductGroupId(Long productGroupId);
}
