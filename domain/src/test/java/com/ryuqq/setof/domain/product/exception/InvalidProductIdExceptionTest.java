package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** InvalidProductIdException 단위 테스트 */
@DisplayName("InvalidProductIdException 단위 테스트")
class InvalidProductIdExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("성공 - productId로 예외를 생성한다")
        void shouldCreateWithProductId() {
            // Given
            Long productId = -1L;

            // When
            InvalidProductIdException exception = new InvalidProductIdException(productId);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품 ID");
            assertThat(exception.getMessage()).contains("-1");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_ID);
        }

        @Test
        @DisplayName("성공 - null productId로 예외를 생성한다")
        void shouldCreateWithNullProductId() {
            // When
            InvalidProductIdException exception = new InvalidProductIdException(null);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품 ID");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_ID);
        }
    }
}
