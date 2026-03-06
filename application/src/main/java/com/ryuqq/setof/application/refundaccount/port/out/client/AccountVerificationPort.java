package com.ryuqq.setof.application.refundaccount.port.out.client;

/**
 * Account Verification Port
 *
 * <p>계좌 실명 검증을 위한 아웃바운드 포트입니다.
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>AccountVerificationAdapter - 포트원(PortOne) API 기반 계좌 실명 검증
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface AccountVerificationPort {

    /**
     * 계좌 실명을 검증합니다.
     *
     * @param bankCode 은행 코드
     * @param accountNumber 계좌 번호
     * @param accountHolderName 예금주명
     * @return 검증 성공 여부
     */
    boolean verifyAccount(String bankCode, String accountNumber, String accountHolderName);
}
