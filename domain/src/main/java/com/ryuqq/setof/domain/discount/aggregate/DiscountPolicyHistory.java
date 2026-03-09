package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.id.DiscountPolicyHistoryId;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountChangeType;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicySnapshot;
import java.time.Instant;

/**
 * DiscountPolicyHistory - 할인 정책 변경 이력 Aggregate.
 *
 * <p>정책의 생성/수정/활성화/비활성화/삭제/대상변경 시 before/after 스냅샷을 저장합니다. 아웃박스 → SQS → Consumer 흐름으로 비동기 저장됩니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>CREATED 타입은 beforeSnapshot이 null
 *   <li>changeType과 operatorId는 필수
 *   <li>afterSnapshot은 항상 필수
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DiscountPolicyHistory {

    private final DiscountPolicyHistoryId id;
    private final DiscountPolicyId policyId;
    private final DiscountChangeType changeType;
    private final DiscountPolicySnapshot beforeSnapshot;
    private final DiscountPolicySnapshot afterSnapshot;
    private final long operatorId;
    private final Instant changedAt;

    private DiscountPolicyHistory(
            DiscountPolicyHistoryId id,
            DiscountPolicyId policyId,
            DiscountChangeType changeType,
            DiscountPolicySnapshot beforeSnapshot,
            DiscountPolicySnapshot afterSnapshot,
            long operatorId,
            Instant changedAt) {
        this.id = id;
        this.policyId = policyId;
        this.changeType = changeType;
        this.beforeSnapshot = beforeSnapshot;
        this.afterSnapshot = afterSnapshot;
        this.operatorId = operatorId;
        this.changedAt = changedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 정책 생성 이력.
     *
     * @param policyId 정책 ID
     * @param afterSnapshot 생성 후 상태 스냅샷
     * @param operatorId 생성한 관리자 ID
     * @param now 생성 시각
     * @return 새 DiscountPolicyHistory 인스턴스
     */
    public static DiscountPolicyHistory forCreated(
            DiscountPolicyId policyId,
            DiscountPolicySnapshot afterSnapshot,
            long operatorId,
            Instant now) {
        return new DiscountPolicyHistory(
                DiscountPolicyHistoryId.forNew(),
                policyId,
                DiscountChangeType.CREATED,
                null,
                afterSnapshot,
                operatorId,
                now);
    }

    /**
     * 정책 변경 이력 (수정/활성화/비활성화/삭제/대상변경).
     *
     * @param policyId 정책 ID
     * @param changeType 변경 유형
     * @param beforeSnapshot 변경 전 상태 스냅샷
     * @param afterSnapshot 변경 후 상태 스냅샷
     * @param operatorId 변경한 관리자 ID
     * @param now 변경 시각
     * @return 새 DiscountPolicyHistory 인스턴스
     */
    public static DiscountPolicyHistory forChanged(
            DiscountPolicyId policyId,
            DiscountChangeType changeType,
            DiscountPolicySnapshot beforeSnapshot,
            DiscountPolicySnapshot afterSnapshot,
            long operatorId,
            Instant now) {
        if (changeType == DiscountChangeType.CREATED) {
            throw new IllegalArgumentException("CREATED 이력은 forCreated()를 사용하세요");
        }
        if (beforeSnapshot == null) {
            throw new IllegalArgumentException("변경 이력은 beforeSnapshot이 필수입니다");
        }
        return new DiscountPolicyHistory(
                DiscountPolicyHistoryId.forNew(),
                policyId,
                changeType,
                beforeSnapshot,
                afterSnapshot,
                operatorId,
                now);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @return 복원된 DiscountPolicyHistory 인스턴스
     */
    public static DiscountPolicyHistory reconstitute(
            DiscountPolicyHistoryId id,
            DiscountPolicyId policyId,
            DiscountChangeType changeType,
            DiscountPolicySnapshot beforeSnapshot,
            DiscountPolicySnapshot afterSnapshot,
            long operatorId,
            Instant changedAt) {
        return new DiscountPolicyHistory(
                id, policyId, changeType, beforeSnapshot, afterSnapshot, operatorId, changedAt);
    }

    // ========== Condition Checks ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /** 생성 이력인지 확인 */
    public boolean isCreated() {
        return changeType == DiscountChangeType.CREATED;
    }

    /** 변경 전 스냅샷이 존재하는지 확인 */
    public boolean hasBeforeSnapshot() {
        return beforeSnapshot != null;
    }

    // ========== Accessor Methods ==========

    public DiscountPolicyHistoryId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public DiscountPolicyId policyId() {
        return policyId;
    }

    public Long policyIdValue() {
        return policyId.value();
    }

    public DiscountChangeType changeType() {
        return changeType;
    }

    public DiscountPolicySnapshot beforeSnapshot() {
        return beforeSnapshot;
    }

    public DiscountPolicySnapshot afterSnapshot() {
        return afterSnapshot;
    }

    public long operatorId() {
        return operatorId;
    }

    public Instant changedAt() {
        return changedAt;
    }
}
