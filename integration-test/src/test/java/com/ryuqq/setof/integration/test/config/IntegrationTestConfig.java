package com.ryuqq.setof.integration.test.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.application.common.port.out.CachePort;
import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.application.common.port.out.StockCacheSyncPort;
import com.ryuqq.setof.application.common.port.out.StockCounterPort;
import com.ryuqq.setof.application.refundaccount.port.out.client.AccountVerificationPort;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Integration Test Configuration
 *
 * <p>외부 서비스 의존성을 Mock으로 대체하여 통합 테스트 환경을 구성합니다.
 *
 * <h3>Mock 대상</h3>
 *
 * <ul>
 *   <li>Redis 관련: DistributedLockPort, CachePort, RefreshTokenCachePort
 *   <li>외부 API: AccountVerificationPort
 *   <li>Security: TokenProviderPort, PasswordEncoderPort
 * </ul>
 *
 * @since 1.0.0
 */
@TestConfiguration
public class IntegrationTestConfig {

    // ============================================================
    // Redis Mock (분산락, 캐시)
    // ============================================================

    /**
     * 분산락 Mock - 멱등성 키 추적을 통한 중복 요청 감지
     *
     * <p>IdempotencyLockKey만 추적하여 동일한 키로 두 번째 tryLock 호출 시 false 반환
     *
     * <p>PaymentLockKey 등 다른 Lock 타입은 항상 true 반환 (정상 획득)
     */
    @Bean
    @Primary
    public DistributedLockPort distributedLockPort() {
        Set<String> acquiredIdempotencyKeys = Collections.newSetFromMap(new ConcurrentHashMap<>());

        DistributedLockPort mock = mock(DistributedLockPort.class);

        when(mock.tryLock(any(), anyLong(), anyLong(), any()))
                .thenAnswer(
                        (InvocationOnMock invocation) -> {
                            Object lockKey = invocation.getArgument(0);
                            // IdempotencyLockKey만 추적하여 중복 감지
                            if (lockKey.getClass().getSimpleName().equals("IdempotencyLockKey")) {
                                String keyString = lockKey.toString();
                                return acquiredIdempotencyKeys.add(keyString);
                            }
                            // 다른 Lock 타입 (PaymentLockKey 등)은 항상 성공
                            return true;
                        });

        when(mock.isHeldByCurrentThread(any())).thenReturn(true);
        when(mock.isLocked(any())).thenReturn(false);
        return mock;
    }

    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public CachePort<Object> cachePort() {
        return mock(CachePort.class);
    }

    @Bean
    @Primary
    public StockCacheSyncPort stockCacheSyncPort() {
        return mock(StockCacheSyncPort.class);
    }

    @Bean
    @Primary
    public StockCounterPort stockCounterPort() {
        StockCounterPort mock = mock(StockCounterPort.class);
        when(mock.hasStock(anyLong(), anyInt())).thenReturn(true);
        when(mock.hasStocks(any())).thenReturn(true);
        when(mock.exists(anyLong())).thenReturn(true);
        when(mock.getStock(anyLong())).thenReturn(100);
        when(mock.decrement(anyLong(), anyInt())).thenReturn(99);
        when(mock.increment(anyLong(), anyInt())).thenReturn(101);
        return mock;
    }

    @Bean
    @Primary
    public RefreshTokenCacheQueryPort refreshTokenCacheQueryPort() {
        return mock(RefreshTokenCacheQueryPort.class);
    }

    @Bean
    @Primary
    public RefreshTokenCacheCommandPort refreshTokenCacheCommandPort() {
        return mock(RefreshTokenCacheCommandPort.class);
    }

    // ============================================================
    // External API Mock
    // ============================================================

    @Bean
    @Primary
    public AccountVerificationPort accountVerificationPort() {
        return mock(AccountVerificationPort.class);
    }

    // ============================================================
    // Security - Real implementations used (not mocked)
    // ============================================================
    // TokenProviderPort: Uses JwtTokenProviderAdapter with test JWT properties
    // PasswordEncoderPort: Uses BCryptPasswordEncoderAdapter
}
