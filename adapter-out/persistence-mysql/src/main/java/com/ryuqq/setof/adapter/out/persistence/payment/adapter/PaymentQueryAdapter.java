package com.ryuqq.setof.adapter.out.persistence.payment.adapter;

import com.ryuqq.setof.adapter.out.persistence.payment.condition.PaymentSearchCondition;
import com.ryuqq.setof.adapter.out.persistence.payment.entity.PaymentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.payment.mapper.PaymentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.payment.repository.PaymentQueryDslRepository;
import com.ryuqq.setof.application.payment.dto.query.GetPaymentsQuery;
import com.ryuqq.setof.application.payment.port.out.query.PaymentQueryPort;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.exception.PaymentNotFoundException;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * PaymentQueryAdapter - Payment Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Payment 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>ID로 단건 조회 - 필수 (getById)
 *   <li>CheckoutId로 단건 조회 (findByCheckoutId)
 *   <li>CheckoutId로 단건 조회 - 필수 (getByCheckoutId)
 *   <li>QueryDslRepository 호출
 *   <li>Mapper를 통한 Entity → Domain 변환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 *   <li>JPAQueryFactory 직접 사용 금지 (QueryDslRepository에서 처리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentQueryAdapter implements PaymentQueryPort {

    private final PaymentQueryDslRepository paymentQueryDslRepository;
    private final PaymentJpaEntityMapper paymentJpaEntityMapper;

    public PaymentQueryAdapter(
            PaymentQueryDslRepository paymentQueryDslRepository,
            PaymentJpaEntityMapper paymentJpaEntityMapper) {
        this.paymentQueryDslRepository = paymentQueryDslRepository;
        this.paymentJpaEntityMapper = paymentJpaEntityMapper;
    }

    /**
     * ID로 Payment 단건 조회
     *
     * @param paymentId Payment ID (Value Object)
     * @return Payment Domain (Optional)
     */
    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        Optional<PaymentJpaEntity> entity = paymentQueryDslRepository.findById(paymentId.value());
        return entity.map(paymentJpaEntityMapper::toDomain);
    }

    /**
     * ID로 Payment 단건 조회 (필수)
     *
     * @param paymentId Payment ID (Value Object)
     * @return Payment Domain
     * @throws PaymentNotFoundException Payment이 존재하지 않는 경우
     */
    @Override
    public Payment getById(PaymentId paymentId) {
        return findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    /**
     * CheckoutId로 Payment 단건 조회
     *
     * @param checkoutId Checkout ID (Value Object)
     * @return Payment Domain (Optional)
     */
    @Override
    public Optional<Payment> findByCheckoutId(CheckoutId checkoutId) {
        Optional<PaymentJpaEntity> entity =
                paymentQueryDslRepository.findByCheckoutId(checkoutId.value());
        return entity.map(paymentJpaEntityMapper::toDomain);
    }

    /**
     * CheckoutId로 Payment 단건 조회 (필수)
     *
     * @param checkoutId Checkout ID (Value Object)
     * @return Payment Domain
     * @throws PaymentNotFoundException Payment이 존재하지 않는 경우
     */
    @Override
    public Payment getByCheckoutId(CheckoutId checkoutId) {
        return findByCheckoutId(checkoutId)
                .orElseThrow(() -> new PaymentNotFoundException(checkoutId.value()));
    }

    /**
     * 조건으로 Payment 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param query 조회 조건
     * @return Payment 목록
     */
    @Override
    public List<Payment> findByQuery(GetPaymentsQuery query) {
        PaymentSearchCondition condition = toSearchCondition(query);
        List<PaymentJpaEntity> entities = paymentQueryDslRepository.findByCondition(condition);
        return entities.stream().map(paymentJpaEntityMapper::toDomain).toList();
    }

    /**
     * Legacy Payment ID로 Payment 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 조회
     *
     * @param legacyPaymentId Legacy Payment ID (Long)
     * @return Payment Domain (Optional)
     */
    @Override
    public Optional<Payment> findByLegacyPaymentId(Long legacyPaymentId) {
        Optional<PaymentJpaEntity> entity =
                paymentQueryDslRepository.findByLegacyPaymentId(legacyPaymentId);
        return entity.map(paymentJpaEntityMapper::toDomain);
    }

    /**
     * Query → SearchCondition 변환
     *
     * @param query 조회 Query
     * @return PaymentSearchCondition
     */
    private PaymentSearchCondition toSearchCondition(GetPaymentsQuery query) {
        UUID lastPaymentId = null;
        if (query.hasCursor()) {
            lastPaymentId = UUID.fromString(query.lastPaymentId());
        }

        return PaymentSearchCondition.of(
                query.memberId(),
                query.statuses(),
                query.startDate(),
                query.endDate(),
                lastPaymentId,
                query.pageSize());
    }
}
