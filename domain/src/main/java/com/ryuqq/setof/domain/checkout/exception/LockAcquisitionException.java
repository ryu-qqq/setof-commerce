package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * LockAcquisitionException - 락 획득 실패 예외
 *
 * <p>분산락 획득에 실패한 경우 발생합니다.
 */
public class LockAcquisitionException extends DomainException {

    /**
     * 락 획득 실패 예외 생성
     *
     * @param lockKey 락 키
     * @return LockAcquisitionException 인스턴스
     */
    public static LockAcquisitionException forLockKey(String lockKey) {
        return new LockAcquisitionException(lockKey);
    }

    private LockAcquisitionException(String lockKey) {
        super(
                CheckoutErrorCode.LOCK_ACQUISITION_FAILED,
                String.format("락 획득 실패 - lockKey: %s", lockKey),
                Map.of("lockKey", lockKey));
    }
}
