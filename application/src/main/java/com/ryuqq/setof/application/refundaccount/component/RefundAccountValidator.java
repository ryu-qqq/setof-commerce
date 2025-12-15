package com.ryuqq.setof.application.refundaccount.component;

import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * RefundAccount Validator Component
 *
 * <p>환불계좌 검증을 담당하는 컴포넌트입니다.
 *
 * <p>AccountVerificationPort를 래핑하여 계좌 검증 로직을 캡슐화합니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>계좌 실명 검증
 *   <li>검증 실패 시 예외 발생
 *   <li>검증 로깅
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountValidator {

    private static final Logger log = LoggerFactory.getLogger(RefundAccountValidator.class);

    private final AccountVerificationPort accountVerificationPort;

    public RefundAccountValidator(AccountVerificationPort accountVerificationPort) {
        this.accountVerificationPort = accountVerificationPort;
    }

    /**
     * 계좌 실명 검증 실행
     *
     * <p>외부 API를 통해 계좌의 예금주 정보를 검증합니다.
     *
     * @param bankCode 은행코드
     * @param accountNumber 계좌번호
     * @param accountHolderName 예금주명
     * @throws AccountVerificationFailedException 검증 실패 시
     */
    public void validateAccount(String bankCode, String accountNumber, String accountHolderName) {
        log.debug(
                "Validating account: bankCode={}, accountNumber={}***",
                bankCode,
                maskAccountNumber(accountNumber));

        boolean verified =
                accountVerificationPort.verifyAccount(bankCode, accountNumber, accountHolderName);

        if (!verified) {
            log.warn(
                    "Account verification failed: bankCode={}, accountNumber={}***",
                    bankCode,
                    maskAccountNumber(accountNumber));
            throw new AccountVerificationFailedException(bankCode, accountNumber);
        }

        log.info(
                "Account verification succeeded: bankCode={}, accountNumber={}***",
                bankCode,
                maskAccountNumber(accountNumber));
    }

    /**
     * 계좌 실명 검증 실행 (결과만 반환)
     *
     * <p>외부 API를 통해 계좌의 예금주 정보를 검증하고 결과만 반환합니다.
     *
     * <p>예외를 발생시키지 않으므로 호출자가 결과를 직접 처리해야 합니다.
     *
     * @param bankCode 은행코드
     * @param accountNumber 계좌번호
     * @param accountHolderName 예금주명
     * @return 검증 성공 여부
     */
    public boolean isValidAccount(String bankCode, String accountNumber, String accountHolderName) {
        log.debug(
                "Checking account validity: bankCode={}, accountNumber={}***",
                bankCode,
                maskAccountNumber(accountNumber));

        return accountVerificationPort.verifyAccount(bankCode, accountNumber, accountHolderName);
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return "****";
        }
        return accountNumber.substring(0, 4);
    }
}
