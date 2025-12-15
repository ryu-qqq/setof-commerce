package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ryuqq.setof.domain.product.exception.InvalidProductGroupNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupName Value Object 테스트
 *
 * <p>Zero-Tolerance Rules: - Lombok 금지 (Pure Java Record) - 불변성 보장 - 최대 200자 검증
 */
@DisplayName("ProductGroupName Value Object")
class ProductGroupNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 이름으로 ProductGroupName 생성")
        void shouldCreateProductGroupNameWithValidValue() {
            // Given
            String validName = "테스트 상품그룹";

            // When
            ProductGroupName name = ProductGroupName.of(validName);

            // Then
            assertNotNull(name);
            assertEquals(validName, name.value());
        }

        @Test
        @DisplayName("최대 길이(200자) 이름으로 생성")
        void shouldCreateProductGroupNameWithMaxLength() {
            // Given
            String maxLengthName = "가".repeat(200);

            // When
            ProductGroupName name = ProductGroupName.of(maxLengthName);

            // Then
            assertNotNull(name);
            assertEquals(200, name.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("null 값으로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsNull() {
            // When & Then
            assertThrows(InvalidProductGroupNameException.class, () -> ProductGroupName.of(null));
        }

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // When & Then
            assertThrows(InvalidProductGroupNameException.class, () -> ProductGroupName.of(""));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenNameIsBlank() {
            // When & Then
            assertThrows(InvalidProductGroupNameException.class, () -> ProductGroupName.of("   "));
        }

        @Test
        @DisplayName("200자 초과 시 예외 발생")
        void shouldThrowExceptionWhenNameExceedsMaxLength() {
            // Given
            String tooLongName = "가".repeat(201);

            // When & Then
            assertThrows(
                    InvalidProductGroupNameException.class, () -> ProductGroupName.of(tooLongName));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ProductGroupName은 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            ProductGroupName name1 = ProductGroupName.of("테스트");
            ProductGroupName name2 = ProductGroupName.of("테스트");

            // When & Then
            assertEquals(name1, name2);
            assertEquals(name1.hashCode(), name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ProductGroupName은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            ProductGroupName name1 = ProductGroupName.of("테스트1");
            ProductGroupName name2 = ProductGroupName.of("테스트2");

            // When & Then
            assertNotEquals(name1, name2);
        }
    }
}
