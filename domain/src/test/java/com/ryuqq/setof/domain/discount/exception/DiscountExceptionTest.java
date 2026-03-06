package com.ryuqq.setof.domain.discount.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountException 테스트")
class DiscountExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            var exception = new DiscountException(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND);
            assertThat(exception.getMessage()).isEqualTo("할인 정책을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("DISC-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            var exception =
                    new DiscountException(
                            DiscountErrorCode.INVALID_DISCOUNT_CONFIG, "RATE에 할인금 설정됨");
            assertThat(exception.getMessage()).isEqualTo("RATE에 할인금 설정됨");
            assertThat(exception.code()).isEqualTo("DISC-002");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            RuntimeException cause = new RuntimeException("원인 예외");
            var exception = new DiscountException(DiscountErrorCode.INSUFFICIENT_BUDGET, cause);
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("DISC-003");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("DiscountPolicyNotFoundException 기본 생성")
        void createDiscountPolicyNotFoundException() {
            var exception = new DiscountPolicyNotFoundException();
            assertThat(exception.code()).isEqualTo("DISC-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("할인 정책을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("DiscountPolicyNotFoundException ID 포함 생성")
        void createDiscountPolicyNotFoundExceptionWithId() {
            var exception = new DiscountPolicyNotFoundException(123L);
            assertThat(exception.code()).isEqualTo("DISC-001");
            assertThat(exception.getMessage()).contains("123");
        }

        @Test
        @DisplayName("InvalidDiscountConfigException 기본 생성")
        void createInvalidDiscountConfigException() {
            var exception = new InvalidDiscountConfigException();
            assertThat(exception.code()).isEqualTo("DISC-002");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("InvalidDiscountConfigException 상세 메시지 생성")
        void createInvalidDiscountConfigExceptionWithDetail() {
            var exception = new InvalidDiscountConfigException("RATE에 정액 할인 설정 불가");
            assertThat(exception.getMessage()).isEqualTo("RATE에 정액 할인 설정 불가");
        }

        @Test
        @DisplayName("InsufficientBudgetException 기본 생성")
        void createInsufficientBudgetException() {
            var exception = new InsufficientBudgetException();
            assertThat(exception.code()).isEqualTo("DISC-003");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("InsufficientBudgetException ID 포함 생성")
        void createInsufficientBudgetExceptionWithId() {
            var exception = new InsufficientBudgetException(456L);
            assertThat(exception.getMessage()).contains("456");
        }

        @Test
        @DisplayName("CouponNotFoundException 기본 생성")
        void createCouponNotFoundException() {
            var exception = new CouponNotFoundException();
            assertThat(exception.code()).isEqualTo("DISC-004");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("CouponNotFoundException ID 포함 생성")
        void createCouponNotFoundExceptionWithId() {
            var exception = new CouponNotFoundException(789L);
            assertThat(exception.getMessage()).contains("789");
        }

        @Test
        @DisplayName("CouponAlreadyIssuedException 기본 생성")
        void createCouponAlreadyIssuedException() {
            var exception = new CouponAlreadyIssuedException();
            assertThat(exception.code()).isEqualTo("DISC-005");
            assertThat(exception.httpStatus()).isEqualTo(409);
        }

        @Test
        @DisplayName("CouponAlreadyIssuedException 상세 메시지 생성")
        void createCouponAlreadyIssuedExceptionWithDetail() {
            var exception = new CouponAlreadyIssuedException(1L, 100L);
            assertThat(exception.getMessage()).contains("1").contains("100");
        }

        @Test
        @DisplayName("CouponExpiredException 기본 생성")
        void createCouponExpiredException() {
            var exception = new CouponExpiredException();
            assertThat(exception.code()).isEqualTo("DISC-006");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("CouponIssuanceLimitExceededException 기본 생성")
        void createCouponIssuanceLimitExceededException() {
            var exception = new CouponIssuanceLimitExceededException();
            assertThat(exception.code()).isEqualTo("DISC-007");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("DiscountException은 DomainException을 상속한다")
        void discountExceptionExtendsDomainException() {
            var exception = new DiscountException(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("DiscountPolicyNotFoundException은 DiscountException을 상속한다")
        void discountPolicyNotFoundExtendsDiscountException() {
            var exception = new DiscountPolicyNotFoundException();
            assertThat(exception).isInstanceOf(DiscountException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("CouponNotFoundException은 DiscountException을 상속한다")
        void couponNotFoundExtendsDiscountException() {
            var exception = new CouponNotFoundException();
            assertThat(exception).isInstanceOf(DiscountException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("InvalidDiscountConfigException은 DiscountException을 상속한다")
        void invalidDiscountConfigExtendsDiscountException() {
            var exception = new InvalidDiscountConfigException();
            assertThat(exception).isInstanceOf(DiscountException.class);
        }

        @Test
        @DisplayName("InsufficientBudgetException은 DiscountException을 상속한다")
        void insufficientBudgetExtendsDiscountException() {
            var exception = new InsufficientBudgetException();
            assertThat(exception).isInstanceOf(DiscountException.class);
        }
    }
}
