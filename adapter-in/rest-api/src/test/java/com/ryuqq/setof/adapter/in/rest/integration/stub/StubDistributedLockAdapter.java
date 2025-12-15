package com.ryuqq.setof.adapter.in.rest.integration.stub;

import com.ryuqq.setof.application.common.port.out.DistributedLockPort;
import com.ryuqq.setof.domain.common.vo.LockKey;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 분산락 Stub 구현체 (테스트용)
 *
 * <p>REST API 통합 테스트에서 Redis 없이 분산락 기능을 테스트하기 위한 In-memory Stub입니다.
 *
 * <p><strong>특징:</strong>
 *
 * <ul>
 *   <li>ConcurrentHashMap 기반 In-memory 락 관리
 *   <li>waitTime, leaseTime은 무시 (테스트 환경)
 *   <li>스레드 ID 기반 락 소유권 추적
 *   <li>@Primary로 실제 Redis 어댑터보다 우선
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
@Component
@Primary
public class StubDistributedLockAdapter implements DistributedLockPort {

    private final ConcurrentHashMap<String, Long> locks = new ConcurrentHashMap<>();

    @Override
    public boolean tryLock(LockKey key, long waitTime, long leaseTime, TimeUnit unit) {
        Long currentThreadId = Thread.currentThread().getId();
        Long existingThreadId = locks.putIfAbsent(key.value(), currentThreadId);

        if (existingThreadId == null) {
            return true;
        }

        return existingThreadId.equals(currentThreadId);
    }

    @Override
    public void unlock(LockKey key) {
        Long currentThreadId = Thread.currentThread().getId();
        locks.remove(key.value(), currentThreadId);
    }

    @Override
    public boolean isHeldByCurrentThread(LockKey key) {
        Long currentThreadId = Thread.currentThread().getId();
        return currentThreadId.equals(locks.get(key.value()));
    }

    @Override
    public boolean isLocked(LockKey key) {
        return locks.containsKey(key.value());
    }

    /** 모든 락 초기화 (테스트 간 격리) */
    public void clear() {
        locks.clear();
    }
}
