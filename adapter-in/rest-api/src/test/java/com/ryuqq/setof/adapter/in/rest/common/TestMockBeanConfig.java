package com.ryuqq.setof.adapter.in.rest.common;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 통합 테스트용 Mock Bean 설정
 *
 * <p>외부 어댑터(Security, External Client 등)를 Mock으로 대체합니다.
 *
 * <p><strong>테스트 인증 지원:</strong>
 *
 * <ul>
 *   <li>{@code TEST_TOKEN_PREFIX + memberId} 형식의 토큰을 유효한 토큰으로 인식
 *   <li>예: "TEST_TOKEN_12345" → memberId = "12345"
 *   <li>ApiIntegrationTestSupport의 인증 헬퍼 메서드와 연동
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 * @see ApiIntegrationTestSupport#getAuthenticated
 */
@TestConfiguration
public class TestMockBeanConfig {

    /** 테스트 토큰 접두사 */
    public static final String TEST_TOKEN_PREFIX = "TEST_TOKEN_";

    @Bean
    public TokenProviderPort tokenProviderPort() {
        TokenProviderPort mockPort = mock(TokenProviderPort.class);

        // 테스트 토큰 패턴: "TEST_TOKEN_{memberId}"
        when(mockPort.validateAccessToken(anyString()))
                .thenAnswer(
                        invocation -> {
                            String token = invocation.getArgument(0);
                            return token != null && token.startsWith(TEST_TOKEN_PREFIX);
                        });

        when(mockPort.extractMemberId(anyString()))
                .thenAnswer(
                        invocation -> {
                            String token = invocation.getArgument(0);
                            if (token != null && token.startsWith(TEST_TOKEN_PREFIX)) {
                                return token.substring(TEST_TOKEN_PREFIX.length());
                            }
                            return null;
                        });

        when(mockPort.isAccessTokenExpired(anyString())).thenReturn(false);
        when(mockPort.validateRefreshToken(anyString())).thenReturn(false);

        // 토큰 생성 - 회원가입 시 사용
        when(mockPort.generateTokenPair(anyString()))
                .thenAnswer(
                        invocation -> {
                            String memberId = invocation.getArgument(0);
                            String accessToken = TEST_TOKEN_PREFIX + memberId;
                            String refreshToken = "REFRESH_" + UUID.randomUUID();
                            return TokenPairResponse.of(accessToken, 3600L, refreshToken, 604800L);
                        });

        return mockPort;
    }

    @Bean
    public AccountVerificationPort accountVerificationPort() {
        AccountVerificationPort mockPort = mock(AccountVerificationPort.class);

        // 모든 계좌 검증 요청에 대해 true 반환 (테스트 환경)
        when(mockPort.verifyAccount(anyString(), anyString(), anyString())).thenReturn(true);

        return mockPort;
    }

    @Bean
    public ClockHolder clockHolder() {
        return () -> Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
