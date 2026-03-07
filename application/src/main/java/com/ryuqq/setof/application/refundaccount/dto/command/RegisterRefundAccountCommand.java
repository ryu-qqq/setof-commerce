package com.ryuqq.setof.application.refundaccount.dto.command;

/**
 * 환불 계좌 등록 Command DTO.
 *
 * @param userId 회원 ID (SecurityContext에서 추출)
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterRefundAccountCommand(
        Long userId, String bankName, String accountNumber, String accountHolderName) {}
