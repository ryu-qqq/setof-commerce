package com.ryuqq.setof.application.payment.dto.command;

/**
 * 결제 주문 항목 커맨드 (공통).
 *
 * @param productGroupId 상품 그룹 ID
 * @param productId 상품(옵션) ID
 * @param sellerId 판매자 ID
 * @param quantity 주문 수량
 * @param orderAmount 클라이언트가 계산한 주문 총 금액
 * @param orderStatus 주문 상태
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentOrderItemCommand(
        long productGroupId,
        long productId,
        long sellerId,
        int quantity,
        long orderAmount,
        String orderStatus) {}
