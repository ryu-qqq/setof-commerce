package com.ryuqq.setof.storage.legacy.composite.order.condition;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebOrderCompositeConditionBuilder - 레거시 주문 Composite QueryDSL 조건 빌더.
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
public class LegacyWebOrderCompositeConditionBuilder {

    // ===== ID 조건 =====

    /**
     * 주문 ID 일치 조건.
     *
     * @param orderId 주문 ID
     * @return BooleanExpression
     */
    public BooleanExpression orderIdEq(Long orderId) {
        return orderId != null ? legacyOrderEntity.id.eq(orderId) : null;
    }

    /**
     * 주문 ID 목록 포함 조건.
     *
     * @param orderIds 주문 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression orderIdIn(List<Long> orderIds) {
        return orderIds != null && !orderIds.isEmpty() ? legacyOrderEntity.id.in(orderIds) : null;
    }

    /**
     * 주문 ID 미만 조건 (커서 기반 페이징).
     *
     * @param lastDomainId 마지막 주문 ID
     * @return BooleanExpression
     */
    public BooleanExpression orderIdLt(Long lastDomainId) {
        return lastDomainId != null ? legacyOrderEntity.id.lt(lastDomainId) : null;
    }

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(long userId) {
        return legacyOrderEntity.userId.eq(userId);
    }

    /**
     * 결제 ID 일치 조건.
     *
     * @param paymentId 결제 ID
     * @return BooleanExpression
     */
    public BooleanExpression paymentIdEq(Long paymentId) {
        return paymentId != null ? legacyOrderEntity.paymentId.eq(paymentId) : null;
    }

    // ===== 날짜 조건 =====

    /**
     * 등록일시 범위 조건.
     *
     * @param startDate 시작일
     * @param endDate 종료일
     * @return BooleanExpression
     */
    public BooleanExpression insertDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        return legacyOrderEntity.insertDate.between(startDate, endDate);
    }

    /**
     * 수정일시 이후 조건 (최근 3개월).
     *
     * @return BooleanExpression
     */
    public BooleanExpression updateDateAfter3Months() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return legacyOrderEntity.updateDate.after(threeMonthsAgo);
    }

    // ===== 상태 조건 =====

    /**
     * 주문 상태 목록 포함 조건.
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

    /**
     * 리뷰 미작성 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression reviewNotYet() {
        return legacyOrderEntity.reviewYn.eq(
                com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderEntity.Yn.N);
    }
}
