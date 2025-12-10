package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InvalidDeliveryAddressException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("InvalidDeliveryAddressException 단위 테스트")
class InvalidDeliveryAddressExceptionTest {

    @Test
    @DisplayName("성공 - addressTooLong 정적 메서드로 예외를 생성한다")
    void shouldCreateWithAddressTooLong() {
        // Given
        String fieldName = "도로명주소";
        int maxLength = 200;

        // When
        InvalidDeliveryAddressException exception =
                InvalidDeliveryAddressException.addressTooLong(fieldName, maxLength);

        // Then
        assertThat(exception.getMessage()).contains("도로명주소");
        assertThat(exception.getMessage()).contains("200자");
        assertThat(exception.getErrorCode()).isEqualTo(ShippingAddressErrorCode.ADDRESS_TOO_LONG);
    }

    @Test
    @DisplayName("성공 - invalidZipCode 정적 메서드로 예외를 생성한다")
    void shouldCreateWithInvalidZipCode() {
        // Given
        String invalidZipCode = "123";

        // When
        InvalidDeliveryAddressException exception =
                InvalidDeliveryAddressException.invalidZipCode(invalidZipCode);

        // Then
        assertThat(exception.getMessage()).contains("우편번호 형식이 올바르지 않습니다");
        assertThat(exception.getMessage()).contains("123");
        assertThat(exception.getErrorCode()).isEqualTo(ShippingAddressErrorCode.INVALID_ZIP_CODE);
    }

    @Test
    @DisplayName("성공 - invalidZipCode에 null을 전달하면 null 문자열이 포함된다")
    void shouldHandleNullZipCode() {
        // When
        InvalidDeliveryAddressException exception =
                InvalidDeliveryAddressException.invalidZipCode(null);

        // Then
        assertThat(exception.getMessage()).contains("null");
    }
}
