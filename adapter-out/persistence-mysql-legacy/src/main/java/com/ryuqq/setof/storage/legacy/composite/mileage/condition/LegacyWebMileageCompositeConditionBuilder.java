package com.ryuqq.setof.storage.legacy.composite.mileage.condition;

import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyMileageEntity.legacyMileageEntity;
import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyMileageHistoryEntity.legacyMileageHistoryEntity;
import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyOrderSnapshotMileageDetailEntity.legacyOrderSnapshotMileageDetailEntity;
import static com.ryuqq.setof.storage.legacy.mileage.entity.QLegacyOrderSnapshotMileageEntity.legacyOrderSnapshotMileageEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.mileage.entity.LegacyMileageEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Mileage Composite QueryDSL 조건 빌더.
 *
 * <p>PER-REP-004: ConditionBuilder로 동적 쿼리 조건 분리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebMileageCompositeConditionBuilder {

    // ========== Mileage 조건 ==========

    public BooleanExpression mileageUserIdEq(Long userId) {
        return userId != null ? legacyMileageEntity.userId.eq(userId) : null;
    }

    public BooleanExpression mileageActiveYn() {
        return legacyMileageEntity.activeYn.eq(LegacyMileageEntity.ActiveYn.Y);
    }

    /**
     * 잔여 마일리지가 0보다 큰 조건.
     *
     * <p>mileageAmount - usedMileageAmount > 0
     *
     * @return BooleanExpression
     */
    public BooleanExpression hasRemainingMileage() {
        return legacyMileageEntity
                .mileageAmount
                .subtract(legacyMileageEntity.usedMileageAmount)
                .gt(0);
    }

    // ========== MileageHistory 조건 ==========

    public BooleanExpression historyUserIdEq(Long userId) {
        return userId != null ? legacyMileageHistoryEntity.userId.eq(userId) : null;
    }

    public BooleanExpression reasonIn(List<String> reasons) {
        return reasons != null && !reasons.isEmpty()
                ? legacyMileageHistoryEntity.reason.in(reasons)
                : null;
    }

    /**
     * targetId가 0인 조건 (비주문 마일리지).
     *
     * @return BooleanExpression
     */
    public BooleanExpression targetIdIsZero() {
        return legacyMileageHistoryEntity.targetId.eq(0L);
    }

    // ========== OrderSnapshotMileage 조건 ==========

    /**
     * 유효한 주문/결제 ID가 존재하는 조건.
     *
     * <p>orderId > 0 AND paymentId > 0
     *
     * @return BooleanExpression
     */
    public BooleanExpression hasValidOrderIdAndPaymentId() {
        return legacyOrderSnapshotMileageEntity
                .orderId
                .gt(0L)
                .and(legacyOrderSnapshotMileageEntity.paymentId.gt(0L));
    }

    /**
     * 주문/결제 ID가 없는 조건 (NULL 또는 0).
     *
     * @return BooleanExpression
     */
    public BooleanExpression noOrderIdAndPaymentId() {
        return legacyOrderSnapshotMileageEntity
                .orderId
                .isNull()
                .or(legacyOrderSnapshotMileageEntity.orderId.eq(0L));
    }

    // ========== 조인 조건 ==========

    /**
     * mileage ↔ mileageHistory 조인 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression mileageHistoryJoinCondition() {
        return legacyMileageEntity.id.eq(legacyMileageHistoryEntity.mileageId);
    }

    /**
     * mileage ↔ orderSnapshotMileageDetail 조인 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression orderSnapshotDetailJoinCondition() {
        return legacyOrderSnapshotMileageDetailEntity.mileageId.eq(legacyMileageEntity.id);
    }

    /**
     * orderSnapshotMileageDetail ↔ orderSnapshotMileage 조인 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression orderSnapshotJoinCondition() {
        return legacyOrderSnapshotMileageEntity.id.eq(
                legacyOrderSnapshotMileageDetailEntity.orderSnapshotMileageId);
    }

    /**
     * orderSnapshotMileage.orderId = mileageHistory.targetId 조인 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression orderIdTargetIdJoinCondition() {
        return legacyOrderSnapshotMileageEntity.orderId.eq(legacyMileageHistoryEntity.targetId);
    }
}
