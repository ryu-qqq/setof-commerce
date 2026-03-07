package com.ryuqq.setof.application.refundaccount.manager;

import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import org.springframework.stereotype.Component;

/**
 * AccountVerificationManager - 계좌 실명 검증 Manager.
 *
 * <p>AccountVerificationPort를 래핑하여 외부 API 호출을 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class AccountVerificationManager {

    private final AccountVerificationPort accountVerificationPort;

    public AccountVerificationManager(AccountVerificationPort accountVerificationPort) {
        this.accountVerificationPort = accountVerificationPort;
    }

    public boolean verify(String bankCode, String accountNumber, String accountHolderName) {
        return accountVerificationPort.verifyAccount(bankCode, accountNumber, accountHolderName);
    }
}
