package com.ryuqq.setof.integration.test.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.adapter.in.rest.admin.auth.component.SellerSecurityChecker;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.application.common.port.out.CachePort;
import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.application.common.port.out.StockCacheSyncPort;
import com.ryuqq.setof.application.common.port.out.StockCounterPort;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Admin Integration Test Configuration
 *
 * <p>Admin API 통합 테스트 환경 구성을 위한 설정 클래스입니다.
 *
 * <h3>Mock 대상</h3>
 *
 * <ul>
 *   <li>Redis 관련: DistributedLockPort, CachePort, RefreshTokenCachePort
 *   <li>Security: ResourceAccessChecker (권한 검증 우회)
 * </ul>
 *
 * @since 1.0.0
 */
@TestConfiguration
public class AdminIntegrationTestConfig {

    // ============================================================
    // Security Mock (Admin 권한 검증 우회)
    // ============================================================

    /**
     * SellerSecurityChecker Mock - 모든 sellerId 접근 허용
     *
     * <p>통합 테스트에서 권한 검증을 우회하기 위해 항상 true 반환
     */
    @Bean
    @Primary
    public SellerSecurityChecker sellerSecurityChecker() {
        SellerSecurityChecker mock = mock(SellerSecurityChecker.class);
        when(mock.canAccess(anyLong())).thenReturn(true);
        when(mock.canModify(anyLong())).thenReturn(true);
        when(mock.canDelete(anyLong())).thenReturn(true);
        return mock;
    }

    // ============================================================
    // Redis Mock (분산락, 캐시)
    // ============================================================

    /** 분산락 Mock - 멱등성 키 추적을 통한 중복 요청 감지 */
    @Bean
    @Primary
    public DistributedLockPort distributedLockPort() {
        Set<String> acquiredIdempotencyKeys = Collections.newSetFromMap(new ConcurrentHashMap<>());

        DistributedLockPort mock = mock(DistributedLockPort.class);

        when(mock.tryLock(any(), anyLong(), anyLong(), any()))
                .thenAnswer(
                        (InvocationOnMock invocation) -> {
                            Object lockKey = invocation.getArgument(0);
                            if (lockKey.getClass().getSimpleName().equals("IdempotencyLockKey")) {
                                String keyString = lockKey.toString();
                                return acquiredIdempotencyKeys.add(keyString);
                            }
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
}
