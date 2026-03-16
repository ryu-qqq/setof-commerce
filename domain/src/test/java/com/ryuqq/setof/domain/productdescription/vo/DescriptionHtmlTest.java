package com.ryuqq.setof.domain.productdescription.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DescriptionHtml VO 테스트")
class DescriptionHtmlTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 HTML 컨텐츠를 생성한다")
        void createValidHtml() {
            // when
            var html = DescriptionHtml.of("<p>상품 상세설명입니다</p>");

            // then
            assertThat(html.value()).isEqualTo("<p>상품 상세설명입니다</p>");
        }

        @Test
        @DisplayName("null 값을 허용한다")
        void allowsNullValue() {
            // when
            var html = DescriptionHtml.of(null);

            // then
            assertThat(html.value()).isNull();
        }
    }

    @Nested
    @DisplayName("empty() - 빈 인스턴스 생성")
    class EmptyTest {

        @Test
        @DisplayName("빈 인스턴스는 null 값을 가진다")
        void emptyHasNullValue() {
            // when
            var html = DescriptionHtml.empty();

            // then
            assertThat(html.value()).isNull();
        }

        @Test
        @DisplayName("빈 인스턴스는 isEmpty()가 true이다")
        void emptyIsEmpty() {
            // when
            var html = DescriptionHtml.empty();

            // then
            assertThat(html.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("isEmpty() - 비어있는지 확인")
    class IsEmptyTest {

        @Test
        @DisplayName("null 값이면 isEmpty()는 true를 반환한다")
        void nullValueIsEmpty() {
            // given
            var html = DescriptionHtml.of(null);

            // then
            assertThat(html.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("공백 문자열은 null로 정규화되어 isEmpty()가 true이다")
        void blankValueIsEmpty() {
            // given
            var html = DescriptionHtml.of("   ");

            // then
            assertThat(html.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("유효한 값이면 isEmpty()는 false를 반환한다")
        void validValueIsNotEmpty() {
            // given
            var html = DescriptionHtml.of("<p>내용</p>");

            // then
            assertThat(html.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("트림 동작")
    class TrimTest {

        @Test
        @DisplayName("앞뒤 공백이 트림된다")
        void trimWhitespace() {
            // when
            var html = DescriptionHtml.of("  <p>내용</p>  ");

            // then
            assertThat(html.value()).isEqualTo("<p>내용</p>");
        }

        @Test
        @DisplayName("빈 문자열은 trim 후 null로 정규화된다")
        void emptyStringBecomesNull() {
            // when
            var html = DescriptionHtml.of("");

            // then
            assertThat(html.value()).isNull();
        }

        @Test
        @DisplayName("공백만 있는 문자열은 trim 후 null로 정규화된다")
        void whitespaceOnlyBecomesNull() {
            // when
            var html = DescriptionHtml.of("    ");

            // then
            assertThat(html.value()).isNull();
        }
    }

    @Nested
    @DisplayName("최대 길이 초과")
    class MaxLengthTest {

        @Test
        @DisplayName("500000자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            // given
            String longContent = "가".repeat(500_001);

            // when & then
            assertThatThrownBy(() -> DescriptionHtml.of(longContent))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("500000");
        }

        @Test
        @DisplayName("100000자는 허용된다")
        void allowsMaxLength() {
            // given
            String maxContent = "가".repeat(100_000);

            // when
            var html = DescriptionHtml.of(maxContent);

            // then
            assertThat(html.value()).hasSize(100_000);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 DescriptionHtml은 동등하다")
        void equalValues() {
            // given
            var html1 = DescriptionHtml.of("<p>내용</p>");
            var html2 = DescriptionHtml.of("<p>내용</p>");

            // then
            assertThat(html1).isEqualTo(html2);
        }

        @Test
        @DisplayName("null 값의 DescriptionHtml은 서로 동등하다")
        void nullValuesAreEqual() {
            // given
            var html1 = DescriptionHtml.of(null);
            var html2 = DescriptionHtml.empty();

            // then
            assertThat(html1).isEqualTo(html2);
        }

        @Test
        @DisplayName("다른 값의 DescriptionHtml은 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            var html1 = DescriptionHtml.of("<p>내용1</p>");
            var html2 = DescriptionHtml.of("<p>내용2</p>");

            // then
            assertThat(html1).isNotEqualTo(html2);
        }
    }
}
