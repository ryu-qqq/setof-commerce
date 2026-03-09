package com.ryuqq.setof.domain.order.vo;

import java.util.List;

/**
 * 주문 아이템 가격 분해 VO.
 *
 * <p>가격 계산 흐름:
 *
 * <pre>
 * regularPrice (정가)
 *   ↓ appliedDiscounts (할인 내역들 순차 적용)
 * salePrice (최종 단가)
 *   × quantity
 *   + shippingFee
 * = orderAmount (최종 결제 금액)
 * </pre>
 *
 * <p>할인 내역은 {@link AppliedDiscountSnapshot} 리스트로 관리합니다. 쿠폰/마일리지 등 새 할인 유형이 추가되어도 이 VO 코드 변경 없이 리스트에
 * 자연스럽게 포함됩니다.
 *
 * @param regularPrice 정가 (단품)
 * @param salePrice 판매가 (단품, 할인 적용 후)
 * @param orderAmount 최종 결제 금액 (수량 반영)
 * @param shippingFee 배송비
 * @param appliedDiscounts 적용된 할인 내역 스냅샷
 */
public record OrderItemPrice(
        int regularPrice,
        int salePrice,
        int orderAmount,
        int shippingFee,
        List<AppliedDiscountSnapshot> appliedDiscounts) {

    public OrderItemPrice {
        if (regularPrice < 0) {
            throw new IllegalArgumentException("정가는 0 이상이어야 합니다: " + regularPrice);
        }
        if (orderAmount < 0) {
            throw new IllegalArgumentException("주문금액은 0 이상이어야 합니다: " + orderAmount);
        }
        appliedDiscounts = appliedDiscounts != null ? List.copyOf(appliedDiscounts) : List.of();
    }

    /** 총 할인액. */
    public int totalDiscount() {
        return appliedDiscounts.stream().mapToInt(AppliedDiscountSnapshot::amount).sum();
    }

    /** 정가 대비 할인율 (%). */
    public int discountRate() {
        if (regularPrice == 0) {
            return 0;
        }
        return totalDiscount() * 100 / regularPrice;
    }

    /** 특정 할인 유형의 금액 조회. */
    public int discountAmountOf(String discountType) {
        return appliedDiscounts.stream()
                .filter(d -> discountType.equals(d.discountType()))
                .mapToInt(AppliedDiscountSnapshot::amount)
                .sum();
    }
}
