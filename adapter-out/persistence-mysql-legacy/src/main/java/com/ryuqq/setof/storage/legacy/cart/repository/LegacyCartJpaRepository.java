package com.ryuqq.setof.storage.legacy.cart.repository;

import com.ryuqq.setof.storage.legacy.cart.entity.LegacyCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyCartJpaRepository - 레거시 장바구니 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * <p>소프트 딜리트 방식 - delete_yn 컬럼을 'Y'로 변경. JPA Dirty Checking으로 자동 UPDATE. INSERT는 save(), UPDATE는
 * Dirty Checking으로 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyCartJpaRepository extends JpaRepository<LegacyCartEntity, Long> {}
