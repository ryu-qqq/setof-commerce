package com.ryuqq.setof.domain.category.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** CategoryErrorCode Enum 테스트 */
@DisplayName("CategoryErrorCode Enum")
class CategoryErrorCodeTest {

    @Nested
    @DisplayName("에러 코드 값 테스트")
    class ErrorCodeValueTest {

        @Test
        @DisplayName("CATEGORY_NOT_FOUND 에러 코드")
        void shouldReturnCorrectValueForCategoryNotFound() {
            // Given
            CategoryErrorCode errorCode = CategoryErrorCode.CATEGORY_NOT_FOUND;

            // When & Then
            assertEquals("CAT-001", errorCode.getCode());
            assertEquals(404, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_CATEGORY_ID 에러 코드")
        void shouldReturnCorrectValueForInvalidCategoryId() {
            // Given
            CategoryErrorCode errorCode = CategoryErrorCode.INVALID_CATEGORY_ID;

            // When & Then
            assertEquals("CAT-100", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_CATEGORY_CODE 에러 코드")
        void shouldReturnCorrectValueForInvalidCategoryCode() {
            // Given
            CategoryErrorCode errorCode = CategoryErrorCode.INVALID_CATEGORY_CODE;

            // When & Then
            assertEquals("CAT-101", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_CATEGORY_NAME 에러 코드")
        void shouldReturnCorrectValueForInvalidCategoryName() {
            // Given
            CategoryErrorCode errorCode = CategoryErrorCode.INVALID_CATEGORY_NAME;

            // When & Then
            assertEquals("CAT-102", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_CATEGORY_PATH 에러 코드")
        void shouldReturnCorrectValueForInvalidCategoryPath() {
            // Given
            CategoryErrorCode errorCode = CategoryErrorCode.INVALID_CATEGORY_PATH;

            // When & Then
            assertEquals("CAT-103", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_CATEGORY_DEPTH 에러 코드")
        void shouldReturnCorrectValueForInvalidCategoryDepth() {
            // Given
            CategoryErrorCode errorCode = CategoryErrorCode.INVALID_CATEGORY_DEPTH;

            // When & Then
            assertEquals("CAT-104", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("HTTP 상태 코드 테스트")
    class HttpStatusTest {

        @Test
        @DisplayName("조회 관련 에러는 404 상태 코드")
        void shouldReturnNotFoundStatusForQueryErrors() {
            // When & Then
            assertEquals(404, CategoryErrorCode.CATEGORY_NOT_FOUND.getHttpStatus());
        }

        @Test
        @DisplayName("유효성 관련 에러는 400 상태 코드")
        void shouldReturnBadRequestStatusForValidationErrors() {
            // When & Then
            assertEquals(400, CategoryErrorCode.INVALID_CATEGORY_ID.getHttpStatus());
            assertEquals(400, CategoryErrorCode.INVALID_CATEGORY_CODE.getHttpStatus());
            assertEquals(400, CategoryErrorCode.INVALID_CATEGORY_NAME.getHttpStatus());
            assertEquals(400, CategoryErrorCode.INVALID_CATEGORY_PATH.getHttpStatus());
            assertEquals(400, CategoryErrorCode.INVALID_CATEGORY_DEPTH.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("CategoryErrorCode는 6개의 값을 가진다")
        void shouldHaveSixValues() {
            // When & Then
            assertEquals(6, CategoryErrorCode.values().length);
        }

        @Test
        @DisplayName("문자열로 CategoryErrorCode를 찾을 수 있다")
        void shouldFindByName() {
            // When & Then
            assertEquals(
                    CategoryErrorCode.CATEGORY_NOT_FOUND,
                    CategoryErrorCode.valueOf("CATEGORY_NOT_FOUND"));
            assertEquals(
                    CategoryErrorCode.INVALID_CATEGORY_ID,
                    CategoryErrorCode.valueOf("INVALID_CATEGORY_ID"));
        }

        @Test
        @DisplayName("에러 코드 형식이 CAT-XXX 패턴을 따른다")
        void shouldFollowCodePattern() {
            // When & Then
            for (CategoryErrorCode errorCode : CategoryErrorCode.values()) {
                assertTrue(errorCode.getCode().startsWith("CAT-"));
            }
        }
    }
}
