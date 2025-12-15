package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** InvalidProductGroupNameException 단위 테스트 */
@DisplayName("InvalidProductGroupNameException 단위 테스트")
class InvalidProductGroupNameExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("성공 - productGroupName과 reason으로 예외를 생성한다")
        void shouldCreateWithProductGroupNameAndReason() {
            // Given
            String productGroupName = "";
            String reason = "상품그룹명은 필수입니다";

            // When
            InvalidProductGroupNameException exception =
                    new InvalidProductGroupNameException(productGroupName, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품그룹명");
            assertThat(exception.getMessage()).contains("상품그룹명은 필수입니다");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("성공 - null productGroupName으로 예외를 생성한다")
        void shouldCreateWithNullProductGroupName() {
            // Given
            String reason = "상품그룹명은 null일 수 없습니다";

            // When
            InvalidProductGroupNameException exception =
                    new InvalidProductGroupNameException(null, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품그룹명");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("성공 - 최대 길이 초과 사유로 예외를 생성한다")
        void shouldCreateWithMaxLengthExceededReason() {
            // Given
            String productGroupName = "a".repeat(201);
            String reason = "상품그룹명은 200자를 초과할 수 없습니다";

            // When
            InvalidProductGroupNameException exception =
                    new InvalidProductGroupNameException(productGroupName, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 상품그룹명");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductGroupErrorCode.INVALID_PRODUCT_GROUP_NAME);
        }
    }
}
