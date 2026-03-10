package com.ryuqq.setof.domain.review.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@DisplayName("Rating Value Object 테스트")
class RatingTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("최솟값 1.0으로 Rating을 생성한다")
        void createWithMinValue() {
            // when
            Rating rating = Rating.of(1.0);

            // then
            assertThat(rating.value()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("최댓값 5.0으로 Rating을 생성한다")
        void createWithMaxValue() {
            // when
            Rating rating = Rating.of(5.0);

            // then
            assertThat(rating.value()).isEqualTo(5.0);
        }

        @ParameterizedTest
        @ValueSource(doubles = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0})
        @DisplayName("0.5 단위의 유효한 평점으로 Rating을 생성한다")
        void createWithValidIncrements(double value) {
            // when
            Rating rating = Rating.of(value);

            // then
            assertThat(rating.value()).isEqualTo(value);
        }

        @Test
        @DisplayName("1.0 미만이면 예외가 발생한다")
        void belowMinValueThrowsException() {
            // when & then
            assertThatThrownBy(() -> Rating.of(0.5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("between 1.0 and 5.0");
        }

        @Test
        @DisplayName("5.0 초과면 예외가 발생한다")
        void aboveMaxValueThrowsException() {
            // when & then
            assertThatThrownBy(() -> Rating.of(5.5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("between 1.0 and 5.0");
        }

        @ParameterizedTest
        @ValueSource(doubles = {1.1, 1.3, 2.4, 3.7, 4.9})
        @DisplayName("0.5 단위가 아닌 값이면 예외가 발생한다")
        void notIncrementOfHalfThrowsException(double value) {
            // when & then
            assertThatThrownBy(() -> Rating.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0.5 increments");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 Rating은 동등하다")
        void sameValueEquals() {
            // given
            Rating rating1 = Rating.of(4.5);
            Rating rating2 = Rating.of(4.5);

            // then
            assertThat(rating1).isEqualTo(rating2);
            assertThat(rating1.hashCode()).isEqualTo(rating2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 Rating은 동등하지 않다")
        void differentValueNotEquals() {
            // given
            Rating rating1 = Rating.of(4.0);
            Rating rating2 = Rating.of(4.5);

            // then
            assertThat(rating1).isNotEqualTo(rating2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("Rating은 record이므로 불변이다")
        void ratingIsImmutable() {
            // given
            Rating rating = Rating.of(3.0);

            // when
            Rating anotherRating = Rating.of(3.0);

            // then
            assertThat(rating.value()).isEqualTo(3.0);
            assertThat(anotherRating.value()).isEqualTo(3.0);
            assertThat(rating).isEqualTo(anotherRating);
        }
    }
}
