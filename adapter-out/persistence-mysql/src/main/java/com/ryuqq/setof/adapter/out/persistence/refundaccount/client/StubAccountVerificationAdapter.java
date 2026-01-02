package com.ryuqq.setof.adapter.out.persistence.refundaccount.client;

import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Stub Account Verification Adapter
 *
 * <p>AccountVerificationPort의 Stub 구현체입니다.
 *
 * <p>실제 외부 계좌 검증 API 연동 전까지 임시로 사용합니다. 모든 계좌 검증 요청에 대해 true를 반환합니다.
 *
 * <p><strong>주의:</strong> 운영 환경에서는 실제 계좌 검증 API 연동 필요
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class StubAccountVerificationAdapter implements AccountVerificationPort {

    private static final Logger log = LoggerFactory.getLogger(StubAccountVerificationAdapter.class);

    @Override
    public boolean verifyAccount(String bankCode, String accountNumber, String accountHolderName) {
        log.warn(
                "Using STUB account verification - always returns true. "
                        + "bankCode={}, accountNumber={}***, accountHolderName={}",
                bankCode,
                maskAccountNumber(accountNumber),
                accountHolderName);

        // Stub: 항상 true 반환
        // TODO: 실제 외부 계좌 검증 API 연동 시 이 클래스를 실제 구현체로 대체
        return true;
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return "****";
        }
        return accountNumber.substring(0, 4);
    }
}
