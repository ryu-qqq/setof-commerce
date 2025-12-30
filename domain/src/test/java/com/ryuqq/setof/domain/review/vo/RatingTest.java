package com.ryuqq.setof.domain.review.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.review.exception.InvalidRatingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Rating Value Object 테스트
 *
 * <p>평점 값 객체의 유효성 검증 로직을 테스트합니다.
 */
@DisplayName("Rating Value Object")
class RatingTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5})
        @DisplayName("1~5 범위의 평점을 생성할 수 있다")
        void shouldCreateRatingInValidRange(int value) {
            // when
            Rating rating = Rating.of(value);

            // then
            assertEquals(value, rating.getValue());
        }

        @Test
        @DisplayName("0 이하의 평점은 예외가 발생한다")
        void shouldThrowExceptionWhenRatingIsTooLow() {
            // when & then
            assertThrows(InvalidRatingException.class, () -> Rating.of(0));
            assertThrows(InvalidRatingException.class, () -> Rating.of(-1));
        }

        @Test
        @DisplayName("6 이상의 평점은 예외가 발생한다")
        void shouldThrowExceptionWhenRatingIsTooHigh() {
            // when & then
            assertThrows(InvalidRatingException.class, () -> Rating.of(6));
            assertThrows(InvalidRatingException.class, () -> Rating.of(100));
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isHighRating()은 4점 이상일 때 true를 반환한다")
        void shouldReturnTrueWhenHighRating() {
            // then
            assertFalse(Rating.of(3).isHighRating());
            assertTrue(Rating.of(4).isHighRating());
            assertTrue(Rating.of(5).isHighRating());
        }

        @Test
        @DisplayName("isLowRating()은 2점 이하일 때 true를 반환한다")
        void shouldReturnTrueWhenLowRating() {
            // then
            assertTrue(Rating.of(1).isLowRating());
            assertTrue(Rating.of(2).isLowRating());
            assertFalse(Rating.of(3).isLowRating());
        }

        @Test
        @DisplayName("getValue()는 평점 값을 반환한다")
        void shouldReturnRatingValue() {
            // given
            Rating rating = Rating.of(4);

            // then
            assertEquals(4, rating.getValue());
        }
    }

    @Nested
    @DisplayName("equals & hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("같은 값을 가진 Rating은 동등하다")
        void shouldBeEqualWhenSameValue() {
            // given
            Rating rating1 = Rating.of(4);
            Rating rating2 = Rating.of(4);

            // then
            assertEquals(rating1, rating2);
            assertEquals(rating1.hashCode(), rating2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 Rating은 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // given
            Rating rating1 = Rating.of(4);
            Rating rating2 = Rating.of(5);

            // then
            assertFalse(rating1.equals(rating2));
        }
    }
}
