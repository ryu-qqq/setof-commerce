package com.ryuqq.setof.domain.review.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.review.exception.InvalidReviewContentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ReviewContent Value Object 테스트
 *
 * <p>리뷰 내용 값 객체의 유효성 검증 로직을 테스트합니다.
 */
@DisplayName("ReviewContent Value Object")
class ReviewContentTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("유효한 내용으로 생성할 수 있다")
        void shouldCreateWithValidContent() {
            // given
            String content = "좋은 상품입니다. 배송도 빠르고 품질도 좋아요!";

            // when
            ReviewContent reviewContent = ReviewContent.of(content);

            // then
            assertEquals(content, reviewContent.getValue());
            assertFalse(reviewContent.isEmpty());
        }

        @Test
        @DisplayName("500자까지 작성할 수 있다")
        void shouldCreateWithMaxLength() {
            // given
            String content = "가".repeat(500);

            // when
            ReviewContent reviewContent = ReviewContent.of(content);

            // then
            assertEquals(500, reviewContent.length());
        }

        @Test
        @DisplayName("500자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenExceedsMaxLength() {
            // given
            String content = "가".repeat(501);

            // when & then
            assertThrows(InvalidReviewContentException.class, () -> ReviewContent.of(content));
        }
    }

    @Nested
    @DisplayName("empty() - 빈 내용 생성")
    class Empty {

        @Test
        @DisplayName("빈 리뷰 내용을 생성할 수 있다")
        void shouldCreateEmptyContent() {
            // when
            ReviewContent content = ReviewContent.empty();

            // then
            assertTrue(content.isEmpty());
            assertEquals(0, content.length());
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isEmpty()는 빈 내용일 때 true를 반환한다")
        void shouldReturnTrueWhenEmpty() {
            // given
            ReviewContent emptyContent = ReviewContent.empty();
            ReviewContent blankContent = ReviewContent.of("");

            // then
            assertTrue(emptyContent.isEmpty());
            assertTrue(blankContent.isEmpty());
        }

        @Test
        @DisplayName("isEmpty()는 내용이 있을 때 false를 반환한다")
        void shouldReturnFalseWhenHasContent() {
            // given
            ReviewContent content = ReviewContent.of("내용이 있습니다.");

            // then
            assertFalse(content.isEmpty());
        }

        @Test
        @DisplayName("length()는 내용의 길이를 반환한다")
        void shouldReturnContentLength() {
            // given
            ReviewContent content = ReviewContent.of("12345");

            // then
            assertEquals(5, content.length());
        }
    }

    @Nested
    @DisplayName("equals & hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("같은 내용을 가진 ReviewContent는 동등하다")
        void shouldBeEqualWhenSameContent() {
            // given
            ReviewContent content1 = ReviewContent.of("같은 내용");
            ReviewContent content2 = ReviewContent.of("같은 내용");

            // then
            assertEquals(content1, content2);
            assertEquals(content1.hashCode(), content2.hashCode());
        }

        @Test
        @DisplayName("다른 내용을 가진 ReviewContent는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentContent() {
            // given
            ReviewContent content1 = ReviewContent.of("내용1");
            ReviewContent content2 = ReviewContent.of("내용2");

            // then
            assertFalse(content1.equals(content2));
        }
    }
}
