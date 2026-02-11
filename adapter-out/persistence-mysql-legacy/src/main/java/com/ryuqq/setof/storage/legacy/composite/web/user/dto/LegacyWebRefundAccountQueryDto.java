package com.ryuqq.setof.storage.legacy.composite.web.user.dto;

/**
 * 레거시 환불 계좌 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param refundAccountId 환불 계좌 ID
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebRefundAccountQueryDto(
        long refundAccountId, String bankName, String accountNumber, String accountHolderName) {}
