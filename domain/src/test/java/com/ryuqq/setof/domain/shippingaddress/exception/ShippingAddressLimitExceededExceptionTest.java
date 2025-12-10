package com.ryuqq.setof.domain.shippingaddress.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ShippingAddressLimitExceededException 단위 테스트
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("ShippingAddressLimitExceededException 단위 테스트")
class ShippingAddressLimitExceededExceptionTest {

    @Test
    @DisplayName("성공 - memberId와 currentCount로 예외를 생성한다")
    void shouldCreateWithMemberIdAndCurrentCount() {
        // Given
        UUID memberId = UUID.randomUUID();
        int currentCount = 5;

        // When
        ShippingAddressLimitExceededException exception =
                new ShippingAddressLimitExceededException(memberId, currentCount);

        // Then
        assertThat(exception.getMessage()).contains("최대 5개까지만 등록");
        assertThat(exception.getMessage()).contains("현재: 5개");
        assertThat(exception.getErrorCode()).isEqualTo(ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED);
    }
}
