package com.ryuqq.setof.application.refundaccount.dto.command;

/**
 * 환불 계좌 삭제 Command DTO.
 *
 * @param userId 회원 ID (SecurityContext에서 추출, 소유권 검증용)
 * @param refundAccountId 삭제할 환불 계좌 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DeleteRefundAccountCommand(Long userId, Long refundAccountId) {}
