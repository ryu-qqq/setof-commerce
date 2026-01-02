package com.ryuqq.setof.domain.payment.vo;

import com.ryuqq.setof.domain.common.vo.LockKey;

/**
 * 결제 완료 분산락 키
 *
 * <p>결제 완료 시 프론트엔드 콜백과 PG 웹훅 간의 경쟁 조건 방지를 위한 분산락 키입니다.
 *
 * <p>키 형식: {@code lock:payment:complete:{paymentId}}
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * PaymentLockKey lockKey = PaymentLockKey.forComplete(paymentId);
 * boolean acquired = lockPort.tryLock(lockKey, 5, 30, TimeUnit.SECONDS);
 *
 * if (!acquired) {
 *     // 이미 다른 요청이 처리 중
 *     throw new PaymentCompletionInProgressException(paymentId);
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
public record PaymentLockKey(String prefix, String paymentId) implements LockKey {

    private static final String COMPLETE_PREFIX = "lock:payment:complete:";
    private static final String CANCEL_PREFIX = "lock:payment:cancel:";
    private static final String REFUND_PREFIX = "lock:payment:refund:";

    /**
     * PaymentLockKey 생성자
     *
     * @param prefix 락 키 prefix
     * @param paymentId 결제 ID (UUIDv7 String)
     * @throws IllegalArgumentException paymentId가 null이거나 빈 문자열인 경우
     */
    public PaymentLockKey {
        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("prefix must not be null or blank");
        }
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId must not be null or blank");
        }
    }

    /**
     * 결제 완료용 락 키 생성
     *
     * @param paymentId 결제 ID
     * @return PaymentLockKey 인스턴스
     */
    public static PaymentLockKey forComplete(String paymentId) {
        return new PaymentLockKey(COMPLETE_PREFIX, paymentId);
    }

    /**
     * 결제 취소용 락 키 생성
     *
     * @param paymentId 결제 ID
     * @return PaymentLockKey 인스턴스
     */
    public static PaymentLockKey forCancel(String paymentId) {
        return new PaymentLockKey(CANCEL_PREFIX, paymentId);
    }

    /**
     * 환불용 락 키 생성
     *
     * @param paymentId 결제 ID
     * @return PaymentLockKey 인스턴스
     */
    public static PaymentLockKey forRefund(String paymentId) {
        return new PaymentLockKey(REFUND_PREFIX, paymentId);
    }

    /**
     * 분산락 키 값 반환
     *
     * @return {@code lock:payment:{action}:{paymentId}} 형식의 키
     */
    @Override
    public String value() {
        return prefix + paymentId;
    }
}
