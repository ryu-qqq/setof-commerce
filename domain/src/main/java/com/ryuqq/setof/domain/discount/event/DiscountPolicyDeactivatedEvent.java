package com.ryuqq.setof.domain.discount.event;

import java.time.Instant;

/**
 * 할인 정책 비활성화 이벤트
 *
 * <p>할인 정책이 비활성화되었을 때 발행됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param discountPolicyId 할인 정책 ID
 * @param sellerId 셀러 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record DiscountPolicyDeactivatedEvent(
        Long discountPolicyId, Long sellerId, Instant occurredAt) {

    /**
     * Static Factory Method
     *
     * @param discountPolicyId 할인 정책 ID
     * @param sellerId 셀러 ID
     * @return 이벤트 인스턴스
     */
    public static DiscountPolicyDeactivatedEvent of(Long discountPolicyId, Long sellerId) {
        return new DiscountPolicyDeactivatedEvent(discountPolicyId, sellerId, Instant.now());
    }
}
