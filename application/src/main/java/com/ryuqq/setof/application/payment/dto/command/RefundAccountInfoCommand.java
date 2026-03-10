package com.ryuqq.setof.application.payment.dto.command;

/**
 * 환불 계좌 정보 커맨드.
 *
 * @param bankCode 은행 코드
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RefundAccountInfoCommand(
        String bankCode, String accountNumber, String accountHolderName) {}
