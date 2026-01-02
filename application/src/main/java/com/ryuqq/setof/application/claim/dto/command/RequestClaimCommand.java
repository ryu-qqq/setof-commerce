package com.ryuqq.setof.application.claim.dto.command;

import java.math.BigDecimal;

/**
 * RequestClaimCommand - 클레임 요청 Command
 *
 * @param orderId 주문 ID
 * @param orderItemId 주문 상품 ID (전체 주문 클레임 시 null)
 * @param claimType 클레임 유형 (String으로 전달, Service에서 enum 변환)
 * @param claimReason 클레임 사유 (String으로 전달, Service에서 enum 변환)
 * @param claimReasonDetail 상세 사유
 * @param quantity 클레임 수량
 * @param refundAmount 환불 예정 금액
 * @author development-team
 * @since 2.0.0
 */
public record RequestClaimCommand(
        String orderId,
        String orderItemId,
        String claimType,
        String claimReason,
        String claimReasonDetail,
        Integer quantity,
        BigDecimal refundAmount) {}
