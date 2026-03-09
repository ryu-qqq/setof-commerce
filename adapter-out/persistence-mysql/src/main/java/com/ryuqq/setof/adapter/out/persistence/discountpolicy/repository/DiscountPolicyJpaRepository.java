package com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DiscountPolicyJpaRepository - 할인 정책 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountPolicyJpaRepository extends JpaRepository<DiscountPolicyJpaEntity, Long> {}
