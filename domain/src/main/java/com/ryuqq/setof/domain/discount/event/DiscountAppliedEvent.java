package com.ryuqq.setof.domain.discount.event;

import java.time.Instant;

/**
 * 할인 적용 이벤트
 *
 * <p>주문에 할인이 적용되었을 때 발행됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param discountPolicyId 할인 정책 ID
 * @param orderId 주문 ID
 * @param memberId 회원 ID
 * @param originalAmount 원 금액
 * @param discountAmount 할인 금액
 * @param platformCost 플랫폼 부담 금액
 * @param sellerCost 셀러 부담 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record DiscountAppliedEvent(
        Long discountPolicyId,
        Long orderId,
        Long memberId,
        long originalAmount,
        long discountAmount,
        long platformCost,
        long sellerCost,
        Instant occurredAt) {

    /**
     * Static Factory Method
     *
     * @param discountPolicyId 할인 정책 ID
     * @param orderId 주문 ID
     * @param memberId 회원 ID
     * @param originalAmount 원 금액
     * @param discountAmount 할인 금액
     * @param platformCost 플랫폼 부담 금액
     * @param sellerCost 셀러 부담 금액
     * @return 이벤트 인스턴스
     */
    public static DiscountAppliedEvent of(
            Long discountPolicyId,
            Long orderId,
            Long memberId,
            long originalAmount,
            long discountAmount,
            long platformCost,
            long sellerCost) {
        return new DiscountAppliedEvent(
                discountPolicyId,
                orderId,
                memberId,
                originalAmount,
                discountAmount,
                platformCost,
                sellerCost,
                Instant.now());
    }
}
