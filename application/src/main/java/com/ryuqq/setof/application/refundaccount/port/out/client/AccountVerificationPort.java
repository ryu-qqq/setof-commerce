package com.ryuqq.setof.application.refundaccount.port.out.client;

/**
 * Account Verification Port (External API)
 *
 * <p>외부 계좌 검증 API를 호출하는 Port
 *
 * <p>구현체는 Adapter Out 모듈에서 실제 외부 API 연동
 *
 * @author development-team
 * @since 1.0.0
 */
public interface AccountVerificationPort {

    /**
     * 계좌 실명 검증
     *
     * @param bankCode 은행코드
     * @param accountNumber 계좌번호
     * @param accountHolderName 예금주명
     * @return 검증 성공하면 true
     */
    boolean verifyAccount(String bankCode, String accountNumber, String accountHolderName);
}
