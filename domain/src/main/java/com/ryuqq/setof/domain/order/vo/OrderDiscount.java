package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.order.exception.InvalidOrderDiscountException;

/**
 * OrderDiscount Value Object
 *
 * <p>주문에 적용된 할인 정보 스냅샷입니다. 할인 정책이 변경되어도 주문 시점의 할인 정보는 보존됩니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>같은 DiscountGroup 내에서는 하나의 할인만 적용
 *   <li>서로 다른 DiscountGroup 간에는 중첩 적용 가능
 *   <li>policyName은 스냅샷으로 저장 (정책 변경에 영향받지 않음)
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param policyId 할인 정책 ID
 * @param discountGroup 할인 그룹 (PRODUCT, MEMBER, PAYMENT)
 * @param amount 할인 금액
 * @param policyName 정책명 스냅샷
 */
public record OrderDiscount(
        DiscountPolicyId policyId,
        DiscountGroup discountGroup,
        DiscountAmount amount,
        String policyName) {

    /** Compact Constructor - 검증 로직 */
    public OrderDiscount {
        validateNotNull(policyId, "policyId");
        validateNotNull(discountGroup, "discountGroup");
        validateNotNull(amount, "amount");
        validateRequired(policyName, "policyName");
    }

    /**
     * Static Factory Method - 주문 할인 생성
     *
     * @param policyId 할인 정책 ID
     * @param group 할인 그룹
     * @param amount 할인 금액
     * @param policyName 정책명
     * @return OrderDiscount 인스턴스
     */
    public static OrderDiscount of(
            DiscountPolicyId policyId,
            DiscountGroup group,
            DiscountAmount amount,
            String policyName) {
        return new OrderDiscount(policyId, group, amount, policyName);
    }

    // === Law of Demeter 준수 Helper Methods ===

    /**
     * 정책 ID 값 반환
     *
     * @return 정책 ID 값
     */
    public Long policyIdValue() {
        return policyId.value();
    }

    /**
     * 할인 금액 값 반환
     *
     * @return 할인 금액 (원)
     */
    public long amountValue() {
        return amount.value();
    }

    /**
     * 할인 그룹 설명 반환
     *
     * @return 할인 그룹 설명
     */
    public String discountGroupDescription() {
        return discountGroup.getDescription();
    }

    /**
     * 0원 할인인지 확인
     *
     * @return 할인 금액이 0원이면 true
     */
    public boolean isZeroDiscount() {
        return amount.isZero();
    }

    // === Private Validation Methods ===

    private static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidOrderDiscountException(fieldName, "은(는) 필수입니다.");
        }
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidOrderDiscountException(fieldName, "은(는) 필수입니다.");
        }
    }
}
