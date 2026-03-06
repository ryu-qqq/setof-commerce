package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SellerAddressJpaRepository - 셀러 주소 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SellerAddressJpaRepository extends JpaRepository<SellerAddressJpaEntity, Long> {}
