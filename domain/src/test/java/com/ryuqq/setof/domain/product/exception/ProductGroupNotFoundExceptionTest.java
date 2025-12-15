package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** ProductGroupNotFoundException 단위 테스트 */
@DisplayName("ProductGroupNotFoundException 단위 테스트")
class ProductGroupNotFoundExceptionTest {

    @Nested
    @DisplayName("ProductGroupId 생성자")
    class ProductGroupIdConstructor {

        @Test
        @DisplayName("성공 - ProductGroupId로 예외를 생성한다")
        void shouldCreateWithProductGroupId() {
            // Given
            ProductGroupId productGroupId = ProductGroupId.of(1L);

            // When
            ProductGroupNotFoundException exception =
                    new ProductGroupNotFoundException(productGroupId);

            // Then
            assertThat(exception.getMessage()).contains("상품그룹을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND);
        }

        @Test
        @DisplayName("성공 - null ProductGroupId로 예외를 생성한다")
        void shouldCreateWithNullProductGroupId() {
            // When
            ProductGroupNotFoundException exception =
                    new ProductGroupNotFoundException((ProductGroupId) null);

            // Then
            assertThat(exception.getMessage()).contains("상품그룹을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Long 생성자")
    class LongConstructor {

        @Test
        @DisplayName("성공 - Long ID로 예외를 생성한다")
        void shouldCreateWithLongId() {
            // Given
            Long productGroupId = 1L;

            // When
            ProductGroupNotFoundException exception =
                    new ProductGroupNotFoundException(productGroupId);

            // Then
            assertThat(exception.getMessage()).contains("상품그룹을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND);
        }

        @Test
        @DisplayName("성공 - null Long ID로 예외를 생성한다")
        void shouldCreateWithNullLongId() {
            // When
            ProductGroupNotFoundException exception =
                    new ProductGroupNotFoundException((Long) null);

            // Then
            assertThat(exception.getMessage()).contains("상품그룹을 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND);
        }
    }
}
