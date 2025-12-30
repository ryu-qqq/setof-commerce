package com.ryuqq.setof.adapter.out.persistence.payment.mapper;

import com.ryuqq.setof.adapter.out.persistence.payment.entity.PaymentJpaEntity;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.Currency;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import com.ryuqq.setof.domain.payment.vo.PgProvider;
import org.springframework.stereotype.Component;

/**
 * PaymentJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Payment 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Payment -> PaymentJpaEntity (저장용)
 *   <li>PaymentJpaEntity -> Payment (조회용)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (저장용)
     *
     * @param domain Payment 도메인
     * @return PaymentJpaEntity
     */
    public PaymentJpaEntity toEntity(Payment domain) {
        return PaymentJpaEntity.of(
                domain.id().value(),
                domain.checkoutId().value(),
                domain.pgProvider().name(),
                domain.pgTransactionId(),
                domain.method().name(),
                domain.status().name(),
                domain.requestedAmount().value(),
                domain.approvedAmount().value(),
                domain.refundedAmount().value(),
                domain.currency().name(),
                domain.approvedAt(),
                domain.cancelledAt(),
                null, // memberId - migration용, 도메인에 없음
                null, // legacyPaymentId - migration용, 도메인에 없음
                domain.createdAt(),
                domain.updatedAt());
    }

    /**
     * Entity -> Domain 변환 (조회용)
     *
     * @param entity PaymentJpaEntity
     * @return Payment 도메인
     */
    public Payment toDomain(PaymentJpaEntity entity) {
        return Payment.restore(
                PaymentId.of(entity.getId()),
                CheckoutId.of(entity.getCheckoutId()),
                PgProvider.valueOf(entity.getPgProvider()),
                entity.getPgTransactionId(),
                PaymentMethod.valueOf(entity.getMethod()),
                PaymentStatus.valueOf(entity.getStatus()),
                PaymentMoney.of(entity.getRequestedAmount()),
                PaymentMoney.of(entity.getApprovedAmount()),
                PaymentMoney.of(entity.getRefundedAmount()),
                Currency.valueOf(entity.getCurrency()),
                entity.getApprovedAt(),
                entity.getCancelledAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
