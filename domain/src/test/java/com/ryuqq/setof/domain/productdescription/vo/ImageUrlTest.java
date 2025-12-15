package com.ryuqq.setof.domain.productdescription.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productdescription.exception.InvalidImageUrlException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ImageUrl Value Object 테스트")
class ImageUrlTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @Test
        @DisplayName("유효한 HTTP URL로 생성한다")
        void shouldCreateWithValidHttpUrl() {
            // given
            String url = "http://example.com/image.jpg";

            // when
            ImageUrl imageUrl = ImageUrl.of(url);

            // then
            assertThat(imageUrl.value()).isEqualTo(url);
        }

        @Test
        @DisplayName("유효한 HTTPS URL로 생성한다")
        void shouldCreateWithValidHttpsUrl() {
            // given
            String url = "https://example.com/image.jpg";

            // when
            ImageUrl imageUrl = ImageUrl.of(url);

            // then
            assertThat(imageUrl.value()).isEqualTo(url);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("null이거나 빈 URL은 예외를 발생시킨다")
        void shouldThrowWhenUrlIsNullOrBlank(String url) {
            // when & then
            assertThatThrownBy(() -> ImageUrl.of(url)).isInstanceOf(InvalidImageUrlException.class);
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "ftp://example.com/file.jpg",
                    "file:///path/to/file.jpg",
                    "example.com/image.jpg"
                })
        @DisplayName("HTTP/HTTPS 프로토콜이 아니면 예외를 발생시킨다")
        void shouldThrowWhenProtocolIsInvalid(String url) {
            // when & then
            assertThatThrownBy(() -> ImageUrl.of(url))
                    .isInstanceOf(InvalidImageUrlException.class)
                    .hasMessageContaining("http://");
        }
    }

    @Nested
    @DisplayName("CDN URL 확인")
    class CdnCheck {

        @ParameterizedTest
        @ValueSource(
                strings = {
                    "https://cdn.example.com/image.jpg",
                    "https://d1234.cloudfront.net/image.jpg"
                })
        @DisplayName("CDN URL이면 true를 반환한다")
        void shouldReturnTrueForCdnUrl(String url) {
            // given
            ImageUrl imageUrl = ImageUrl.of(url);

            // then
            assertThat(imageUrl.isCdnUrl()).isTrue();
        }

        @Test
        @DisplayName("CDN URL이 아니면 false를 반환한다")
        void shouldReturnFalseForNonCdnUrl() {
            // given
            ImageUrl imageUrl = ImageUrl.of("https://example.com/image.jpg");

            // then
            assertThat(imageUrl.isCdnUrl()).isFalse();
        }
    }

    @Nested
    @DisplayName("확장자 추출")
    class ExtensionExtraction {

        @Test
        @DisplayName("파일 확장자를 추출한다")
        void shouldExtractExtension() {
            // given
            ImageUrl imageUrl = ImageUrl.of("https://example.com/image.jpg");

            // then
            assertThat(imageUrl.extractExtension()).isEqualTo("jpg");
        }

        @Test
        @DisplayName("쿼리 파라미터가 있어도 확장자를 추출한다")
        void shouldExtractExtensionWithQueryParams() {
            // given
            ImageUrl imageUrl = ImageUrl.of("https://example.com/image.png?size=large");

            // then
            assertThat(imageUrl.extractExtension()).isEqualTo("png");
        }

        @Test
        @DisplayName("확장자가 없으면 빈 문자열을 반환한다")
        void shouldReturnEmptyWhenNoExtension() {
            // given
            ImageUrl imageUrl = ImageUrl.of("https://example.com/image");

            // then
            assertThat(imageUrl.extractExtension()).isEmpty();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 URL을 가진 ImageUrl은 동등하다")
        void shouldBeEqualForSameUrl() {
            // given
            ImageUrl url1 = ImageUrl.of("https://example.com/image.jpg");
            ImageUrl url2 = ImageUrl.of("https://example.com/image.jpg");

            // then
            assertThat(url1).isEqualTo(url2);
            assertThat(url1.hashCode()).isEqualTo(url2.hashCode());
        }
    }
}
