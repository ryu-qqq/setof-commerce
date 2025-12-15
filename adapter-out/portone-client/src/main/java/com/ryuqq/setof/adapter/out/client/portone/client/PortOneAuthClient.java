package com.ryuqq.setof.adapter.out.client.portone.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOnePaths;
import com.ryuqq.setof.adapter.out.client.portone.config.PortOneProperties;
import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneAuthRequest;
import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneErrorResponse;
import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneRefreshRequest;
import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneTokenResponse;
import com.ryuqq.setof.adapter.out.client.portone.exception.PortOneAuthException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * PortOne V2 인증 클라이언트
 *
 * <p>PortOne V2 API 인증 토큰 발급 및 관리를 담당합니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>API Secret을 사용한 토큰 발급 (POST /login/api-secret)
 *   <li>Refresh Token을 사용한 토큰 갱신 (POST /token/refresh)
 *   <li>토큰 캐싱 및 자동 갱신
 * </ul>
 *
 * <p><strong>토큰 유효기간:</strong>
 *
 * <ul>
 *   <li>Access Token: 30분
 *   <li>Refresh Token: 24시간
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class PortOneAuthClient {

    private static final Logger log = LoggerFactory.getLogger(PortOneAuthClient.class);

    private static final String BEARER_PREFIX = "Bearer ";

    /** Access Token 유효기간 (30분) */
    private static final long ACCESS_TOKEN_TTL_SECONDS = 30 * 60;

    /** Refresh Token 유효기간 (24시간) */
    private static final long REFRESH_TOKEN_TTL_SECONDS = 24 * 60 * 60;

    /** 만료 버퍼 시간 (1분 전에 갱신) */
    private static final long EXPIRY_BUFFER_SECONDS = 60;

    private final RestClient restClient;
    private final PortOneProperties properties;
    private final ObjectMapper objectMapper;
    private final AtomicReference<CachedToken> cachedToken = new AtomicReference<>();

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification =
                    "RestClient, PortOneProperties, and ObjectMapper are Spring-managed beans,"
                            + " immutable after injection")
    public PortOneAuthClient(
            RestClient portOneRestClient, PortOneProperties properties, ObjectMapper objectMapper) {
        this.restClient = portOneRestClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * 유효한 Access Token 반환
     *
     * <p>캐시된 토큰이 유효하면 반환하고, 만료되었거나 없으면 새로 발급/갱신합니다.
     *
     * @return Bearer 토큰 문자열, 비활성화 시 빈 문자열
     */
    public String getValidAccessToken() {
        if (!properties.isEnabled()) {
            log.debug("PortOne is disabled. Returning empty token.");
            return "";
        }

        CachedToken cached = cachedToken.get();

        if (cached == null) {
            return issueNewToken();
        }

        if (!cached.isAccessTokenExpired()) {
            return cached.bearerToken();
        }

        if (!cached.isRefreshTokenExpired()) {
            return refreshTokenAndGet(cached.refreshToken());
        }

        return issueNewToken();
    }

    /**
     * 토큰 발급
     *
     * <p>API Secret을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.
     *
     * @return 토큰 응답
     * @throws PortOneAuthException 토큰 발급 실패 시
     */
    public PortOneTokenResponse issueToken() {
        if (!properties.isEnabled()) {
            throw new PortOneAuthException("PortOne is disabled");
        }

        try {
            PortOneAuthRequest request = PortOneAuthRequest.of(properties.getApiSecret());

            PortOneTokenResponse response =
                    restClient
                            .post()
                            .uri(PortOnePaths.AUTH_LOGIN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(request)
                            .retrieve()
                            .body(PortOneTokenResponse.class);

            if (response == null || !response.isValid()) {
                throw new PortOneAuthException("Failed to issue token: invalid response");
            }

            cacheToken(response);
            log.info("PortOne token issued successfully");

            return response;

        } catch (HttpClientErrorException e) {
            throw handleClientError(e, "Failed to issue token");
        } catch (PortOneAuthException e) {
            throw e;
        } catch (RestClientException e) {
            throw new PortOneAuthException("Failed to issue token", e);
        }
    }

    /**
     * 토큰 갱신
     *
     * <p>Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 토큰 응답
     * @throws PortOneAuthException 토큰 갱신 실패 시
     */
    public PortOneTokenResponse refreshToken(String refreshToken) {
        if (!properties.isEnabled()) {
            throw new PortOneAuthException("PortOne is disabled");
        }

        try {
            PortOneRefreshRequest request = PortOneRefreshRequest.of(refreshToken);

            PortOneTokenResponse response =
                    restClient
                            .post()
                            .uri(PortOnePaths.AUTH_REFRESH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(request)
                            .retrieve()
                            .body(PortOneTokenResponse.class);

            if (response == null || !response.isValid()) {
                throw new PortOneAuthException("Failed to refresh token: invalid response");
            }

            cacheToken(response);
            log.info("PortOne token refreshed successfully");

            return response;

        } catch (HttpClientErrorException e) {
            log.warn("Token refresh failed: {}", e.getMessage());
            throw handleClientError(e, "Failed to refresh token");
        } catch (PortOneAuthException e) {
            log.warn("Token refresh failed, will issue new token: {}", e.getMessage());
            throw e;
        } catch (RestClientException e) {
            throw new PortOneAuthException("Failed to refresh token", e);
        }
    }

    /**
     * 현재 캐시된 토큰 정보 조회 (테스트용)
     *
     * @return 캐시된 토큰, 없으면 null
     */
    CachedToken getCachedToken() {
        return cachedToken.get();
    }

    /**
     * 캐시 초기화
     *
     * <p>캐시된 토큰을 삭제합니다. 토큰 갱신 실패 시 호출됩니다.
     */
    public void clearCache() {
        cachedToken.set(null);
    }

    private String issueNewToken() {
        PortOneTokenResponse response = issueToken();
        return BEARER_PREFIX + response.accessToken();
    }

    private String refreshTokenAndGet(String refreshToken) {
        try {
            PortOneTokenResponse response = refreshToken(refreshToken);
            return BEARER_PREFIX + response.accessToken();
        } catch (PortOneAuthException e) {
            log.warn("Token refresh failed, issuing new token");
            return issueNewToken();
        }
    }

    private void cacheToken(PortOneTokenResponse response) {
        long now = Instant.now().getEpochSecond();
        CachedToken cached =
                new CachedToken(
                        response.accessToken(),
                        response.refreshToken(),
                        now + ACCESS_TOKEN_TTL_SECONDS,
                        now + REFRESH_TOKEN_TTL_SECONDS);
        cachedToken.set(cached);
    }

    private PortOneAuthException handleClientError(HttpClientErrorException e, String message) {
        try {
            String responseBody = e.getResponseBodyAsString();
            PortOneErrorResponse errorResponse =
                    objectMapper.readValue(responseBody, PortOneErrorResponse.class);
            return new PortOneAuthException(errorResponse);
        } catch (Exception parseException) {
            log.warn("Failed to parse error response: {}", parseException.getMessage());
            return new PortOneAuthException(message + ": " + e.getStatusCode(), e);
        }
    }

    /** 캐시된 토큰 정보 */
    record CachedToken(
            String accessToken,
            String refreshToken,
            long accessTokenExpiresAt,
            long refreshTokenExpiresAt) {

        String bearerToken() {
            return BEARER_PREFIX + accessToken;
        }

        boolean isAccessTokenExpired() {
            long now = Instant.now().getEpochSecond();
            return now >= (accessTokenExpiresAt - EXPIRY_BUFFER_SECONDS);
        }

        boolean isRefreshTokenExpired() {
            long now = Instant.now().getEpochSecond();
            return now >= (refreshTokenExpiresAt - EXPIRY_BUFFER_SECONDS);
        }
    }
}
