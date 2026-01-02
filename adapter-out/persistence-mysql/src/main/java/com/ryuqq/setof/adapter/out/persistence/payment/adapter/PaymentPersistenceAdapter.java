package com.ryuqq.setof.adapter.out.persistence.payment.adapter;

import com.ryuqq.setof.adapter.out.persistence.payment.entity.PaymentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.payment.mapper.PaymentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.payment.repository.PaymentJpaRepository;
import com.ryuqq.setof.application.payment.port.out.command.PaymentPersistencePort;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import org.springframework.stereotype.Component;

/**
 * PaymentPersistenceAdapter - Payment Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Payment 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Payment 저장 (persist)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentPersistenceAdapter implements PaymentPersistencePort {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentJpaEntityMapper paymentJpaEntityMapper;

    public PaymentPersistenceAdapter(
            PaymentJpaRepository paymentJpaRepository,
            PaymentJpaEntityMapper paymentJpaEntityMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.paymentJpaEntityMapper = paymentJpaEntityMapper;
    }

    /**
     * Payment 저장 (생성/수정)
     *
     * @param payment Payment 도메인
     * @return 저장된 PaymentId
     */
    @Override
    public PaymentId persist(Payment payment) {
        PaymentJpaEntity entity = paymentJpaEntityMapper.toEntity(payment);
        PaymentJpaEntity savedEntity = paymentJpaRepository.save(entity);
        return PaymentId.of(savedEntity.getId());
    }
}
