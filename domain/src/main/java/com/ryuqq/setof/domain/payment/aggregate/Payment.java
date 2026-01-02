package com.ryuqq.setof.domain.payment.aggregate;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.payment.exception.PaymentStatusException;
import com.ryuqq.setof.domain.payment.exception.RefundAmountException;
import com.ryuqq.setof.domain.payment.vo.Currency;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import com.ryuqq.setof.domain.payment.vo.PgProvider;
import java.time.Instant;

/**
 * Payment Aggregate Root
 *
 * <p>결제를 나타내는 Aggregate Root입니다. Checkout 완료 후 생성되며, 부분 환불과 전액 환불을 지원합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드, 상태 변경 시 새 인스턴스 반환
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Tell, Don't Ask - 상태 변경은 도메인 메서드를 통해서만
 * </ul>
 *
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → APPROVED → PARTIAL_REFUNDED → FULLY_REFUNDED
 *    │          │
 *    └→ FAILED  └→ CANCELLED
 * </pre>
 */
public class Payment {

    private final PaymentId id;
    private final CheckoutId checkoutId;
    private final PgProvider pgProvider;
    private final String pgTransactionId;
    private final PaymentMethod method;
    private final PaymentStatus status;
    private final PaymentMoney requestedAmount;
    private final PaymentMoney approvedAmount;
    private final PaymentMoney refundedAmount;
    private final Currency currency;
    private final Instant approvedAt;
    private final Instant cancelledAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Payment(
            PaymentId id,
            CheckoutId checkoutId,
            PgProvider pgProvider,
            String pgTransactionId,
            PaymentMethod method,
            PaymentStatus status,
            PaymentMoney requestedAmount,
            PaymentMoney approvedAmount,
            PaymentMoney refundedAmount,
            Currency currency,
            Instant approvedAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.checkoutId = checkoutId;
        this.pgProvider = pgProvider;
        this.pgTransactionId = pgTransactionId;
        this.method = method;
        this.status = status;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.refundedAmount = refundedAmount;
        this.currency = currency;
        this.approvedAt = approvedAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 결제 요청 생성 (PG 요청 전)
     *
     * @param checkoutId 결제 세션 ID
     * @param pgProvider PG사
     * @param method 결제 수단
     * @param requestedAmount 요청 금액
     * @param now 현재 시각
     * @return Payment 인스턴스
     */
    public static Payment forNewRequest(
            CheckoutId checkoutId,
            PgProvider pgProvider,
            PaymentMethod method,
            PaymentMoney requestedAmount,
            Instant now) {

        validateCheckoutId(checkoutId);
        validatePgProvider(pgProvider);
        validateMethod(method);
        validateAmount(requestedAmount);

        return new Payment(
                PaymentId.forNew(),
                checkoutId,
                pgProvider,
                null, // PG 거래 ID는 승인 후 설정
                method,
                PaymentStatus.defaultStatus(),
                requestedAmount,
                PaymentMoney.zero(),
                PaymentMoney.zero(),
                Currency.defaultCurrency(),
                null,
                null,
                now,
                now);
    }

    /** 영속화된 데이터로부터 복원 (Persistence Layer용) */
    public static Payment restore(
            PaymentId id,
            CheckoutId checkoutId,
            PgProvider pgProvider,
            String pgTransactionId,
            PaymentMethod method,
            PaymentStatus status,
            PaymentMoney requestedAmount,
            PaymentMoney approvedAmount,
            PaymentMoney refundedAmount,
            Currency currency,
            Instant approvedAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        return new Payment(
                id,
                checkoutId,
                pgProvider,
                pgTransactionId,
                method,
                status,
                requestedAmount,
                approvedAmount,
                refundedAmount,
                currency,
                approvedAt,
                cancelledAt,
                createdAt,
                updatedAt);
    }

    // ===== 상태 전이 메서드 =====

    /**
     * 결제 승인
     *
     * @param pgTransactionId PG 거래 ID
     * @param approvedAmount 승인 금액
     * @param now 현재 시각
     * @return APPROVED 상태의 새 Payment 인스턴스
     * @throws PaymentStatusException 상태 전이 불가 시
     */
    public Payment approve(String pgTransactionId, PaymentMoney approvedAmount, Instant now) {
        if (!status.canApprove()) {
            throw PaymentStatusException.notApprovable(id, status);
        }
        validatePgTransactionId(pgTransactionId);
        validateAmount(approvedAmount);

        return new Payment(
                id,
                checkoutId,
                pgProvider,
                pgTransactionId,
                method,
                PaymentStatus.APPROVED,
                requestedAmount,
                approvedAmount,
                PaymentMoney.zero(),
                currency,
                now,
                null,
                createdAt,
                now);
    }

    /**
     * 결제 실패 처리
     *
     * @param now 현재 시각
     * @return FAILED 상태의 새 Payment 인스턴스
     */
    public Payment fail(Instant now) {
        return new Payment(
                id,
                checkoutId,
                pgProvider,
                pgTransactionId,
                method,
                PaymentStatus.FAILED,
                requestedAmount,
                PaymentMoney.zero(),
                PaymentMoney.zero(),
                currency,
                null,
                null,
                createdAt,
                now);
    }

    /**
     * 환불 처리 (부분/전액)
     *
     * @param refundAmount 환불 금액
     * @param now 현재 시각
     * @return 환불 후 상태의 새 Payment 인스턴스
     * @throws PaymentStatusException 환불 불가 상태인 경우
     * @throws RefundAmountException 환불 금액 초과 시
     */
    public Payment refund(PaymentMoney refundAmount, Instant now) {
        if (!status.canRefund()) {
            throw PaymentStatusException.notRefundable(id, status);
        }

        PaymentMoney available = refundableAmount();
        if (!available.isGreaterThanOrEqual(refundAmount)) {
            throw RefundAmountException.exceedsAvailable(id, refundAmount, available);
        }

        PaymentMoney newRefundedAmount = refundedAmount.add(refundAmount);
        PaymentStatus newStatus = determineStatusAfterRefund(newRefundedAmount);

        return new Payment(
                id,
                checkoutId,
                pgProvider,
                pgTransactionId,
                method,
                newStatus,
                requestedAmount,
                approvedAmount,
                newRefundedAmount,
                currency,
                approvedAt,
                cancelledAt,
                createdAt,
                now);
    }

    /**
     * 결제 취소
     *
     * @param now 현재 시각
     * @return CANCELLED 상태의 새 Payment 인스턴스
     * @throws PaymentStatusException 취소 불가 상태인 경우
     */
    public Payment cancel(Instant now) {
        if (!status.canCancel()) {
            throw PaymentStatusException.notCancellable(id, status);
        }

        return new Payment(
                id,
                checkoutId,
                pgProvider,
                pgTransactionId,
                method,
                PaymentStatus.CANCELLED,
                requestedAmount,
                approvedAmount,
                refundedAmount,
                currency,
                approvedAt,
                now,
                createdAt,
                now);
    }

    // ===== 도메인 로직 =====

    /**
     * 환불 가능 금액 계산
     *
     * @return 승인 금액 - 환불된 금액
     */
    public PaymentMoney refundableAmount() {
        if (approvedAmount.isZero()) {
            return PaymentMoney.zero();
        }
        return approvedAmount.subtract(refundedAmount);
    }

    /**
     * 환불 가능 여부
     *
     * @return 환불 가능하면 true
     */
    public boolean canBeRefunded() {
        return status.canRefund() && refundableAmount().isPositive();
    }

    /**
     * 전액 환불 여부
     *
     * @return 전액 환불되었으면 true
     */
    public boolean isFullyRefunded() {
        return status == PaymentStatus.FULLY_REFUNDED;
    }

    /**
     * 성공 상태 여부
     *
     * @return 승인 또는 부분환불 상태이면 true
     */
    public boolean isSuccess() {
        return status.isSuccess();
    }

    // ===== Getter 메서드 =====

    public PaymentId id() {
        return id;
    }

    public CheckoutId checkoutId() {
        return checkoutId;
    }

    public PgProvider pgProvider() {
        return pgProvider;
    }

    public String pgTransactionId() {
        return pgTransactionId;
    }

    public PaymentMethod method() {
        return method;
    }

    public PaymentStatus status() {
        return status;
    }

    public PaymentMoney requestedAmount() {
        return requestedAmount;
    }

    public PaymentMoney approvedAmount() {
        return approvedAmount;
    }

    public PaymentMoney refundedAmount() {
        return refundedAmount;
    }

    public Currency currency() {
        return currency;
    }

    public Instant approvedAt() {
        return approvedAt;
    }

    public Instant cancelledAt() {
        return cancelledAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    // ===== Private Helper =====

    private PaymentStatus determineStatusAfterRefund(PaymentMoney newRefundedAmount) {
        if (newRefundedAmount.isEqualTo(approvedAmount)) {
            return PaymentStatus.FULLY_REFUNDED;
        }
        return PaymentStatus.PARTIAL_REFUNDED;
    }

    private static void validateCheckoutId(CheckoutId checkoutId) {
        if (checkoutId == null) {
            throw new IllegalArgumentException("CheckoutId는 필수입니다");
        }
    }

    private static void validatePgProvider(PgProvider pgProvider) {
        if (pgProvider == null) {
            throw new IllegalArgumentException("PG사는 필수입니다");
        }
    }

    private static void validateMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("결제 수단은 필수입니다");
        }
    }

    private static void validateAmount(PaymentMoney amount) {
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("결제 금액은 0보다 커야 합니다");
        }
    }

    private static void validatePgTransactionId(String pgTransactionId) {
        if (pgTransactionId == null || pgTransactionId.isBlank()) {
            throw new IllegalArgumentException("PG 거래 ID는 필수입니다");
        }
    }
}
