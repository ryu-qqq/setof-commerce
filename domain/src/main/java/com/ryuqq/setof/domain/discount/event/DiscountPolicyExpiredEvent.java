package com.ryuqq.setof.domain.discount.event;

import java.time.Instant;

/**
 * 할인 정책 만료 이벤트
 *
 * <p>할인 정책의 유효 기간이 만료되었을 때 발행됩니다.
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
 * @param expiredAt 만료 시각
 * @param occurredAt 이벤트 발생 시각
 */
public record DiscountPolicyExpiredEvent(
        Long discountPolicyId, Long sellerId, Instant expiredAt, Instant occurredAt) {

    /**
     * Static Factory Method
     *
     * @param discountPolicyId 할인 정책 ID
     * @param sellerId 셀러 ID
     * @param expiredAt 만료 시각
     * @return 이벤트 인스턴스
     */
    public static DiscountPolicyExpiredEvent of(
            Long discountPolicyId, Long sellerId, Instant expiredAt) {
        return new DiscountPolicyExpiredEvent(discountPolicyId, sellerId, expiredAt, Instant.now());
    }
}
