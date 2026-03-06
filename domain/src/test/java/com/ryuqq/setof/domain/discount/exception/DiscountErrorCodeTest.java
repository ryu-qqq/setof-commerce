package com.ryuqq.setof.domain.discount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountErrorCode 테스트")
class DiscountErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("DISCOUNT_POLICY_NOT_FOUND 테스트")
    class DiscountPolicyNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'DISC-001'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND.getCode()).isEqualTo("DISC-001");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND.getHttpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("메시지는 '할인 정책을 찾을 수 없습니다'이다")
        void hasCorrectMessage() {
            assertThat(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND.getMessage())
                    .isEqualTo("할인 정책을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("INVALID_DISCOUNT_CONFIG 테스트")
    class InvalidDiscountConfigTest {

        @Test
        @DisplayName("에러 코드는 'DISC-002'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.INVALID_DISCOUNT_CONFIG.getCode()).isEqualTo("DISC-002");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.INVALID_DISCOUNT_CONFIG.getHttpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("INSUFFICIENT_BUDGET 테스트")
    class InsufficientBudgetTest {

        @Test
        @DisplayName("에러 코드는 'DISC-003'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.INSUFFICIENT_BUDGET.getCode()).isEqualTo("DISC-003");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.INSUFFICIENT_BUDGET.getHttpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("COUPON_NOT_FOUND 테스트")
    class CouponNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'DISC-004'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.COUPON_NOT_FOUND.getCode()).isEqualTo("DISC-004");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.COUPON_NOT_FOUND.getHttpStatus()).isEqualTo(404);
        }
    }

    @Nested
    @DisplayName("COUPON_ALREADY_ISSUED 테스트")
    class CouponAlreadyIssuedTest {

        @Test
        @DisplayName("에러 코드는 'DISC-005'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.COUPON_ALREADY_ISSUED.getCode()).isEqualTo("DISC-005");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 409이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.COUPON_ALREADY_ISSUED.getHttpStatus()).isEqualTo(409);
        }
    }

    @Nested
    @DisplayName("COUPON_EXPIRED 테스트")
    class CouponExpiredTest {

        @Test
        @DisplayName("에러 코드는 'DISC-006'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.COUPON_EXPIRED.getCode()).isEqualTo("DISC-006");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.COUPON_EXPIRED.getHttpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("COUPON_ISSUANCE_LIMIT_EXCEEDED 테스트")
    class CouponIssuanceLimitExceededTest {

        @Test
        @DisplayName("에러 코드는 'DISC-007'이다")
        void hasCorrectCode() {
            assertThat(DiscountErrorCode.COUPON_ISSUANCE_LIMIT_EXCEEDED.getCode())
                    .isEqualTo("DISC-007");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(DiscountErrorCode.COUPON_ISSUANCE_LIMIT_EXCEEDED.getHttpStatus())
                    .isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(DiscountErrorCode.values())
                    .containsExactly(
                            DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND,
                            DiscountErrorCode.INVALID_DISCOUNT_CONFIG,
                            DiscountErrorCode.INSUFFICIENT_BUDGET,
                            DiscountErrorCode.COUPON_NOT_FOUND,
                            DiscountErrorCode.COUPON_ALREADY_ISSUED,
                            DiscountErrorCode.COUPON_EXPIRED,
                            DiscountErrorCode.COUPON_ISSUANCE_LIMIT_EXCEEDED);
        }
    }
}
