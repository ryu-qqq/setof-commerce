package com.ryuqq.setof.domain.brand.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** BrandErrorCode Enum 테스트 */
@DisplayName("BrandErrorCode Enum 테스트")
class BrandErrorCodeTest {

    @Nested
    @DisplayName("조회 관련 에러 코드")
    class QueryErrorCodes {

        @Test
        @DisplayName("BRAND_NOT_FOUND 에러 코드 속성 검증")
        void shouldHaveCorrectBrandNotFoundProperties() {
            // Given
            BrandErrorCode errorCode = BrandErrorCode.BRAND_NOT_FOUND;

            // Then
            assertEquals("BRD-001", errorCode.getCode());
            assertEquals(404, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("유효성 관련 에러 코드")
    class ValidationErrorCodes {

        @Test
        @DisplayName("INVALID_BRAND_ID 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidBrandIdProperties() {
            // Given
            BrandErrorCode errorCode = BrandErrorCode.INVALID_BRAND_ID;

            // Then
            assertEquals("BRD-100", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_BRAND_CODE 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidBrandCodeProperties() {
            // Given
            BrandErrorCode errorCode = BrandErrorCode.INVALID_BRAND_CODE;

            // Then
            assertEquals("BRD-101", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_BRAND_NAME_KO 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidBrandNameKoProperties() {
            // Given
            BrandErrorCode errorCode = BrandErrorCode.INVALID_BRAND_NAME_KO;

            // Then
            assertEquals("BRD-102", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_BRAND_NAME_EN 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidBrandNameEnProperties() {
            // Given
            BrandErrorCode errorCode = BrandErrorCode.INVALID_BRAND_NAME_EN;

            // Then
            assertEquals("BRD-103", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }

        @Test
        @DisplayName("INVALID_BRAND_LOGO_URL 에러 코드 속성 검증")
        void shouldHaveCorrectInvalidBrandLogoUrlProperties() {
            // Given
            BrandErrorCode errorCode = BrandErrorCode.INVALID_BRAND_LOGO_URL;

            // Then
            assertEquals("BRD-104", errorCode.getCode());
            assertEquals(400, errorCode.getHttpStatus());
            assertNotNull(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("BrandErrorCode는 6개의 값을 가진다")
        void shouldHaveSixValues() {
            // When & Then
            assertEquals(6, BrandErrorCode.values().length);
        }
    }
}
