package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.id.DiscountTargetId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;

/**
 * DiscountTarget - 할인 적용 대상 (자식 엔티티).
 *
 * <p>DiscountPolicy Aggregate의 자식으로, 할인이 적용될 대상을 나타냅니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>targetType, targetId는 필수
 *   <li>targetId는 0보다 커야 함
 * </ul>
 */
public class DiscountTarget {

    private final DiscountTargetId id;
    private final DiscountTargetType targetType;
    private final long targetId;
    private boolean active;

    private DiscountTarget(
            DiscountTargetId id, DiscountTargetType targetType, long targetId, boolean active) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.active = active;
    }

    /**
     * 새 할인 대상 생성.
     *
     * @param targetType 대상 유형 (필수)
     * @param targetId 대상 ID (필수, 0 초과)
     * @return 새 DiscountTarget 인스턴스
     */
    public static DiscountTarget forNew(DiscountTargetType targetType, long targetId) {
        validateTargetType(targetType);
        validateTargetId(targetId);
        return new DiscountTarget(DiscountTargetId.forNew(), targetType, targetId, true);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @param active 활성 여부
     * @return 복원된 DiscountTarget 인스턴스
     */
    public static DiscountTarget reconstitute(
            DiscountTargetId id, DiscountTargetType targetType, long targetId, boolean active) {
        return new DiscountTarget(id, targetType, targetId, active);
    }

    private static void validateTargetType(DiscountTargetType targetType) {
        if (targetType == null) {
            throw new IllegalArgumentException("할인 대상 유형은 필수입니다");
        }
    }

    private static void validateTargetId(long targetId) {
        if (targetId <= 0) {
            throw new IllegalArgumentException("할인 대상 ID는 0보다 커야 합니다: " + targetId);
        }
    }

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /** 비활성화 */
    public void deactivate() {
        this.active = false;
    }

    /** 특정 대상과 매칭되는지 확인 */
    public boolean matches(DiscountTargetType type, long id) {
        return this.targetType == type && this.targetId == id && this.active;
    }

    // ========== Accessor Methods ==========

    public DiscountTargetId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public DiscountTargetType targetType() {
        return targetType;
    }

    public long targetId() {
        return targetId;
    }

    public boolean isActive() {
        return active;
    }
}
