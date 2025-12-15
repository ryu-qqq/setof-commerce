package com.ryuqq.setof.adapter.out.client.portone.client;

import com.ryuqq.setof.adapter.out.client.portone.config.PortOnePaths;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneApiResponse;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneBankHolderResponse;
import com.ryuqq.setof.adapter.out.client.portone.support.PortOneApiExecutor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * PortOne V2 API Client
 *
 * <p>포트원(PortOne) V2 API와 통신하는 클라이언트입니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>계좌 예금주 조회 (가상계좌 검증)
 *   <li>결제 정보 조회 (향후 확장)
 *   <li>결제 취소 (향후 확장)
 * </ul>
 *
 * <p><strong>특징:</strong>
 *
 * <ul>
 *   <li>PortOneApiExecutor를 통한 공통 로직 처리 (토큰, 재시도, 서킷브레이커)
 *   <li>실패 시 빈 응답 반환 (예외 미발생)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class PortOneClient {

    private static final Logger log = LoggerFactory.getLogger(PortOneClient.class);

    private final RestClient restClient;
    private final PortOneProperties properties;
    private final PortOneAuthClient authClient;
    private final PortOneApiExecutor apiExecutor;

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification =
                    "RestClient, PortOneProperties, PortOneAuthClient, and PortOneApiExecutor"
                            + " are Spring-managed beans, immutable after injection")
    public PortOneClient(
            RestClient portOneRestClient,
            PortOneProperties properties,
            PortOneAuthClient authClient,
            PortOneApiExecutor apiExecutor) {
        this.restClient = portOneRestClient;
        this.properties = properties;
        this.authClient = authClient;
        this.apiExecutor = apiExecutor;
    }

    /**
     * 인증 토큰 반환
     *
     * <p>유효한 Bearer 토큰을 반환합니다. PortOneAuthClient에 위임합니다.
     *
     * @return Bearer 토큰 문자열, 비활성화 시 빈 문자열
     */
    public String getAccessToken() {
        return authClient.getValidAccessToken();
    }

    /**
     * 계좌 예금주 조회
     *
     * <p>포트원 API를 통해 계좌의 예금주 정보를 조회합니다.
     *
     * <p><strong>특징:</strong>
     *
     * <ul>
     *   <li>실패 시 빈 응답 반환 (예외 미발생)
     *   <li>자동 재시도 및 서킷브레이커 적용
     * </ul>
     *
     * @param bankCode 은행 코드
     * @param accountNumber 계좌 번호
     * @return 예금주 정보, 실패 시 빈 응답
     */
    public PortOneBankHolderResponse fetchBankHolder(String bankCode, String accountNumber) {
        if (!properties.isEnabled()) {
            log.warn("PortOne is disabled. Returning empty response.");
            return PortOneBankHolderResponse.empty();
        }

        return apiExecutor.executeWithDefault(
                "fetchBankHolder",
                token -> callBankHolderApi(token, bankCode, accountNumber),
                PortOneBankHolderResponse::empty);
    }

    private PortOneBankHolderResponse callBankHolderApi(
            String token, String bankCode, String accountNumber) {
        PortOneApiResponse<PortOneBankHolderResponse> response =
                restClient
                        .get()
                        .uri(
                                uriBuilder ->
                                        uriBuilder
                                                .path(PortOnePaths.VBANK_HOLDER)
                                                .queryParam("bank_code", bankCode)
                                                .queryParam("bank_num", accountNumber)
                                                .build())
                        .header("Authorization", token)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});

        if (response == null || !response.isSuccess()) {
            log.warn("Failed to fetch bank holder. bankCode={}, response={}", bankCode, response);
            return PortOneBankHolderResponse.empty();
        }

        return response.response();
    }
}
