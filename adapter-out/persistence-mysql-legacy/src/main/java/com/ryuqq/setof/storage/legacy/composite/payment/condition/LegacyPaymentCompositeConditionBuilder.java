package com.ryuqq.setof.storage.legacy.composite.payment.condition;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentEntity.legacyPaymentEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentCompositeConditionBuilder - 결제 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentCompositeConditionBuilder {

    // ===== ID 조건 =====

    /**
     * 결제 ID 일치 조건.
     *
     * @param paymentId 결제 ID
     * @return BooleanExpression
     */
    public BooleanExpression paymentIdEq(Long paymentId) {
        return paymentId != null ? legacyPaymentEntity.id.eq(paymentId) : null;
    }

    /**
     * 결제 ID 목록 포함 조건.
     *
     * @param paymentIds 결제 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression paymentIdIn(List<Long> paymentIds) {
        return paymentIds != null && !paymentIds.isEmpty()
                ? legacyPaymentEntity.id.in(paymentIds)
                : null;
    }

    /**
     * 결제 ID 미만 조건 (커서 기반 페이징).
     *
     * @param lastPaymentId 마지막 결제 ID (커서)
     * @return BooleanExpression
     */
    public BooleanExpression paymentIdLt(Long lastPaymentId) {
        return lastPaymentId != null ? legacyPaymentEntity.id.lt(lastPaymentId) : null;
    }

    // ===== 사용자 조건 =====

    /**
     * 사용자 ID 일치 조건 (payment 테이블).
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(long userId) {
        return legacyPaymentEntity.userId.eq(userId);
    }

    // ===== 날짜 조건 =====

    /**
     * 등록일시 범위 조건 (payment.insert_date 기준).
     *
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @return BooleanExpression
     */
    public BooleanExpression insertDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        return legacyPaymentEntity.insertDate.between(startDate, endDate);
    }

    // ===== 상태 조건 =====

    /**
     * 결제 실패 상태 제외 조건 (PAYMENT_FAILED 제외).
     *
     * @return BooleanExpression
     */
    public BooleanExpression paymentStatusNotFailed() {
        return legacyPaymentEntity.paymentStatus.ne(LegacyPaymentStatus.PAYMENT_FAILED);
    }

    /**
     * 주문 상태 목록 포함 조건 (orders 테이블 JOIN 필요).
     *
     * @param orderStatuses 주문 상태 목록 (문자열)
     * @return BooleanExpression
     */
    public BooleanExpression orderStatusIn(List<String> orderStatuses) {
        if (orderStatuses == null || orderStatuses.isEmpty()) {
            return null;
        }
        return legacyOrderEntity.orderStatus.stringValue().in(orderStatuses);
    }
}
