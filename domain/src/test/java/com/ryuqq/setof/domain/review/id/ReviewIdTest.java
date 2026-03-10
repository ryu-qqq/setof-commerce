package com.ryuqq.setof.domain.review.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewId Value Object 테스트")
class ReviewIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 ReviewId를 생성한다")
        void createWithOf() {
            // when
            ReviewId reviewId = ReviewId.of(1L);

            // then
            assertThat(reviewId.value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외를 발생시킨다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> ReviewId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }

        @Test
        @DisplayName("forNew()로 새로운 리뷰용 ID를 생성한다")
        void createWithForNew() {
            // when
            ReviewId reviewId = ReviewId.forNew();

            // then
            assertThat(reviewId.value()).isNull();
            assertThat(reviewId.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenValueIsNull() {
            // given
            ReviewId reviewId = ReviewId.forNew();

            // then
            assertThat(reviewId.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            ReviewId reviewId = ReviewId.of(1L);

            // then
            assertThat(reviewId.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ReviewId는 동등하다")
        void sameValueEquals() {
            // given
            ReviewId reviewId1 = ReviewId.of(100L);
            ReviewId reviewId2 = ReviewId.of(100L);

            // then
            assertThat(reviewId1).isEqualTo(reviewId2);
            assertThat(reviewId1.hashCode()).isEqualTo(reviewId2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ReviewId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ReviewId reviewId1 = ReviewId.of(100L);
            ReviewId reviewId2 = ReviewId.of(200L);

            // then
            assertThat(reviewId1).isNotEqualTo(reviewId2);
        }
    }
}
