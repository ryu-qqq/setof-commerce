package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InvalidShippingAddressIdException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("InvalidShippingAddressIdException 단위 테스트")
class InvalidShippingAddressIdExceptionTest {

    @Test
    @DisplayName("성공 - 유효하지 않은 ID로 예외를 생성한다")
    void shouldCreateWithInvalidId() {
        // Given
        Long invalidId = -1L;

        // When
        InvalidShippingAddressIdException exception =
                new InvalidShippingAddressIdException(invalidId);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 배송지 ID");
        assertThat(exception.getMessage()).contains("-1");
        assertThat(exception.getErrorCode())
                .isEqualTo(ShippingAddressErrorCode.INVALID_SHIPPING_ADDRESS_ID);
    }

    @Test
    @DisplayName("성공 - null ID로 예외를 생성한다")
    void shouldCreateWithNullId() {
        // When
        InvalidShippingAddressIdException exception = new InvalidShippingAddressIdException(null);

        // Then
        assertThat(exception.getMessage()).contains("유효하지 않은 배송지 ID");
        assertThat(exception.getMessage()).contains("null");
    }
}
