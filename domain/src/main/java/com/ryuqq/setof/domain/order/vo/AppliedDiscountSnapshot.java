package com.ryuqq.setof.domain.order.vo;

/**
 * 주문 시점 적용된 할인 내역 스냅샷 VO.
 *
 * <p>discount 도메인의 AppliedDiscount를 원시값으로 복사하여 보관합니다. order 패키지는 discount 패키지를 import하지 않습니다.
 *
 * <p>쿠폰/마일리지 등 새 할인 유형이 추가되어도 이 VO 변경 없이 리스트에 자연스럽게 포함됩니다.
 *
 * @param policyId 적용된 할인 정책 ID
 * @param discountType 할인 유형 (StackingGroup.name() 스냅샷, 예: "SELLER_INSTANT", "COUPON")
 * @param amount 할인 금액 (원)
 * @param description 할인 정책명 스냅샷 (예: "셀러 10% 할인")
 */
public record AppliedDiscountSnapshot(
        long policyId, String discountType, int amount, String description) {

    public AppliedDiscountSnapshot {
        if (amount < 0) {
            throw new IllegalArgumentException("할인 금액은 0 이상이어야 합니다: " + amount);
        }
    }

    public static AppliedDiscountSnapshot of(
            long policyId, String discountType, int amount, String description) {
        return new AppliedDiscountSnapshot(policyId, discountType, amount, description);
    }
}
