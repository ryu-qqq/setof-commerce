package com.ryuqq.setof.domain.review.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewContent Value Object 테스트")
class ReviewContentTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 내용으로 ReviewContent를 생성한다")
        void createWithValidValue() {
            // when
            ReviewContent content = ReviewContent.of("좋은 상품입니다.");

            // then
            assertThat(content.value()).isEqualTo("좋은 상품입니다.");
        }

        @Test
        @DisplayName("null 값으로 ReviewContent를 생성한다")
        void createWithNullValue() {
            // when
            ReviewContent content = ReviewContent.of(null);

            // then
            assertThat(content.value()).isNull();
        }

        @Test
        @DisplayName("빈 문자열로 ReviewContent를 생성한다")
        void createWithEmptyString() {
            // when
            ReviewContent content = ReviewContent.of("");

            // then
            assertThat(content.value()).isEmpty();
        }

        @Test
        @DisplayName("500자 이하의 내용으로 생성한다")
        void createWithMaxLengthContent() {
            // given
            String maxContent = "a".repeat(500);

            // when & then
            assertThatCode(() -> ReviewContent.of(maxContent)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("500자를 초과하면 예외가 발생한다")
        void createWithTooLongContentThrowsException() {
            // given
            String tooLongContent = "a".repeat(501);

            // when & then
            assertThatThrownBy(() -> ReviewContent.of(tooLongContent))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("500");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ReviewContent는 동등하다")
        void sameValueEquals() {
            // given
            ReviewContent content1 = ReviewContent.of("좋은 상품입니다.");
            ReviewContent content2 = ReviewContent.of("좋은 상품입니다.");

            // then
            assertThat(content1).isEqualTo(content2);
            assertThat(content1.hashCode()).isEqualTo(content2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ReviewContent는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ReviewContent content1 = ReviewContent.of("좋습니다.");
            ReviewContent content2 = ReviewContent.of("보통입니다.");

            // then
            assertThat(content1).isNotEqualTo(content2);
        }

        @Test
        @DisplayName("null 값을 가진 ReviewContent 두 개는 동등하다")
        void nullValueEquals() {
            // given
            ReviewContent content1 = ReviewContent.of(null);
            ReviewContent content2 = ReviewContent.of(null);

            // then
            assertThat(content1).isEqualTo(content2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("ReviewContent는 record이므로 불변이다")
        void reviewContentIsImmutable() {
            // given
            ReviewContent content = ReviewContent.of("원본 내용");

            // then
            assertThat(content.value()).isEqualTo("원본 내용");
        }
    }
}
