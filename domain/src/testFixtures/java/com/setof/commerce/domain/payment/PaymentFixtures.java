package com.setof.commerce.domain.payment;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.id.PaymentId;
import com.ryuqq.setof.domain.payment.vo.BuyerInfo;
import com.ryuqq.setof.domain.payment.vo.CardPaymentInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentMethodType;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import com.ryuqq.setof.domain.payment.vo.PgTransactionInfo;
import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;
import com.ryuqq.setof.domain.payment.vo.UsedMileage;
import com.ryuqq.setof.domain.payment.vo.VBankInfo;
import java.time.Instant;

/**
 * Payment 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Payment 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class PaymentFixtures {

    private PaymentFixtures() {}

    // ===== ID Fixtures =====

    public static PaymentId defaultPaymentId() {
        return PaymentId.of(1L);
    }

    public static PaymentId paymentId(Long value) {
        return PaymentId.of(value);
    }

    // ===== VO Fixtures =====

    public static LegacyOrderId defaultLegacyOrderId() {
        return LegacyOrderId.of(100L);
    }

    public static MemberId defaultMemberId() {
        return MemberId.of(1L);
    }

    public static LegacyUserId defaultLegacyUserId() {
        return LegacyUserId.of(999L);
    }

    public static Money defaultPaymentAmount() {
        return Money.of(50000);
    }

    public static BuyerInfo defaultBuyerInfo() {
        return BuyerInfo.of("홍길동", "test@example.com", "010-1234-5678");
    }

    public static BuyerInfo buyerInfoWithoutOptionals() {
        return BuyerInfo.of("홍길동", null, null);
    }

    public static UsedMileage defaultUsedMileage() {
        return UsedMileage.of(0);
    }

    public static UsedMileage usedMileage(long amount) {
        return UsedMileage.of(amount);
    }

    public static PgTransactionInfo defaultPgTransactionInfo() {
        return PgTransactionInfo.of(
                "PG_KCP", "TXN_20240101_001", "https://receipt.example.com/TXN_20240101_001");
    }

    public static CardPaymentInfo defaultCardPaymentInfo() {
        return CardPaymentInfo.of("삼성카드", "1234-****-****-5678");
    }

    public static CardPaymentInfo installmentCardPaymentInfo() {
        return CardPaymentInfo.of("신한카드", "4567-****-****-1234", 3);
    }

    public static VBankInfo defaultVBankInfo() {
        return VBankInfo.of("국민은행", "123456789012", 50000L, CommonVoFixtures.tomorrow());
    }

    public static RefundAccountSnapshot defaultRefundAccountSnapshot() {
        return RefundAccountSnapshot.of(999L, "004", "110-123-456789", "홍길동");
    }

    // ===== Aggregate Fixtures =====

    public static Payment newPayment() {
        return Payment.forNew(
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentMethodType.CARD,
                defaultBuyerInfo(),
                defaultUsedMileage(),
                CommonVoFixtures.now());
    }

    public static Payment newPaymentWithMileage() {
        return Payment.forNew(
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentMethodType.CARD,
                defaultBuyerInfo(),
                UsedMileage.of(5000),
                CommonVoFixtures.now());
    }

    public static Payment newPaymentWithoutMember() {
        return Payment.forNew(
                defaultLegacyOrderId(),
                null,
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentMethodType.CARD,
                defaultBuyerInfo(),
                defaultUsedMileage(),
                CommonVoFixtures.now());
    }

    public static Payment processingPayment() {
        return Payment.reconstitute(
                defaultPaymentId(),
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentStatus.PROCESSING,
                PaymentMethodType.CARD,
                null,
                defaultBuyerInfo(),
                null,
                null,
                defaultUsedMileage(),
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Payment completedPayment() {
        Instant paidAt = CommonVoFixtures.yesterday();
        return Payment.reconstitute(
                defaultPaymentId(),
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentStatus.COMPLETED,
                PaymentMethodType.CARD,
                defaultPgTransactionInfo(),
                defaultBuyerInfo(),
                defaultCardPaymentInfo(),
                null,
                defaultUsedMileage(),
                paidAt,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Payment partiallyRefundedPayment() {
        Instant paidAt = CommonVoFixtures.yesterday();
        return Payment.reconstitute(
                defaultPaymentId(),
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentStatus.PARTIALLY_REFUNDED,
                PaymentMethodType.CARD,
                defaultPgTransactionInfo(),
                defaultBuyerInfo(),
                defaultCardPaymentInfo(),
                null,
                defaultUsedMileage(),
                paidAt,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Payment failedPayment() {
        return Payment.reconstitute(
                defaultPaymentId(),
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentStatus.FAILED,
                PaymentMethodType.CARD,
                null,
                defaultBuyerInfo(),
                null,
                null,
                defaultUsedMileage(),
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Payment cancelledPayment() {
        Instant cancelledAt = CommonVoFixtures.yesterday();
        return Payment.reconstitute(
                defaultPaymentId(),
                defaultLegacyOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultPaymentAmount(),
                PaymentStatus.CANCELLED,
                PaymentMethodType.CARD,
                null,
                defaultBuyerInfo(),
                null,
                null,
                defaultUsedMileage(),
                null,
                cancelledAt,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }
}
