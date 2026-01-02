package com.ryuqq.setof.adapter.out.persistence.payment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.payment.condition.PaymentSearchCondition;
import com.ryuqq.setof.adapter.out.persistence.payment.entity.PaymentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.payment.entity.QPaymentJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * PaymentQueryDslRepository - Payment QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(UUID id): ID로 단건 조회
 *   <li>findByCheckoutId(UUID checkoutId): CheckoutId로 단건 조회
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>동적 쿼리 구성 (BooleanExpression)
 *   <li>단건/목록 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지 (fetch join, left join, inner join)
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class PaymentQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QPaymentJpaEntity qPayment = QPaymentJpaEntity.paymentJpaEntity;

    public PaymentQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Payment 단건 조회
     *
     * @param id Payment ID (UUID)
     * @return PaymentJpaEntity (Optional)
     */
    public Optional<PaymentJpaEntity> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qPayment).where(qPayment.id.eq(id)).fetchOne());
    }

    /**
     * CheckoutId로 Payment 단건 조회
     *
     * @param checkoutId Checkout ID (UUID)
     * @return PaymentJpaEntity (Optional)
     */
    public Optional<PaymentJpaEntity> findByCheckoutId(UUID checkoutId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qPayment)
                        .where(qPayment.checkoutId.eq(checkoutId))
                        .fetchOne());
    }

    /**
     * ID로 Payment 존재 여부 확인
     *
     * @param id Payment ID (UUID)
     * @return 존재 여부
     */
    public boolean existsById(UUID id) {
        Integer count =
                queryFactory.selectOne().from(qPayment).where(qPayment.id.eq(id)).fetchFirst();

        return count != null;
    }

    /**
     * 검색 조건으로 Payment 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param condition 검색 조건
     * @return PaymentJpaEntity 목록
     */
    public List<PaymentJpaEntity> findByCondition(PaymentSearchCondition condition) {
        BooleanBuilder builder = buildConditions(condition);

        return queryFactory
                .selectFrom(qPayment)
                .where(builder)
                .orderBy(qPayment.createdAt.desc(), qPayment.id.desc())
                .limit(condition.limit() + 1L)
                .fetch();
    }

    /**
     * Legacy Payment ID로 Payment 단건 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 조회 메서드
     *
     * @param legacyPaymentId Legacy Payment ID (Long)
     * @return PaymentJpaEntity (Optional)
     */
    public Optional<PaymentJpaEntity> findByLegacyPaymentId(Long legacyPaymentId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qPayment)
                        .where(qPayment.legacyPaymentId.eq(legacyPaymentId))
                        .fetchOne());
    }

    /**
     * 검색 조건 BooleanBuilder 생성
     *
     * @param condition 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildConditions(PaymentSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qPayment.memberId.eq(condition.memberId()));

        if (condition.hasStatuses()) {
            builder.and(qPayment.status.in(condition.statuses()));
        }

        if (condition.hasStartDate()) {
            builder.and(qPayment.createdAt.goe(condition.startDate()));
        }

        if (condition.hasEndDate()) {
            builder.and(qPayment.createdAt.loe(condition.endDate()));
        }

        if (condition.hasCursor()) {
            builder.and(qPayment.id.lt(condition.lastPaymentId()));
        }

        return builder;
    }
}
