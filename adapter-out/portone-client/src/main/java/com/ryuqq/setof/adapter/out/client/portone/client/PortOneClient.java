package com.ryuqq.setof.adapter.out.client.portone.client;

import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneApiResponse;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneBankHolderResponse;
import com.ryuqq.setof.adapter.out.client.portone.dto.PortOneTokenResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * PortOne API Client
 *
 * <p>포트원(PortOne/Iamport) API와 통신하는 클라이언트입니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>인증 토큰 발급 및 캐싱
 *   <li>계좌 예금주 조회 (가상계좌 검증)
 *   <li>결제 정보 조회 (향후 확장)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PortOneClient {

    private static final Logger log = LoggerFactory.getLogger(PortOneClient.class);
    private static final String AUTH_PATH = "/users/getToken";
    private static final String VBANK_HOLDER_PATH = "/vbanks/holder";
    private static final String BEARER_PREFIX = "Bearer ";

    private final RestClient restClient;
    private final PortOneProperties properties;
    private final AtomicReference<CachedToken> cachedToken = new AtomicReference<>();

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification =
                    "RestClient and PortOneProperties are Spring-managed beans, immutable after"
                            + " injection")
    public PortOneClient(RestClient portOneRestClient, PortOneProperties properties) {
        this.restClient = portOneRestClient;
        this.properties = properties;
    }

    /**
     * 인증 토큰 발급
     *
     * <p>토큰이 캐시되어 있고 유효하면 캐시된 토큰을 반환합니다.
     *
     * @return Bearer 토큰 문자열, 비활성화 시 빈 문자열
     */
    public String getAccessToken() {
        if (!properties.isEnabled()) {
            log.debug("PortOne is disabled. Skipping token refresh.");
            return "";
        }

        CachedToken cached = cachedToken.get();
        if (cached != null && !cached.isExpired()) {
            return cached.token();
        }

        return refreshToken();
    }

    /**
     * 계좌 예금주 조회
     *
     * @param bankCode 은행 코드
     * @param accountNumber 계좌 번호
     * @return 예금주 정보
     */
    public PortOneBankHolderResponse fetchBankHolder(String bankCode, String accountNumber) {
        if (!properties.isEnabled()) {
            log.warn("PortOne is disabled. Returning empty response.");
            return PortOneBankHolderResponse.empty();
        }

        try {
            String token = getAccessToken();

            PortOneApiResponse<PortOneBankHolderResponse> response =
                    restClient
                            .get()
                            .uri(
                                    uriBuilder ->
                                            uriBuilder
                                                    .path(VBANK_HOLDER_PATH)
                                                    .queryParam("bank_code", bankCode)
                                                    .queryParam("bank_num", accountNumber)
                                                    .build())
                            .header("Authorization", token)
                            .retrieve()
                            .body(new ParameterizedTypeReference<>() {});

            if (response == null || !response.isSuccess()) {
                log.warn(
                        "Failed to fetch bank holder. bankCode={}, response={}",
                        bankCode,
                        response);
                return PortOneBankHolderResponse.empty();
            }

            return response.response();

        } catch (RestClientException e) {
            log.error("PortOne API call failed. bankCode={}", bankCode, e);
            return PortOneBankHolderResponse.empty();
        }
    }

    private String refreshToken() {
        try {
            Map<String, String> requestBody =
                    Map.of(
                            "imp_key", properties.getApiKey(),
                            "imp_secret", properties.getApiSecret());

            PortOneApiResponse<PortOneTokenResponse> response =
                    restClient
                            .post()
                            .uri(AUTH_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(requestBody)
                            .retrieve()
                            .body(new ParameterizedTypeReference<>() {});

            if (response == null || !response.isSuccess() || response.response() == null) {
                throw new PortOneClientException(
                        "Failed to get PortOne token: "
                                + (response != null ? response.message() : "null response"));
            }

            PortOneTokenResponse tokenResponse = response.response();
            String token = BEARER_PREFIX + tokenResponse.accessToken();

            cachedToken.set(new CachedToken(token, tokenResponse.expiredAt()));

            return token;

        } catch (RestClientException e) {
            throw new PortOneClientException("Failed to get PortOne token", e);
        }
    }

    /** Cached Token Record */
    private record CachedToken(String token, long expiredAt) {

        private static final long BUFFER_SECONDS = 60;

        boolean isExpired() {
            long nowSeconds = System.currentTimeMillis() / 1000;
            return nowSeconds >= (expiredAt - BUFFER_SECONDS);
        }
    }

    /** PortOne Client Exception */
    public static class PortOneClientException extends RuntimeException {

        public PortOneClientException(String message) {
            super(message);
        }

        public PortOneClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
