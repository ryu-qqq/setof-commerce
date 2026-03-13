package com.ryuqq.setof.integration.test.common.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositeQueryPort;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositionQueryPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * 통합 테스트용 설정.
 *
 * <p>테스트 환경에서 필요한 Mock 빈들을 제공합니다. Security 설정은 기존 SecurityConfig를 사용합니다.
 */
@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    /**
     * 테스트용 TokenProviderPort Mock.
     *
     * @return TokenProviderPort Mock
     */
    @Bean
    public TokenProviderPort tokenProviderPort() {
        TokenProviderPort mock = mock(TokenProviderPort.class);
        when(mock.generateTokenPair(anyString()))
                .thenReturn(
                        new TokenPairResponse(
                                "test-access-token", 3600L, "test-refresh-token", 86400L));
        when(mock.extractMemberId(anyString())).thenReturn("1");
        when(mock.validateAccessToken(anyString())).thenReturn(true);
        when(mock.validateRefreshToken(anyString())).thenReturn(true);
        when(mock.extractMemberIdFromRefreshToken(anyString())).thenReturn("1");
        when(mock.isAccessTokenExpired(anyString())).thenReturn(false);
        return mock;
    }

    /**
     * 테스트용 RefreshTokenCacheCommandPort Mock.
     *
     * @return RefreshTokenCacheCommandPort Mock
     */
    @Bean
    public RefreshTokenCacheCommandPort refreshTokenCacheCommandPort() {
        RefreshTokenCacheCommandPort mock = mock(RefreshTokenCacheCommandPort.class);
        doNothing()
                .when(mock)
                .persist(any(RefreshTokenCacheKey.class), anyString(), any(Long.class));
        doNothing().when(mock).delete(any(RefreshTokenCacheKey.class));
        return mock;
    }

    /**
     * 테스트용 RefreshTokenCacheQueryPort Mock.
     *
     * @return RefreshTokenCacheQueryPort Mock
     */
    @Bean
    public RefreshTokenCacheQueryPort refreshTokenCacheQueryPort() {
        RefreshTokenCacheQueryPort mock = mock(RefreshTokenCacheQueryPort.class);
        when(mock.findMemberIdByToken(any(RefreshTokenCacheKey.class)))
                .thenReturn(Optional.of("1"));
        return mock;
    }

    /**
     * 테스트용 ProductGroupCompositionQueryPort Mock.
     *
     * <p>레거시 DB 전용 포트로, 테스트 환경에서는 Mock으로 대체합니다.
     *
     * @return ProductGroupCompositionQueryPort Mock
     */
    @Bean
    public ProductGroupCompositionQueryPort productGroupCompositionQueryPort() {
        return mock(ProductGroupCompositionQueryPort.class);
    }

    /**
     * 테스트용 ProductGroupCompositeQueryPort Mock.
     *
     * <p>Composite QueryDSL 포트로, 구현체가 WIP 상태이므로 Mock으로 대체합니다.
     *
     * @return ProductGroupCompositeQueryPort Mock
     */
    @Bean
    public ProductGroupCompositeQueryPort productGroupCompositeQueryPort() {
        return mock(ProductGroupCompositeQueryPort.class);
    }
}
