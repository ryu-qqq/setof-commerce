package com.ryuqq.setof.domain.claim.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 클레임 배송(수거) 상태.
 *
 * <p>반품/교환 수거의 생명주기를 나타내며, 상태 전이 규칙을 정의합니다.
 *
 * <p><strong>상태 전이 규칙:</strong>
 *
 * <ul>
 *   <li>PENDING -> IN_TRANSIT, FAILED
 *   <li>IN_TRANSIT -> DELIVERED, FAILED
 *   <li>DELIVERED -> (종료 상태)
 *   <li>FAILED -> PENDING (재시도 가능)
 * </ul>
 */
public enum ClaimShipmentStatus {
    PENDING("대기중"),
    IN_TRANSIT("수거중"),
    DELIVERED("수거완료"),
    FAILED("실패");

    private static final Map<ClaimShipmentStatus, Set<ClaimShipmentStatus>> VALID_TRANSITIONS;

    static {
        VALID_TRANSITIONS = new EnumMap<>(ClaimShipmentStatus.class);
        VALID_TRANSITIONS.put(PENDING, Set.of(IN_TRANSIT, FAILED));
        VALID_TRANSITIONS.put(IN_TRANSIT, Set.of(DELIVERED, FAILED));
        VALID_TRANSITIONS.put(FAILED, Set.of(PENDING));
    }

    private final String displayName;

    ClaimShipmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    /**
     * 현재 상태에서 대상 상태로 전이 가능한지 확인합니다.
     *
     * @param target 전이 대상 상태
     * @return 전이 가능 여부
     */
    public boolean canTransitionTo(ClaimShipmentStatus target) {
        Set<ClaimShipmentStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * <p>DELIVERED는 종료 상태입니다. FAILED는 재시도 가능하므로 종료 상태가 아닙니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return this == DELIVERED;
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * <p>종료 상태가 아닌 PENDING, IN_TRANSIT, FAILED는 활성 상태입니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return !isFinal();
    }

    /**
     * 수거 완료 상태인지 확인합니다.
     *
     * @return 수거 완료 여부
     */
    public boolean isCompleted() {
        return this == DELIVERED;
    }
}
