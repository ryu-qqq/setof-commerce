package com.ryuqq.setof.domain.checkout.vo;

import com.ryuqq.setof.domain.common.vo.LockKey;

/**
 * 멱등성 분산락 키
 *
 * <p>체크아웃 생성 시 중복 요청 방지를 위한 분산락 키입니다. 프론트엔드에서 생성한 멱등키를 사용합니다.
 *
 * <p>키 형식: {@code lock:checkout:idempotency:{idempotencyKey}}
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * IdempotencyLockKey lockKey = new IdempotencyLockKey(idempotencyKey);
 * boolean acquired = lockPort.tryLock(lockKey, 5, 30, TimeUnit.SECONDS);
 *
 * if (!acquired) {
 *     throw new DuplicateCheckoutRequestException(idempotencyKey);
 * }
 * }</pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>LockKey 인터페이스 구현 필수
 *   <li>record로 구현 (불변성)
 *   <li>compact constructor에서 유효성 검증
 *   <li>Lombok 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public record IdempotencyLockKey(String idempotencyKey) implements LockKey {

    private static final String PREFIX = "lock:checkout:idempotency:";

    /**
     * IdempotencyLockKey 생성자
     *
     * @param idempotencyKey 멱등키 (프론트엔드에서 생성)
     * @throws IllegalArgumentException idempotencyKey가 null이거나 빈 문자열인 경우
     */
    public IdempotencyLockKey {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("idempotencyKey must not be null or blank");
        }
    }

    /**
     * 분산락 키 값 반환
     *
     * @return {@code lock:checkout:idempotency:{idempotencyKey}} 형식의 키
     */
    @Override
    public String value() {
        return PREFIX + idempotencyKey;
    }
}
