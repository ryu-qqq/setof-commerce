package com.ryuqq.setof.application.review.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.port.out.query.ReviewQueryPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.ReviewNotFoundException;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewReadManager 단위 테스트")
class ReviewReadManagerTest {

    @InjectMocks private ReviewReadManager sut;

    @Mock private ReviewQueryPort reviewQueryPort;

    @Mock private ProductGroupReviewSearchCriteria mockProductGroupCriteria;
    @Mock private MyReviewSearchCriteria mockMyReviewCriteria;
    @Mock private WrittenReview mockWrittenReview;
    @Mock private Review mockReview;

    @Nested
    @DisplayName("fetchProductGroupReviews() - 상품그룹 리뷰 목록 조회")
    class FetchProductGroupReviewsTest {

        @Test
        @DisplayName("상품그룹 리뷰 목록을 조회한다")
        void fetchProductGroupReviews_ValidCriteria_ReturnsList() {
            // given
            List<WrittenReview> expected = List.of(mockWrittenReview);
            given(reviewQueryPort.fetchProductGroupReviews(mockProductGroupCriteria))
                    .willReturn(expected);

            // when
            List<WrittenReview> result = sut.fetchProductGroupReviews(mockProductGroupCriteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(reviewQueryPort).should().fetchProductGroupReviews(mockProductGroupCriteria);
        }

        @Test
        @DisplayName("상품그룹 리뷰가 없으면 빈 목록을 반환한다")
        void fetchProductGroupReviews_NoReviews_ReturnsEmptyList() {
            // given
            given(reviewQueryPort.fetchProductGroupReviews(mockProductGroupCriteria))
                    .willReturn(List.of());

            // when
            List<WrittenReview> result = sut.fetchProductGroupReviews(mockProductGroupCriteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countProductGroupReviews() - 상품그룹 리뷰 수 조회")
    class CountProductGroupReviewsTest {

        @Test
        @DisplayName("상품그룹의 리뷰 수를 반환한다")
        void countProductGroupReviews_ReturnsCount() {
            // given
            long productGroupId = 500L;
            long expectedCount = 10L;
            given(reviewQueryPort.countProductGroupReviews(productGroupId))
                    .willReturn(expectedCount);

            // when
            long result = sut.countProductGroupReviews(productGroupId);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(reviewQueryPort).should().countProductGroupReviews(productGroupId);
        }

        @Test
        @DisplayName("리뷰가 없으면 0을 반환한다")
        void countProductGroupReviews_NoReviews_ReturnsZero() {
            // given
            long productGroupId = 999L;
            given(reviewQueryPort.countProductGroupReviews(productGroupId)).willReturn(0L);

            // when
            long result = sut.countProductGroupReviews(productGroupId);

            // then
            assertThat(result).isZero();
        }
    }

    @Nested
    @DisplayName("fetchAverageRating() - 평균 평점 조회")
    class FetchAverageRatingTest {

        @Test
        @DisplayName("상품그룹의 평균 평점을 반환한다")
        void fetchAverageRating_ReturnsRating() {
            // given
            long productGroupId = 500L;
            given(reviewQueryPort.fetchAverageRating(productGroupId)).willReturn(Optional.of(4.2));

            // when
            Optional<Double> result = sut.fetchAverageRating(productGroupId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(4.2);
            then(reviewQueryPort).should().fetchAverageRating(productGroupId);
        }

        @Test
        @DisplayName("리뷰가 없으면 Optional.empty()를 반환한다")
        void fetchAverageRating_NoReviews_ReturnsEmpty() {
            // given
            long productGroupId = 999L;
            given(reviewQueryPort.fetchAverageRating(productGroupId)).willReturn(Optional.empty());

            // when
            Optional<Double> result = sut.fetchAverageRating(productGroupId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("fetchMyReviews() - 내 리뷰 목록 조회")
    class FetchMyReviewsTest {

        @Test
        @DisplayName("내 리뷰 목록을 조회한다")
        void fetchMyReviews_ValidCriteria_ReturnsList() {
            // given
            List<WrittenReview> expected = List.of(mockWrittenReview);
            given(reviewQueryPort.fetchMyReviews(mockMyReviewCriteria)).willReturn(expected);

            // when
            List<WrittenReview> result = sut.fetchMyReviews(mockMyReviewCriteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(reviewQueryPort).should().fetchMyReviews(mockMyReviewCriteria);
        }

        @Test
        @DisplayName("내 리뷰가 없으면 빈 목록을 반환한다")
        void fetchMyReviews_NoReviews_ReturnsEmptyList() {
            // given
            given(reviewQueryPort.fetchMyReviews(mockMyReviewCriteria)).willReturn(List.of());

            // when
            List<WrittenReview> result = sut.fetchMyReviews(mockMyReviewCriteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countMyReviews() - 내 리뷰 수 조회")
    class CountMyReviewsTest {

        @Test
        @DisplayName("내 리뷰 수를 반환한다")
        void countMyReviews_ReturnsCount() {
            // given
            long expectedCount = 5L;
            given(reviewQueryPort.countMyReviews(mockMyReviewCriteria)).willReturn(expectedCount);

            // when
            long result = sut.countMyReviews(mockMyReviewCriteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(reviewQueryPort).should().countMyReviews(mockMyReviewCriteria);
        }
    }

    @Nested
    @DisplayName("existsActiveReviewByOrderAndUser() - 주문+회원으로 활성 리뷰 존재 확인")
    class ExistsActiveReviewByOrderAndUserTest {

        @Test
        @DisplayName("활성 리뷰가 존재하면 true를 반환한다")
        void existsActiveReviewByOrderAndUser_ExistsReview_ReturnsTrue() {
            // given
            long orderId = 700L;
            long userId = 100L;
            given(reviewQueryPort.existsActiveReviewByOrderAndUser(orderId, userId))
                    .willReturn(true);

            // when
            boolean result = sut.existsActiveReviewByOrderAndUser(orderId, userId);

            // then
            assertThat(result).isTrue();
            then(reviewQueryPort).should().existsActiveReviewByOrderAndUser(orderId, userId);
        }

        @Test
        @DisplayName("활성 리뷰가 없으면 false를 반환한다")
        void existsActiveReviewByOrderAndUser_NoReview_ReturnsFalse() {
            // given
            long orderId = 999L;
            long userId = 100L;
            given(reviewQueryPort.existsActiveReviewByOrderAndUser(orderId, userId))
                    .willReturn(false);

            // when
            boolean result = sut.existsActiveReviewByOrderAndUser(orderId, userId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("getActiveReview() - 활성 리뷰 조회")
    class GetActiveReviewTest {

        @Test
        @DisplayName("활성 리뷰가 존재하면 Review를 반환한다")
        void getActiveReview_ExistsReview_ReturnsReview() {
            // given
            long reviewId = 1L;
            long userId = 100L;
            given(reviewQueryPort.fetchActiveReview(reviewId, userId))
                    .willReturn(Optional.of(mockReview));

            // when
            Review result = sut.getActiveReview(reviewId, userId);

            // then
            assertThat(result).isEqualTo(mockReview);
            then(reviewQueryPort).should().fetchActiveReview(reviewId, userId);
        }

        @Test
        @DisplayName("활성 리뷰가 없으면 ReviewNotFoundException이 발생한다")
        void getActiveReview_NoReview_ThrowsReviewNotFoundException() {
            // given
            long reviewId = 999L;
            long userId = 100L;
            given(reviewQueryPort.fetchActiveReview(reviewId, userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getActiveReview(reviewId, userId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }
}
