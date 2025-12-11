package com.ryuqq.setof.domain.category.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * CategoryName Value Object 테스트
 *
 * <p>카테고리명 (필수, 최대 255자)
 */
@DisplayName("CategoryName Value Object")
class CategoryNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 카테고리명으로 CategoryName 생성")
        void shouldCreateCategoryNameWithValidValue() {
            // Given
            String validName = "패션";

            // When
            CategoryName categoryName = CategoryName.of(validName);

            // Then
            assertNotNull(categoryName);
            assertEquals(validName, categoryName.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 카테고리명으로 생성")
        @ValueSource(strings = {"패션", "전자제품", "가구/인테리어", "스포츠/레저"})
        void shouldCreateCategoryNameWithVariousValidValues(String validName) {
            // When
            CategoryName categoryName = CategoryName.of(validName);

            // Then
            assertNotNull(categoryName);
            assertEquals(validName, categoryName.value());
        }

        @Test
        @DisplayName("최대 길이(255자) 카테고리명으로 생성")
        void shouldCreateCategoryNameWithMaxLength() {
            // Given
            String maxLengthName = "가".repeat(255);

            // When
            CategoryName categoryName = CategoryName.of(maxLengthName);

            // Then
            assertNotNull(categoryName);
            assertEquals(255, categoryName.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // Given
            String nullName = null;

            // When & Then
            assertThrows(InvalidCategoryNameException.class, () -> CategoryName.of(nullName));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // Given
            String emptyName = "";

            // When & Then
            assertThrows(InvalidCategoryNameException.class, () -> CategoryName.of(emptyName));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Given
            String blankName = "   ";

            // When & Then
            assertThrows(InvalidCategoryNameException.class, () -> CategoryName.of(blankName));
        }

        @Test
        @DisplayName("255자 초과 카테고리명으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String tooLongName = "가".repeat(256);

            // When & Then
            assertThrows(InvalidCategoryNameException.class, () -> CategoryName.of(tooLongName));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 CategoryName는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            CategoryName name1 = CategoryName.of("패션");
            CategoryName name2 = CategoryName.of("패션");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 CategoryName는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            CategoryName name1 = CategoryName.of("패션");
            CategoryName name2 = CategoryName.of("전자제품");

            // When & Then
            assertNotEquals(name1, name2);
        }
    }
}
