package com.ryuqq.setof.application.review.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.ReviewNotFoundException;
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
@DisplayName("ReviewDeletionValidator 단위 테스트")
class ReviewDeletionValidatorTest {

    @InjectMocks private ReviewDeletionValidator sut;

    @Mock private ReviewReadManager readManager;

    @Mock private Review mockReview;

    @Nested
    @DisplayName("getExistingReview() - 활성 리뷰 조회 검증")
    class GetExistingReviewTest {

        @Test
        @DisplayName("활성 리뷰가 있으면 Review를 반환한다")
        void getExistingReview_ExistsReview_ReturnsReview() {
            // given
            long reviewId = 1L;
            long userId = 100L;
            given(readManager.getActiveReview(reviewId, userId)).willReturn(mockReview);

            // when
            Review result = sut.getExistingReview(reviewId, userId);

            // then
            assertThat(result).isEqualTo(mockReview);
            then(readManager).should().getActiveReview(reviewId, userId);
        }

        @Test
        @DisplayName("활성 리뷰가 없으면 ReviewNotFoundException이 발생한다")
        void getExistingReview_NoReview_ThrowsReviewNotFoundException() {
            // given
            long reviewId = 999L;
            long userId = 100L;
            given(readManager.getActiveReview(reviewId, userId))
                    .willThrow(new ReviewNotFoundException(reviewId));

            // when & then
            assertThatThrownBy(() -> sut.getExistingReview(reviewId, userId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }

        @Test
        @DisplayName("다른 사용자의 리뷰를 조회하면 ReviewNotFoundException이 발생한다")
        void getExistingReview_OtherUserReview_ThrowsReviewNotFoundException() {
            // given
            long reviewId = 1L;
            long anotherUserId = 999L;
            given(readManager.getActiveReview(reviewId, anotherUserId))
                    .willThrow(new ReviewNotFoundException(reviewId));

            // when & then
            assertThatThrownBy(() -> sut.getExistingReview(reviewId, anotherUserId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }
}
