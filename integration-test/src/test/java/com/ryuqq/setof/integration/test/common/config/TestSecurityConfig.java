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
import com.ryuqq.setof.application.productgroup.port.out.query.LegacyProductGroupWebQueryPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 통합 테스트용 설정.
 *
 * <p>테스트 환경에서 필요한 Mock 빈들과 Security 설정을 제공합니다.
 */
@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    /**
     * 테스트용 SecurityFilterChain - 모든 요청 허용.
     *
     * <p>Web E2E 테스트에서 실제 SecurityConfig를 제외하고 사용합니다.
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }

    /**
     * 테스트용 TokenProviderPort Mock.
     *
     * @return TokenProviderPort Mock
     */
    @Bean
    @ConditionalOnMissingBean(TokenProviderPort.class)
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
    @ConditionalOnMissingBean(RefreshTokenCacheCommandPort.class)
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
    @ConditionalOnMissingBean(RefreshTokenCacheQueryPort.class)
    public RefreshTokenCacheQueryPort refreshTokenCacheQueryPort() {
        RefreshTokenCacheQueryPort mock = mock(RefreshTokenCacheQueryPort.class);
        when(mock.findMemberIdByToken(any(RefreshTokenCacheKey.class)))
                .thenReturn(Optional.of("1"));
        return mock;
    }

    /**
     * 테스트용 LegacyProductGroupWebQueryPort Mock.
     *
     * @return LegacyProductGroupWebQueryPort Mock
     */
    @Bean
    @ConditionalOnMissingBean(LegacyProductGroupWebQueryPort.class)
    public LegacyProductGroupWebQueryPort legacyProductGroupWebQueryPort() {
        return mock(LegacyProductGroupWebQueryPort.class);
    }
}
