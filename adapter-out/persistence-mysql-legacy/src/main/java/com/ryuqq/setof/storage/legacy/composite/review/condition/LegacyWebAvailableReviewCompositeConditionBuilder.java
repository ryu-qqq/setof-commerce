package com.ryuqq.setof.storage.legacy.composite.review.condition;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderEntity;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web 작성 가능한 리뷰 주문 Composite QueryDSL 조건 빌더.
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
public class LegacyWebAvailableReviewCompositeConditionBuilder {

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
     * 주문 ID 미만 조건 (커서 기반 페이징).
     *
     * @param lastOrderId 마지막 주문 ID
     * @return BooleanExpression
     */
    public BooleanExpression orderIdLt(Long lastOrderId) {
        return lastOrderId != null ? legacyOrderEntity.id.lt(lastOrderId) : null;
    }

    /**
     * 리뷰 미작성 조건 (review_yn = 'N').
     *
     * @return BooleanExpression
     */
    public BooleanExpression reviewNotYet() {
        return legacyOrderEntity.reviewYn.eq(LegacyOrderEntity.Yn.N);
    }

    /**
     * 리뷰 가능 주문 상태 조건.
     *
     * <p>DELIVERED 상태 주문만 리뷰 작성 가능.
     *
     * @return BooleanExpression
     */
    public BooleanExpression reviewableOrderStatus() {
        return legacyOrderEntity.orderStatus.in(
                List.of(
                        LegacyOrderStatus.DELIVERY_COMPLETED,
                        LegacyOrderStatus.SETTLEMENT_PROCESSING,
                        LegacyOrderStatus.SETTLEMENT_COMPLETED));
    }

    /**
     * 주문일 3개월 이내 조건.
     *
     * <p>주문 후 3개월 이내만 리뷰 작성 가능.
     *
     * @return BooleanExpression
     */
    public BooleanExpression withinThreeMonths() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return legacyOrderEntity.insertDate.after(threeMonthsAgo);
    }
}
