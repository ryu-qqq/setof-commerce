package com.ryuqq.setof.domain.category.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** CategoryNotFoundException 단위 테스트 */
@DisplayName("CategoryNotFoundException 단위 테스트")
class CategoryNotFoundExceptionTest {

    @Nested
    @DisplayName("categoryId 생성자")
    class CategoryIdConstructor {

        @Test
        @DisplayName("성공 - categoryId로 예외를 생성한다")
        void shouldCreateWithCategoryId() {
            // Given
            Long categoryId = 1L;

            // When
            CategoryNotFoundException exception = new CategoryNotFoundException(categoryId);

            // Then
            assertThat(exception.getMessage()).contains("카테고리를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("categoryCode 생성자")
    class CategoryCodeConstructor {

        @Test
        @DisplayName("성공 - categoryCode로 예외를 생성한다")
        void shouldCreateWithCategoryCode() {
            // Given
            String categoryCode = "FASHION001";

            // When
            CategoryNotFoundException exception = new CategoryNotFoundException(categoryCode);

            // Then
            assertThat(exception.getMessage()).contains("카테고리를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("FASHION001");
        }
    }
}
