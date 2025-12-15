package com.ryuqq.setof.domain.productstock.vo;

import com.ryuqq.setof.domain.common.vo.LockKey;

/**
 * 재고 분산락 키
 *
 * <p>관리자 재고 설정 시 동시성 제어를 위한 분산락 키입니다.
 *
 * <p>키 형식: {@code lock:stock:product:{productId}}
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * StockLockKey lockKey = new StockLockKey(productId);
 * boolean acquired = lockPort.tryLock(lockKey, 10, 30, TimeUnit.SECONDS);
 *
 * if (!acquired) {
 *     throw new StockLockAcquisitionException(productId);
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
public record StockLockKey(Long productId) implements LockKey {

    private static final String PREFIX = "lock:stock:product:";

    /**
     * StockLockKey 생성자
     *
     * @param productId 상품 ID
     * @throws IllegalArgumentException productId가 null이거나 0 이하인 경우
     */
    public StockLockKey {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
    }

    /**
     * 분산락 키 값 반환
     *
     * @return {@code lock:stock:product:{productId}} 형식의 키
     */
    @Override
    public String value() {
        return PREFIX + productId;
    }
}
