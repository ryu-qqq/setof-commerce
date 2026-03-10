package com.ryuqq.setof.domain.stock.vo;

import com.ryuqq.setof.domain.common.vo.LockKey;

/**
 * 재고 분산락 키.
 *
 * <p>productId 기준으로 락을 잡습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record StockLockKey(long productId) implements LockKey {

    private static final String PREFIX = "lock:stock:product:";

    public StockLockKey {
        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
    }

    @Override
    public String value() {
        return PREFIX + productId;
    }
}
