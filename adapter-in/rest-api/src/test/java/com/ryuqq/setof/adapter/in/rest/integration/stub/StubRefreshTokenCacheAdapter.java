package com.ryuqq.setof.adapter.in.rest.integration.stub;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Refresh Token 캐시 Stub 구현체 (테스트용)
 *
 * <p>REST API 통합 테스트에서 Redis 없이 캐시 기능을 테스트하기 위한 In-memory Stub입니다.
 *
 * <p><strong>특징:</strong>
 *
 * <ul>
 *   <li>ConcurrentHashMap 기반 In-memory 저장
 *   <li>만료 시간은 무시 (테스트 환경)
 *   <li>@Primary로 실제 Redis 어댑터보다 우선
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
@Component
@Primary
public class StubRefreshTokenCacheAdapter
        implements RefreshTokenCacheCommandPort, RefreshTokenCacheQueryPort {

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public void persist(RefreshTokenCacheKey cacheKey, String memberId, long expiresInSeconds) {
        cache.put(cacheKey.value(), memberId);
    }

    @Override
    public void delete(RefreshTokenCacheKey cacheKey) {
        cache.remove(cacheKey.value());
    }

    @Override
    public Optional<String> findMemberIdByToken(RefreshTokenCacheKey cacheKey) {
        return Optional.ofNullable(cache.get(cacheKey.value()));
    }

    /** 캐시 초기화 (테스트 간 격리) */
    public void clear() {
        cache.clear();
    }
}
