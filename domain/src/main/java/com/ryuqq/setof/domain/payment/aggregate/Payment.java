package com.ryuqq.setof.domain.payment.aggregate;

import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.payment.exception.InvalidPaymentStatusException;
import com.ryuqq.setof.domain.payment.exception.PaymentErrorCode;
import com.ryuqq.setof.domain.payment.exception.PaymentException;
import com.ryuqq.setof.domain.payment.id.PaymentId;
import com.ryuqq.setof.domain.payment.vo.BuyerInfo;
import com.ryuqq.setof.domain.payment.vo.CardPaymentInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentMethodType;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import com.ryuqq.setof.domain.payment.vo.PgTransactionInfo;
import com.ryuqq.setof.domain.payment.vo.UsedMileage;
import java.time.Instant;

/** 결제 Aggregate Root. */
public class Payment {

    private final PaymentId id;
    private final OrderId orderId;
    private final MemberId memberId;
    private final LegacyUserId legacyUserId;
    private final Money paymentAmount;
    private PaymentStatus paymentStatus;
    private final PaymentMethodType methodType;
    private PgTransactionInfo pgInfo;
    private final BuyerInfo buyerInfo;
    private CardPaymentInfo cardInfo;
    private final UsedMileage usedMileage;
    private Instant paidAt;
    private Instant cancelledAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private Payment(
            PaymentId id,
            OrderId orderId,
            MemberId memberId,
            LegacyUserId legacyUserId,
            Money paymentAmount,
            PaymentStatus paymentStatus,
            PaymentMethodType methodType,
            PgTransactionInfo pgInfo,
            BuyerInfo buyerInfo,
            CardPaymentInfo cardInfo,
            UsedMileage usedMileage,
            Instant paidAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.memberId = memberId;
        this.legacyUserId = legacyUserId;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.methodType = methodType;
        this.pgInfo = pgInfo;
        this.buyerInfo = buyerInfo;
        this.cardInfo = cardInfo;
        this.usedMileage = usedMileage;
        this.paidAt = paidAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 결제 생성. PROCESSING 상태로 시작합니다.
     *
     * @param orderId 주문 ID
     * @param memberId 회원 ID (nullable)
     * @param legacyUserId 레거시 사용자 ID
     * @param paymentAmount 결제 금액
     * @param methodType 결제 수단
     * @param buyerInfo 구매자 정보
     * @param usedMileage 사용 마일리지
     * @param now 현재 시간
     * @return PROCESSING 상태의 새 결제
     */
    public static Payment forNew(
            OrderId orderId,
            MemberId memberId,
            LegacyUserId legacyUserId,
            Money paymentAmount,
            PaymentMethodType methodType,
            BuyerInfo buyerInfo,
            UsedMileage usedMileage,
            Instant now) {
        return new Payment(
                PaymentId.forNew(),
                orderId,
                memberId,
                legacyUserId,
                paymentAmount,
                PaymentStatus.PROCESSING,
                methodType,
                null,
                buyerInfo,
                null,
                usedMileage,
                null,
                null,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 결제 ID
     * @param orderId 주문 ID
     * @param memberId 회원 ID (nullable)
     * @param legacyUserId 레거시 사용자 ID
     * @param paymentAmount 결제 금액
     * @param paymentStatus 결제 상태
     * @param methodType 결제 수단
     * @param pgInfo PG 트랜잭션 정보 (nullable)
     * @param buyerInfo 구매자 정보
     * @param cardInfo 카드 결제 정보 (nullable)
     * @param usedMileage 사용 마일리지
     * @param paidAt 결제 완료 시각 (nullable)
     * @param cancelledAt 취소 시각 (nullable)
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 결제
     */
    public static Payment reconstitute(
            PaymentId id,
            OrderId orderId,
            MemberId memberId,
            LegacyUserId legacyUserId,
            Money paymentAmount,
            PaymentStatus paymentStatus,
            PaymentMethodType methodType,
            PgTransactionInfo pgInfo,
            BuyerInfo buyerInfo,
            CardPaymentInfo cardInfo,
            UsedMileage usedMileage,
            Instant paidAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        return new Payment(
                id,
                orderId,
                memberId,
                legacyUserId,
                paymentAmount,
                paymentStatus,
                methodType,
                pgInfo,
                buyerInfo,
                cardInfo,
                usedMileage,
                paidAt,
                cancelledAt,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 결제 완료. PROCESSING → COMPLETED 상태로 전이합니다.
     *
     * @param pgInfo PG 트랜잭션 정보
     * @param cardInfo 카드 결제 정보 (nullable)
     * @param now 현재 시간
     */
    public void complete(PgTransactionInfo pgInfo, CardPaymentInfo cardInfo, Instant now) {
        transitTo(PaymentStatus.COMPLETED, now);
        this.pgInfo = pgInfo;
        this.cardInfo = cardInfo;
        this.paidAt = now;
    }

    /**
     * 결제 실패. PROCESSING → FAILED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void fail(Instant now) {
        transitTo(PaymentStatus.FAILED, now);
    }

    /**
     * 부분 환불. COMPLETED → PARTIALLY_REFUNDED 상태로 전이합니다.
     *
     * @param refundAmount 환불 금액
     * @param now 현재 시간
     * @throws PaymentException 환불 금액이 결제 금액 이상인 경우
     */
    public void partialRefund(Money refundAmount, Instant now) {
        if (refundAmount.isGreaterThanOrEqual(paymentAmount)) {
            throw new PaymentException(
                    PaymentErrorCode.REFUND_AMOUNT_EXCEEDS_PAYMENT,
                    String.format(
                            "부분 환불 금액(%d원)이 결제 금액(%d원) 이상입니다",
                            refundAmount.value(), paymentAmount.value()));
        }
        transitTo(PaymentStatus.PARTIALLY_REFUNDED, now);
    }

    /**
     * 전액 환불. COMPLETED 또는 PARTIALLY_REFUNDED → REFUNDED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void fullRefund(Instant now) {
        transitTo(PaymentStatus.REFUNDED, now);
        this.cancelledAt = now;
    }

    /**
     * 결제 취소. PROCESSING → CANCELLED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void cancel(Instant now) {
        transitTo(PaymentStatus.CANCELLED, now);
        this.cancelledAt = now;
    }

    private void transitTo(PaymentStatus target, Instant now) {
        if (!this.paymentStatus.canTransitionTo(target)) {
            throw new InvalidPaymentStatusException(this.paymentStatus, target);
        }
        this.paymentStatus = target;
        this.updatedAt = now;
    }

    /**
     * 환불 가능 여부를 확인합니다.
     *
     * @return 환불 가능한 경우 true
     */
    public boolean isRefundable() {
        return paymentStatus.isRefundable();
    }

    /**
     * 결제 완료 여부를 확인합니다.
     *
     * @return 결제 완료 상태인 경우 true
     */
    public boolean isCompleted() {
        return paymentStatus.isCompleted();
    }

    // VO Getters
    public PaymentId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public OrderId orderId() {
        return orderId;
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public String memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public LegacyUserId legacyUserId() {
        return legacyUserId;
    }

    public long legacyUserIdValue() {
        return legacyUserId.value();
    }

    public Money paymentAmount() {
        return paymentAmount;
    }

    public int paymentAmountValue() {
        return paymentAmount.value();
    }

    public PaymentStatus paymentStatus() {
        return paymentStatus;
    }

    public PaymentMethodType methodType() {
        return methodType;
    }

    public PgTransactionInfo pgInfo() {
        return pgInfo;
    }

    public BuyerInfo buyerInfo() {
        return buyerInfo;
    }

    public CardPaymentInfo cardInfo() {
        return cardInfo;
    }

    public UsedMileage usedMileage() {
        return usedMileage;
    }

    public Instant paidAt() {
        return paidAt;
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
}
