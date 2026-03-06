package com.ryuqq.setof.adapter.out.client.portone.adapter;

import com.ryuqq.setof.adapter.out.client.portone.client.PortOneClient;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneBankHolderResponse;
import com.ryuqq.setof.application.refundaccount.port.out.client.AccountClient;
import com.ryuqq.setof.application.refundaccount.port.out.client.AccountHolderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Account Verification Adapter
 *
 * <p>포트원(PortOne) API를 사용하여 계좌 예금주를 조회합니다.
 *
 * <p><strong>TODO: V2 API 마이그레이션 필요</strong>
 *
 * <ul>
 *   <li>현재: V1 {@code /vbanks/holder} 엔드포인트 사용
 *   <li>변경: V2 {@code /platform/bank-accounts/{bank}/{accountNumber}/holder} 사용
 *   <li>응답: {@code holderName} + {@code accountVerificationId}
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class AccountVerificationAdapter implements AccountClient {

    private static final Logger log = LoggerFactory.getLogger(AccountVerificationAdapter.class);

    private final PortOneClient portOneClient;
    private final PortOneProperties properties;

    public AccountVerificationAdapter(PortOneClient portOneClient, PortOneProperties properties) {
        this.portOneClient = portOneClient;
        this.properties = properties;
    }

    @Override
    public AccountHolderInfo fetchAccountHolder(String bankCode, String accountNumber) {
        if (!properties.isEnabled()) {
            log.info(
                    "PortOne is disabled. Skipping account verification. bankCode={},"
                            + " accountNumber={}",
                    bankCode,
                    maskAccountNumber(accountNumber));
            return AccountHolderInfo.empty();
        }

        try {
            PortOneBankHolderResponse response =
                    portOneClient.fetchBankHolder(bankCode, accountNumber);

            if (!response.hasHolder()) {
                log.warn(
                        "Bank holder not found. bankCode={}, accountNumber={}",
                        bankCode,
                        maskAccountNumber(accountNumber));
                return AccountHolderInfo.empty();
            }

            return new AccountHolderInfo(response.bankHolderInfo(), "");

        } catch (Exception e) {
            log.error(
                    "Failed to fetch bank holder. bankCode={}, accountNumber={}",
                    bankCode,
                    maskAccountNumber(accountNumber),
                    e);
            return AccountHolderInfo.empty();
        }
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
