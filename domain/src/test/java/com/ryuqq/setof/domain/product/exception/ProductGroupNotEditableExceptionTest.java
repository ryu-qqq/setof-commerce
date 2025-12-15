package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** ProductGroupNotEditableException 단위 테스트 */
@DisplayName("ProductGroupNotEditableException 단위 테스트")
class ProductGroupNotEditableExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("성공 - ProductGroupId와 ProductGroupStatus로 예외를 생성한다")
        void shouldCreateWithProductGroupIdAndStatus() {
            // Given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            ProductGroupStatus currentStatus = ProductGroupStatus.DELETED;

            // When
            ProductGroupNotEditableException exception =
                    new ProductGroupNotEditableException(productGroupId, currentStatus);

            // Then
            assertThat(exception.getMessage()).contains("수정할 수 없는 상품그룹입니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getMessage()).contains("DELETED");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_EDITABLE);
        }

        @Test
        @DisplayName("성공 - null ProductGroupId로 예외를 생성한다")
        void shouldCreateWithNullProductGroupId() {
            // Given
            ProductGroupStatus currentStatus = ProductGroupStatus.DELETED;

            // When
            ProductGroupNotEditableException exception =
                    new ProductGroupNotEditableException(null, currentStatus);

            // Then
            assertThat(exception.getMessage()).contains("수정할 수 없는 상품그룹입니다");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getMessage()).contains("DELETED");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_EDITABLE);
        }

        @Test
        @DisplayName("성공 - null ProductGroupStatus로 예외를 생성한다")
        void shouldCreateWithNullStatus() {
            // Given
            ProductGroupId productGroupId = ProductGroupId.of(1L);

            // When
            ProductGroupNotEditableException exception =
                    new ProductGroupNotEditableException(productGroupId, null);

            // Then
            assertThat(exception.getMessage()).contains("수정할 수 없는 상품그룹입니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.PRODUCT_GROUP_NOT_EDITABLE);
        }
    }
}
