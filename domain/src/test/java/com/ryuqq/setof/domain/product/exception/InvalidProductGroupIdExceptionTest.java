package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** InvalidProductGroupIdException 단위 테스트 */
@DisplayName("InvalidProductGroupIdException 단위 테스트")
class InvalidProductGroupIdExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("성공 - productGroupId로 예외를 생성한다")
        void shouldCreateWithProductGroupId() {
            // Given
            Long productGroupId = -1L;

            // When
            InvalidProductGroupIdException exception =
                    new InvalidProductGroupIdException(productGroupId);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품그룹 ID");
            assertThat(exception.getMessage()).contains("-1");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("성공 - null productGroupId로 예외를 생성한다")
        void shouldCreateWithNullProductGroupId() {
            // When
            InvalidProductGroupIdException exception = new InvalidProductGroupIdException(null);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품그룹 ID");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_GROUP_ID);
        }
    }
}
