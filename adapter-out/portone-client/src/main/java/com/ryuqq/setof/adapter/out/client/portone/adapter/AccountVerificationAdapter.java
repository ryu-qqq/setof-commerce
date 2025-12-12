package com.ryuqq.setof.adapter.out.client.portone.adapter;

import com.ryuqq.setof.adapter.out.client.portone.client.PortOneClient;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneBankHolderResponse;
import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Account Verification Adapter
 *
 * <p>포트원(PortOne) API를 사용하여 계좌 실명 검증을 수행합니다.
 *
 * <p><strong>검증 로직:</strong>
 *
 * <ol>
 *   <li>포트원 API로 예금주 정보 조회
 *   <li>조회된 예금주명과 입력된 예금주명 비교
 *   <li>일치 시 true, 불일치 시 false 반환
 * </ol>
 *
 * <p><strong>주의사항:</strong>
 *
 * <ul>
 *   <li>포트원 API가 비활성화되면 항상 true 반환 (개발/테스트 환경)
 *   <li>예금주명 비교 시 공백 제거 후 비교
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class AccountVerificationAdapter implements AccountVerificationPort {

    private static final Logger log = LoggerFactory.getLogger(AccountVerificationAdapter.class);

    private final PortOneClient portOneClient;
    private final PortOneProperties properties;

    public AccountVerificationAdapter(PortOneClient portOneClient, PortOneProperties properties) {
        this.portOneClient = portOneClient;
        this.properties = properties;
    }

    @Override
    public boolean verifyAccount(String bankCode, String accountNumber, String accountHolderName) {
        if (!properties.isEnabled()) {
            log.info(
                    "PortOne is disabled. Skipping account verification. bankCode={},"
                            + " accountNumber={}",
                    bankCode,
                    maskAccountNumber(accountNumber));
            return true;
        }

        try {
            PortOneBankHolderResponse response =
                    portOneClient.fetchBankHolder(bankCode, accountNumber);

            if (!response.hasHolder()) {
                log.warn(
                        "Bank holder not found. bankCode={}, accountNumber={}",
                        bankCode,
                        maskAccountNumber(accountNumber));
                return false;
            }

            boolean matched =
                    normalizeHolderName(response.bankHolderInfo())
                            .equals(normalizeHolderName(accountHolderName));

            if (!matched) {
                log.warn(
                        "Bank holder name mismatch. bankCode={}, accountNumber={}, "
                                + "expected={}, actual={}",
                        bankCode,
                        maskAccountNumber(accountNumber),
                        accountHolderName,
                        response.bankHolderInfo());
            }

            return matched;

        } catch (Exception e) {
            log.error(
                    "Account verification failed. bankCode={}, accountNumber={}",
                    bankCode,
                    maskAccountNumber(accountNumber),
                    e);
            return false;
        }
    }

    /**
     * 예금주명 정규화
     *
     * <p>공백 제거 및 소문자 변환
     *
     * @param holderName 예금주명
     * @return 정규화된 예금주명
     */
    private String normalizeHolderName(String holderName) {
        if (holderName == null) {
            return "";
        }
        return holderName.replaceAll("\\s+", "").toLowerCase();
    }

    /**
     * 계좌번호 마스킹 (로깅용)
     *
     * @param accountNumber 계좌번호
     * @return 마스킹된 계좌번호
     */
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }
        return accountNumber.substring(0, 4) + "****";
    }
}
