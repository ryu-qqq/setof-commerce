package com.ryuqq.setof.application.claim.dto.command;

/**
 * RegisterExchangeShippingCommand - 교환품 발송 등록 Command
 *
 * <p>교환 클레임에서 교환품을 발송할 때 사용합니다.
 *
 * @param claimId 클레임 ID
 * @param trackingNumber 송장 번호
 * @param carrier 배송사
 * @author development-team
 * @since 2.0.0
 */
public record RegisterExchangeShippingCommand(
        String claimId, String trackingNumber, String carrier) {}
