package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressErrorCode 단위 테스트")
class ShippingAddressErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ShippingAddressErrorCode는 ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND)
                    .isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("각 에러 코드 값 검증")
    class ErrorCodeValuesTest {

        @Test
        @DisplayName("SHIPPING_ADDRESS_NOT_FOUND 에러 코드를 검증한다")
        void shippingAddressNotFound() {
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND.getCode())
                    .isEqualTo("SHP-001");
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND.getHttpStatus())
                    .isEqualTo(404);
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND.getMessage())
                    .isEqualTo("배송지를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS 에러 코드를 검증한다")
        void cannotUnmarkDefault() {
            assertThat(ShippingAddressErrorCode.CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS.getCode())
                    .isEqualTo("SHP-002");
            assertThat(
                            ShippingAddressErrorCode.CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS
                                    .getHttpStatus())
                    .isEqualTo(400);
            assertThat(ShippingAddressErrorCode.CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS.getMessage())
                    .isEqualTo("기본 배송지 설정은 해제할 수 없습니다");
        }

        @Test
        @DisplayName("SHIPPING_ADDRESS_LIMIT_EXCEEDED 에러 코드를 검증한다")
        void shippingAddressLimitExceeded() {
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED.getCode())
                    .isEqualTo("SHP-003");
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED.getHttpStatus())
                    .isEqualTo(400);
            assertThat(ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED.getMessage())
                    .isEqualTo("배송지는 최대 5개까지 등록할 수 있습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(ShippingAddressErrorCode.values())
                    .containsExactly(
                            ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND,
                            ShippingAddressErrorCode.CANNOT_UNMARK_DEFAULT_SHIPPING_ADDRESS,
                            ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED);
        }
    }
}
