package com.ryuqq.setof.application.claim.dto.command;

/**
 * ConfirmExchangeDeliveredCommand - 교환품 배송 완료 확인 Command
 *
 * <p>고객이 교환품을 수령했음을 확인합니다.
 *
 * @param claimId 클레임 ID
 * @author development-team
 * @since 2.0.0
 */
public record ConfirmExchangeDeliveredCommand(String claimId) {}
