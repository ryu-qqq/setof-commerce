package com.ryuqq.setof.adapter.out.persistence.productnotice.repository;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductNoticeJpaRepository - 상품고시 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductNoticeJpaRepository extends JpaRepository<ProductNoticeJpaEntity, Long> {

    Optional<ProductNoticeJpaEntity> findByProductGroupId(Long productGroupId);
}
