package com.ryuqq.setof.adapter.out.persistence.payment.repository;

import com.ryuqq.setof.adapter.out.persistence.payment.entity.PaymentJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PaymentJpaRepository - Payment JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * <p><strong>표준 JPA 메서드만 사용:</strong>
 *
 * <ul>
 *   <li>save, saveAll, findById, deleteById, deleteAll - 표준 메서드
 *   <li>커스텀 쿼리는 PaymentQueryDslRepository에서 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {
    // 표준 JPA 메서드만 사용 (save, saveAll, findById, deleteById, deleteAll)
    // 커스텀 쿼리는 PaymentQueryDslRepository에서 처리
}
