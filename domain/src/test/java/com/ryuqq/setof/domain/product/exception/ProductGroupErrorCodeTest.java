package com.ryuqq.setof.domain.product.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** ProductGroupErrorCode Enum 테스트 */
@DisplayName("ProductGroupErrorCode Enum 테스트")
class ProductGroupErrorCodeTest {

    @Nested
    @DisplayName("조회 관련 에러 코드")
    class QueryErrorCodes {

        @Test
        @DisplayName("PRODUCT_GROUP_NOT_FOUND 에러 코드 속성 검증")
        void shouldHaveCorrectProductGroupNotFoundProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND;

            // Then
            assertEquals("PRODUCT-001", errorCode.getCode());
            assertEquals(404, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("PRODUCT_NOT_FOUND 에러 코드 속성 검증")
        void shouldHaveCorrectProductNotFoundProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.PRODUCT_NOT_FOUND;

            // Then
            assertEquals("PRODUCT-002", errorCode.getCode());
            assertEquals(404, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("유효성 관련 에러 코드")
    class ValidationErrorCodes {

        @Test
        @DisplayName("INVALID_PRODUCT_GROUP_ID 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidProductGroupIdProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.INVALID_PRODUCT_GROUP_ID;

            // Then
            assertEquals("PRODUCT-003", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_PRODUCT_ID 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidProductIdProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.INVALID_PRODUCT_ID;

            // Then
            assertEquals("PRODUCT-004", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_PRODUCT_GROUP_NAME 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidProductGroupNameProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.INVALID_PRODUCT_GROUP_NAME;

            // Then
            assertEquals("PRODUCT-005", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_PRICE 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidPriceProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.INVALID_PRICE;

            // Then
            assertEquals("PRODUCT-006", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_MONEY 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidMoneyProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.INVALID_MONEY;

            // Then
            assertEquals("PRODUCT-007", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_OPTION_CONFIGURATION 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidOptionConfigurationProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.INVALID_OPTION_CONFIGURATION;

            // Then
            assertEquals("PRODUCT-010", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("상태 관련 에러 코드")
    class StatusErrorCodes {

        @Test
        @DisplayName("PRODUCT_GROUP_ALREADY_DELETED 에러 코드 속성 검증")
        void shouldHaveCorrectProductGroupAlreadyDeletedProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.PRODUCT_GROUP_ALREADY_DELETED;

            // Then
            assertEquals("PRODUCT-008", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("PRODUCT_GROUP_NOT_EDITABLE 에러 코드 속성 검증")
        void shouldHaveCorrectProductGroupNotEditableProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.PRODUCT_GROUP_NOT_EDITABLE;

            // Then
            assertEquals("PRODUCT-009", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("PRODUCT_NOT_BELONG_TO_GROUP 에러 코드 속성 검증")
        void shouldHaveCorrectProductNotBelongToGroupProperties() {
            // Given
            ProductGroupErrorCode errorCode = ProductGroupErrorCode.PRODUCT_NOT_BELONG_TO_GROUP;

            // Then
            assertEquals("PRODUCT-011", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("ProductGroupErrorCode는 11개의 값을 가진다")
        void shouldHaveElevenValues() {
            // When & Then
            assertEquals(11, ProductGroupErrorCode.values().length);
        }
    }
}
