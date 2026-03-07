package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressException 단위 테스트")
class ShippingAddressExceptionTest {

    @Nested
    @DisplayName("ShippingAddressException 기본 생성 테스트")
    class BaseExceptionCreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ShippingAddressException exception =
                    new ShippingAddressException(
                            ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("배송지를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("SHP-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ShippingAddressException exception =
                    new ShippingAddressException(
                            ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND, "ID 999 배송지 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 999 배송지 없음");
            assertThat(exception.code()).isEqualTo("SHP-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ShippingAddressException exception =
                    new ShippingAddressException(
                            ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("SHP-001");
        }

        @Test
        @DisplayName("ShippingAddressException은 DomainException을 상속한다")
        void extendsdomainException() {
            // given
            ShippingAddressException exception =
                    new ShippingAddressException(
                            ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("ShippingAddressNotFoundException 테스트")
    class NotFoundExceptionTest {

        @Test
        @DisplayName("기본 생성자로 ShippingAddressNotFoundException을 생성한다")
        void createWithDefaultConstructor() {
            // when
            ShippingAddressNotFoundException exception = new ShippingAddressNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("SHP-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("배송지를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ID를 포함한 메시지로 ShippingAddressNotFoundException을 생성한다")
        void createWithId() {
            // when
            ShippingAddressNotFoundException exception = new ShippingAddressNotFoundException(42L);

            // then
            assertThat(exception.code()).isEqualTo("SHP-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("ID가 42인 배송지를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ShippingAddressNotFoundException은 ShippingAddressException을 상속한다")
        void extendsShippingAddressException() {
            // given
            ShippingAddressNotFoundException exception = new ShippingAddressNotFoundException();

            // then
            assertThat(exception).isInstanceOf(ShippingAddressException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("ShippingAddressLimitExceededException 테스트")
    class LimitExceededExceptionTest {

        @Test
        @DisplayName("ShippingAddressLimitExceededException을 생성한다")
        void createLimitExceededException() {
            // when
            ShippingAddressLimitExceededException exception =
                    new ShippingAddressLimitExceededException();

            // then
            assertThat(exception.code()).isEqualTo("SHP-003");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("배송지는 최대 5개까지 등록할 수 있습니다");
        }

        @Test
        @DisplayName("ShippingAddressLimitExceededException은 ShippingAddressException을 상속한다")
        void extendsShippingAddressException() {
            // given
            ShippingAddressLimitExceededException exception =
                    new ShippingAddressLimitExceededException();

            // then
            assertThat(exception).isInstanceOf(ShippingAddressException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("CannotUnmarkDefaultShippingAddressException 테스트")
    class CannotUnmarkDefaultExceptionTest {

        @Test
        @DisplayName("CannotUnmarkDefaultShippingAddressException을 생성한다")
        void createCannotUnmarkException() {
            // when
            CannotUnmarkDefaultShippingAddressException exception =
                    new CannotUnmarkDefaultShippingAddressException();

            // then
            assertThat(exception.code()).isEqualTo("SHP-002");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("기본 배송지 설정은 해제할 수 없습니다");
        }

        @Test
        @DisplayName("CannotUnmarkDefaultShippingAddressException은 ShippingAddressException을 상속한다")
        void extendsShippingAddressException() {
            // given
            CannotUnmarkDefaultShippingAddressException exception =
                    new CannotUnmarkDefaultShippingAddressException();

            // then
            assertThat(exception).isInstanceOf(ShippingAddressException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
