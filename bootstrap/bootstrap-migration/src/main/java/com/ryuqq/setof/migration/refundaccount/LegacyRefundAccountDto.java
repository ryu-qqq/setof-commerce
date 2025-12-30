package com.ryuqq.setof.migration.refundaccount;

import java.time.LocalDateTime;

/**
 * 레거시 REFUND_ACCOUNT 테이블 조회 DTO
 *
 * @param refundAccountId 레거시 환불계좌 ID
 * @param userId 레거시 사용자 ID
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @param createdAt 생성일시
 * @param modifiedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record LegacyRefundAccountDto(
        Long refundAccountId,
        Long userId,
        String bankName,
        String accountNumber,
        String accountHolderName,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {}
