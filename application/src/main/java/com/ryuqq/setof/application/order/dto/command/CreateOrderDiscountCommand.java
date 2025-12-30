package com.ryuqq.setof.application.order.dto.command;

/**
 * 주문 할인 생성 Command
 *
 * <p>주문에 적용할 할인 정보를 전달합니다.
 *
 * @param discountPolicyId 할인 정책 ID
 * @param discountGroup 할인 그룹 (PRODUCT, MEMBER, PAYMENT)
 * @param amount 할인 금액 (원)
 * @param policyName 정책명
 * @author development-team
 * @since 1.0.0
 */
public record CreateOrderDiscountCommand(
        Long discountPolicyId, String discountGroup, long amount, String policyName) {

    public CreateOrderDiscountCommand {
        if (discountPolicyId == null || discountPolicyId <= 0) {
            throw new IllegalArgumentException("discountPolicyId는 필수입니다");
        }
        if (discountGroup == null || discountGroup.isBlank()) {
            throw new IllegalArgumentException("discountGroup은 필수입니다");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("할인 금액은 0 이상이어야 합니다");
        }
        if (policyName == null || policyName.isBlank()) {
            throw new IllegalArgumentException("policyName은 필수입니다");
        }
    }
}
