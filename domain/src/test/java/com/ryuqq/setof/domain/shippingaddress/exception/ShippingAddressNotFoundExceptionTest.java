package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ShippingAddressNotFoundException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingAddressNotFoundException 단위 테스트")
class ShippingAddressNotFoundExceptionTest {

    @Nested
    @DisplayName("단일 파라미터 생성자")
    class SingleParameterConstructor {

        @Test
        @DisplayName("성공 - shippingAddressId로 예외를 생성한다")
        void shouldCreateWithShippingAddressId() {
            // Given
            Long shippingAddressId = 1L;

            // When
            ShippingAddressNotFoundException exception =
                    new ShippingAddressNotFoundException(shippingAddressId);

            // Then
            assertThat(exception.getMessage()).contains("배송지를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("두 개 파라미터 생성자")
    class TwoParameterConstructor {

        @Test
        @DisplayName("성공 - shippingAddressId와 memberId로 예외를 생성한다")
        void shouldCreateWithShippingAddressIdAndMemberId() {
            // Given
            Long shippingAddressId = 1L;
            UUID memberId = UUID.randomUUID();

            // When
            ShippingAddressNotFoundException exception =
                    new ShippingAddressNotFoundException(shippingAddressId, memberId);

            // Then
            assertThat(exception.getMessage()).contains("배송지를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getMessage()).contains(memberId.toString());
        }
    }
}
