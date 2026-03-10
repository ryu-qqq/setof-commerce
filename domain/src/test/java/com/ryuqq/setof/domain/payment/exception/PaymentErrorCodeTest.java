package com.ryuqq.setof.domain.payment.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("PaymentErrorCode 단위 테스트")
class PaymentErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(PaymentErrorCode.PAYMENT_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("결제 에러 코드 값 검증 테스트")
    class PaymentErrorCodesTest {

        @Test
        @DisplayName("PAYMENT_NOT_FOUND 에러 코드를 검증한다")
        void paymentNotFound() {
            assertThat(PaymentErrorCode.PAYMENT_NOT_FOUND.getCode()).isEqualTo("PMT-001");
            assertThat(PaymentErrorCode.PAYMENT_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(PaymentErrorCode.PAYMENT_NOT_FOUND.getMessage())
                    .isEqualTo("결제 정보를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("INVALID_PAYMENT_STATUS 에러 코드를 검증한다")
        void invalidPaymentStatus() {
            assertThat(PaymentErrorCode.INVALID_PAYMENT_STATUS.getCode()).isEqualTo("PMT-002");
            assertThat(PaymentErrorCode.INVALID_PAYMENT_STATUS.getHttpStatus()).isEqualTo(400);
            assertThat(PaymentErrorCode.INVALID_PAYMENT_STATUS.getMessage())
                    .isEqualTo("유효하지 않은 결제 상태 전이입니다");
        }

        @Test
        @DisplayName("PAYMENT_AMOUNT_MISMATCH 에러 코드를 검증한다")
        void paymentAmountMismatch() {
            assertThat(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.getCode()).isEqualTo("PMT-003");
            assertThat(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.getHttpStatus()).isEqualTo(400);
            assertThat(PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage())
                    .isEqualTo("결제 금액이 일치하지 않습니다");
        }

        @Test
        @DisplayName("REFUND_AMOUNT_EXCEEDS_PAYMENT 에러 코드를 검증한다")
        void refundAmountExceedsPayment() {
            assertThat(PaymentErrorCode.REFUND_AMOUNT_EXCEEDS_PAYMENT.getCode())
                    .isEqualTo("PMT-004");
            assertThat(PaymentErrorCode.REFUND_AMOUNT_EXCEEDS_PAYMENT.getHttpStatus())
                    .isEqualTo(400);
            assertThat(PaymentErrorCode.REFUND_AMOUNT_EXCEEDS_PAYMENT.getMessage())
                    .isEqualTo("환불 금액이 결제 금액을 초과합니다");
        }

        @Test
        @DisplayName("PRODUCT_PRICE_MISMATCH 에러 코드를 검증한다")
        void productPriceMismatch() {
            assertThat(PaymentErrorCode.PRODUCT_PRICE_MISMATCH.getCode()).isEqualTo("PMT-005");
            assertThat(PaymentErrorCode.PRODUCT_PRICE_MISMATCH.getHttpStatus()).isEqualTo(400);
            assertThat(PaymentErrorCode.PRODUCT_PRICE_MISMATCH.getMessage())
                    .isEqualTo("상품 가격이 일치하지 않습니다");
        }

        @Test
        @DisplayName("INVALID_MILEAGE_UNIT 에러 코드를 검증한다")
        void invalidMileageUnit() {
            assertThat(PaymentErrorCode.INVALID_MILEAGE_UNIT.getCode()).isEqualTo("PMT-006");
            assertThat(PaymentErrorCode.INVALID_MILEAGE_UNIT.getHttpStatus()).isEqualTo(400);
            assertThat(PaymentErrorCode.INVALID_MILEAGE_UNIT.getMessage())
                    .isEqualTo("적립금은 100원 단위로 사용할 수 있습니다");
        }

        @Test
        @DisplayName("INSUFFICIENT_CASH_WITH_MILEAGE 에러 코드를 검증한다")
        void insufficientCashWithMileage() {
            assertThat(PaymentErrorCode.INSUFFICIENT_CASH_WITH_MILEAGE.getCode())
                    .isEqualTo("PMT-007");
            assertThat(PaymentErrorCode.INSUFFICIENT_CASH_WITH_MILEAGE.getHttpStatus())
                    .isEqualTo(400);
            assertThat(PaymentErrorCode.INSUFFICIENT_CASH_WITH_MILEAGE.getMessage())
                    .isEqualTo("적립금을 사용하려면 결제 금액이 최소 10,000원 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(PaymentErrorCode.values())
                    .containsExactly(
                            PaymentErrorCode.PAYMENT_NOT_FOUND,
                            PaymentErrorCode.INVALID_PAYMENT_STATUS,
                            PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH,
                            PaymentErrorCode.REFUND_AMOUNT_EXCEEDS_PAYMENT,
                            PaymentErrorCode.PRODUCT_PRICE_MISMATCH,
                            PaymentErrorCode.INVALID_MILEAGE_UNIT,
                            PaymentErrorCode.INSUFFICIENT_CASH_WITH_MILEAGE);
        }
    }
}
