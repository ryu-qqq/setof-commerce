package com.ryuqq.setof.application.payment.dto.command;

import java.util.List;

/**
 * 장바구니 구매 결제 커맨드.
 *
 * @param userId 사용자 ID
 * @param payAmount 현금 결제 금액
 * @param mileageAmount 마일리지 사용 금액
 * @param payMethod 결제 수단
 * @param shippingInfo 배송지 정보
 * @param refundAccountInfo 환불 계좌 정보 (가상계좌 시)
 * @param orderItems 장바구니 주문 항목 목록 (cartId 포함)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CreatePaymentInCartCommand(
        long userId,
        long payAmount,
        long mileageAmount,
        String payMethod,
        ShippingInfoCommand shippingInfo,
        RefundAccountInfoCommand refundAccountInfo,
        List<CartOrderItemCommand> orderItems) {}
