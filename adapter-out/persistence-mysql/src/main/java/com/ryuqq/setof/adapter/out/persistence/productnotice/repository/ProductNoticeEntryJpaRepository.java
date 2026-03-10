package com.ryuqq.setof.adapter.out.persistence.productnotice.repository;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductNoticeEntryJpaRepository - 상품고시 항목 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductNoticeEntryJpaRepository
        extends JpaRepository<ProductNoticeEntryJpaEntity, Long> {

    void deleteByProductNoticeId(Long productNoticeId);
}
