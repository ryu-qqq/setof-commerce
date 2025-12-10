package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InvalidDeliveryRequestException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("InvalidDeliveryRequestException 단위 테스트")
class InvalidDeliveryRequestExceptionTest {

    @Test
    @DisplayName("성공 - 너무 긴 배송 요청사항으로 예외를 생성한다")
    void shouldCreateWithTooLongDeliveryRequest() {
        // Given
        String longDeliveryRequest = "a".repeat(201);

        // When
        InvalidDeliveryRequestException exception =
                new InvalidDeliveryRequestException(longDeliveryRequest);

        // Then
        assertThat(exception.getMessage()).contains("최대 길이(200자)를 초과");
        assertThat(exception.getMessage()).contains("201");
        assertThat(exception.getErrorCode()).isEqualTo(ShippingAddressErrorCode.INVALID_DELIVERY_REQUEST);
    }

    @Test
    @DisplayName("성공 - null 배송 요청사항으로 예외를 생성한다")
    void shouldCreateWithNullDeliveryRequest() {
        // When
        InvalidDeliveryRequestException exception =
                new InvalidDeliveryRequestException(null);

        // Then
        assertThat(exception.getMessage()).contains("현재 길이: 0");
    }
}
