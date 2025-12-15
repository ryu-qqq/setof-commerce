package com.ryuqq.setof.domain.productdescription.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productdescription.exception.InvalidHtmlContentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HtmlContent Value Object 테스트")
class HtmlContentTest {

    @Nested
    @DisplayName("생성 테스트")
    class Creation {

        @Test
        @DisplayName("유효한 HTML로 생성한다")
        void shouldCreateWithValidHtml() {
            // given
            String html = "<p>테스트 컨텐츠</p>";

            // when
            HtmlContent content = HtmlContent.of(html);

            // then
            assertThat(content.value()).isEqualTo(html);
        }

        @Test
        @DisplayName("빈 HTML로 생성한다")
        void shouldCreateWithEmptyHtml() {
            // when
            HtmlContent content = HtmlContent.empty();

            // then
            assertThat(content.value()).isEmpty();
            assertThat(content.hasContent()).isFalse();
        }

        @Test
        @DisplayName("null HTML은 예외를 발생시킨다")
        void shouldThrowWhenHtmlIsNull() {
            // when & then
            assertThatThrownBy(() -> HtmlContent.of(null))
                    .isInstanceOf(InvalidHtmlContentException.class);
        }

        @Test
        @DisplayName("최대 길이 초과 시 예외를 발생시킨다")
        void shouldThrowWhenExceedsMaxLength() {
            // given
            String longHtml = "a".repeat(100_001);

            // when & then
            assertThatThrownBy(() -> HtmlContent.of(longHtml))
                    .isInstanceOf(InvalidHtmlContentException.class)
                    .hasMessageContaining("100000");
        }
    }

    @Nested
    @DisplayName("컨텐츠 확인")
    class ContentCheck {

        @Test
        @DisplayName("hasContent는 내용이 있으면 true를 반환한다")
        void hasContentShouldReturnTrue() {
            // given
            HtmlContent content = HtmlContent.of("<p>내용</p>");

            // then
            assertThat(content.hasContent()).isTrue();
        }

        @Test
        @DisplayName("hasContent는 빈 내용이면 false를 반환한다")
        void hasContentShouldReturnFalse() {
            // given
            HtmlContent content = HtmlContent.empty();

            // then
            assertThat(content.hasContent()).isFalse();
        }

        @Test
        @DisplayName("containsImages는 img 태그가 있으면 true를 반환한다")
        void containsImagesShouldReturnTrue() {
            // given
            HtmlContent content = HtmlContent.of("<img src='test.jpg'/>");

            // then
            assertThat(content.containsImages()).isTrue();
        }

        @Test
        @DisplayName("length는 HTML 길이를 반환한다")
        void lengthShouldReturnCorrectLength() {
            // given
            String html = "<p>테스트</p>";
            HtmlContent content = HtmlContent.of(html);

            // then
            assertThat(content.length()).isEqualTo(html.length());
        }
    }

    @Nested
    @DisplayName("URL 치환")
    class UrlReplacement {

        @Test
        @DisplayName("원본 URL을 CDN URL로 치환한다")
        void shouldReplaceUrl() {
            // given
            String originUrl = "https://origin.com/image.jpg";
            String cdnUrl = "https://cdn.com/image.jpg";
            HtmlContent content = HtmlContent.of("<img src='" + originUrl + "'/>");

            // when
            HtmlContent replaced = content.replaceUrl(originUrl, cdnUrl);

            // then
            assertThat(replaced.value()).contains(cdnUrl);
            assertThat(replaced.value()).doesNotContain(originUrl);
        }

        @Test
        @DisplayName("null 파라미터는 원본을 반환한다")
        void shouldReturnOriginalWhenParamIsNull() {
            // given
            HtmlContent content = HtmlContent.of("<p>테스트</p>");

            // when
            HtmlContent result = content.replaceUrl(null, "https://cdn.com/image.jpg");

            // then
            assertThat(result).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class Equality {

        @Test
        @DisplayName("같은 내용의 HtmlContent는 동등하다")
        void shouldBeEqualForSameContent() {
            // given
            HtmlContent content1 = HtmlContent.of("<p>테스트</p>");
            HtmlContent content2 = HtmlContent.of("<p>테스트</p>");

            // then
            assertThat(content1).isEqualTo(content2);
            assertThat(content1.hashCode()).isEqualTo(content2.hashCode());
        }
    }
}
