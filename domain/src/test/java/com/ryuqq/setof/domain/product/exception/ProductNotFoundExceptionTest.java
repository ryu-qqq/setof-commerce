package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.product.vo.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** ProductNotFoundException 단위 테스트 */
@DisplayName("ProductNotFoundException 단위 테스트")
class ProductNotFoundExceptionTest {

    @Nested
    @DisplayName("ProductId 생성자")
    class ProductIdConstructor {

        @Test
        @DisplayName("성공 - ProductId로 예외를 생성한다")
        void shouldCreateWithProductId() {
            // Given
            ProductId productId = ProductId.of(1L);

            // When
            ProductNotFoundException exception = new ProductNotFoundException(productId);

            // Then
            assertThat(exception.getMessage()).contains("상품을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("성공 - null ProductId로 예외를 생성한다")
        void shouldCreateWithNullProductId() {
            // When
            ProductNotFoundException exception = new ProductNotFoundException((ProductId) null);

            // Then
            assertThat(exception.getMessage()).contains("상품을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Long 생성자")
    class LongConstructor {

        @Test
        @DisplayName("성공 - Long ID로 예외를 생성한다")
        void shouldCreateWithLongId() {
            // Given
            Long productId = 1L;

            // When
            ProductNotFoundException exception = new ProductNotFoundException(productId);

            // Then
            assertThat(exception.getMessage()).contains("상품을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.PRODUCT_NOT_FOUND);
        }

        @Test
        @DisplayName("성공 - null Long ID로 예외를 생성한다")
        void shouldCreateWithNullLongId() {
            // When
            ProductNotFoundException exception = new ProductNotFoundException((Long) null);

            // Then
            assertThat(exception.getMessage()).contains("상품을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}
