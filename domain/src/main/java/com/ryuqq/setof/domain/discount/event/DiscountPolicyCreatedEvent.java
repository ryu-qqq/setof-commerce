package com.ryuqq.setof.domain.discount.event;

import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.time.Instant;

/**
 * 할인 정책 생성 이벤트
 *
 * <p>새로운 할인 정책이 생성되었을 때 발행됩니다.
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
 * @param policyName 정책명
 * @param discountGroup 할인 그룹
 * @param discountType 할인 타입
 * @param occurredAt 이벤트 발생 시각
 */
public record DiscountPolicyCreatedEvent(
        Long discountPolicyId,
        Long sellerId,
        String policyName,
        DiscountGroup discountGroup,
        DiscountType discountType,
        Instant occurredAt) {

    /**
     * Static Factory Method
     *
     * @param discountPolicyId 할인 정책 ID
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param discountGroup 할인 그룹
     * @param discountType 할인 타입
     * @return 이벤트 인스턴스
     */
    public static DiscountPolicyCreatedEvent of(
            Long discountPolicyId,
            Long sellerId,
            String policyName,
            DiscountGroup discountGroup,
            DiscountType discountType) {
        return new DiscountPolicyCreatedEvent(
                discountPolicyId, sellerId, policyName, discountGroup, discountType, Instant.now());
    }
}
