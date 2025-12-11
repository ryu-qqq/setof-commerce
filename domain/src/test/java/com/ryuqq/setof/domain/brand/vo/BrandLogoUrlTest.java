package com.ryuqq.setof.domain.brand.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.brand.exception.InvalidBrandLogoUrlException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * BrandLogoUrl Value Object 테스트
 *
 * <p>브랜드 로고 URL (선택, null 허용, 최대 500자, http/https로 시작)
 */
@DisplayName("BrandLogoUrl Value Object")
class BrandLogoUrlTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("유효한 HTTPS URL로 BrandLogoUrl 생성")
        void shouldCreateBrandLogoUrlWithHttpsUrl() {
            // Given
            String validUrl = "https://cdn.example.com/logo.png";

            // When
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.of(validUrl);

            // Then
            assertNotNull(brandLogoUrl);
            assertEquals(validUrl, brandLogoUrl.value());
        }

        @Test
        @DisplayName("유효한 HTTP URL로 BrandLogoUrl 생성")
        void shouldCreateBrandLogoUrlWithHttpUrl() {
            // Given
            String validUrl = "http://cdn.example.com/logo.png";

            // When
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.of(validUrl);

            // Then
            assertNotNull(brandLogoUrl);
            assertEquals(validUrl, brandLogoUrl.value());
        }

        @Test
        @DisplayName("null 값으로 BrandLogoUrl 생성 (선택 필드)")
        void shouldCreateBrandLogoUrlWithNull() {
            // Given
            String nullUrl = null;

            // When
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.of(nullUrl);

            // Then
            assertNotNull(brandLogoUrl);
            assertNull(brandLogoUrl.value());
        }

        @Test
        @DisplayName("empty() 팩토리로 빈 BrandLogoUrl 생성")
        void shouldCreateEmptyBrandLogoUrl() {
            // When
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.empty();

            // Then
            assertNotNull(brandLogoUrl);
            assertNull(brandLogoUrl.value());
        }

        @ParameterizedTest
        @DisplayName("다양한 유효한 URL로 생성")
        @ValueSource(
                strings = {
                    "https://cdn.example.com/logo.png",
                    "http://localhost:8080/image.jpg",
                    "https://s3.amazonaws.com/bucket/brand/logo.webp"
                })
        void shouldCreateBrandLogoUrlWithVariousValidUrls(String validUrl) {
            // When
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.of(validUrl);

            // Then
            assertNotNull(brandLogoUrl);
            assertEquals(validUrl, brandLogoUrl.value());
        }

        @Test
        @DisplayName("최대 길이(500자) URL로 생성")
        void shouldCreateBrandLogoUrlWithMaxLength() {
            // Given
            String prefix = "https://example.com/";
            String maxLengthUrl = prefix + "a".repeat(500 - prefix.length());

            // When
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.of(maxLengthUrl);

            // Then
            assertNotNull(brandLogoUrl);
            assertEquals(500, brandLogoUrl.value().length());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("빈 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenUrlIsEmpty() {
            // Given
            String emptyUrl = "";

            // When & Then
            assertThrows(InvalidBrandLogoUrlException.class, () -> BrandLogoUrl.of(emptyUrl));
        }

        @Test
        @DisplayName("공백 문자열로 생성 시 예외 발생")
        void shouldThrowExceptionWhenUrlIsBlank() {
            // Given
            String blankUrl = "   ";

            // When & Then
            assertThrows(InvalidBrandLogoUrlException.class, () -> BrandLogoUrl.of(blankUrl));
        }

        @Test
        @DisplayName("500자 초과 URL로 생성 시 예외 발생")
        void shouldThrowExceptionWhenUrlExceedsMaxLength() {
            // Given
            String prefix = "https://example.com/";
            String tooLongUrl = prefix + "a".repeat(501 - prefix.length());

            // When & Then
            assertThrows(InvalidBrandLogoUrlException.class, () -> BrandLogoUrl.of(tooLongUrl));
        }

        @ParameterizedTest
        @DisplayName("http/https로 시작하지 않는 URL로 생성 시 예외 발생")
        @ValueSource(
                strings = {
                    "ftp://example.com/logo.png",
                    "file:///path/logo.png",
                    "//cdn.example.com/logo.png"
                })
        void shouldThrowExceptionWhenUrlHasInvalidProtocol(String invalidUrl) {
            // When & Then
            assertThrows(InvalidBrandLogoUrlException.class, () -> BrandLogoUrl.of(invalidUrl));
        }
    }

    @Nested
    @DisplayName("hasValue() 메서드")
    class HasValueTest {

        @Test
        @DisplayName("값이 있으면 true 반환")
        void shouldReturnTrueWhenHasValue() {
            // Given
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.of("https://cdn.example.com/logo.png");

            // When & Then
            assertTrue(brandLogoUrl.hasValue());
        }

        @Test
        @DisplayName("값이 null이면 false 반환")
        void shouldReturnFalseWhenNull() {
            // Given
            BrandLogoUrl brandLogoUrl = BrandLogoUrl.empty();

            // When & Then
            assertFalse(brandLogoUrl.hasValue());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BrandLogoUrl는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            BrandLogoUrl url1 = BrandLogoUrl.of("https://cdn.example.com/logo.png");
            BrandLogoUrl url2 = BrandLogoUrl.of("https://cdn.example.com/logo.png");

            // When & Then
            assertEquals(url1, url2);
            assertEquals(url1.hashCode(), url2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BrandLogoUrl는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            BrandLogoUrl url1 = BrandLogoUrl.of("https://cdn.example.com/logo1.png");
            BrandLogoUrl url2 = BrandLogoUrl.of("https://cdn.example.com/logo2.png");

            // When & Then
            assertNotEquals(url1, url2);
        }

        @Test
        @DisplayName("빈 BrandLogoUrl는 서로 동등하다")
        void shouldBeEqualWhenBothEmpty() {
            // Given
            BrandLogoUrl empty1 = BrandLogoUrl.empty();
            BrandLogoUrl empty2 = BrandLogoUrl.empty();

            // When & Then
            assertEquals(empty1, empty2);
        }
    }
}
