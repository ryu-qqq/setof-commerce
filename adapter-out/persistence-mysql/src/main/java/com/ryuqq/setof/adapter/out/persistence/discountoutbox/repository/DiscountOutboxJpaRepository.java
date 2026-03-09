package com.ryuqq.setof.adapter.out.persistence.discountoutbox.repository;

import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DiscountOutboxJpaRepository - 할인 아웃박스 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountOutboxJpaRepository extends JpaRepository<DiscountOutboxJpaEntity, Long> {}
