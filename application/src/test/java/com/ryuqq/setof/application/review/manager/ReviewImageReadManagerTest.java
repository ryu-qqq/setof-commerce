package com.ryuqq.setof.application.review.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.port.out.query.ReviewImageQueryPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
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
@DisplayName("ReviewImageReadManager 단위 테스트")
class ReviewImageReadManagerTest {

    @InjectMocks private ReviewImageReadManager sut;

    @Mock private ReviewImageQueryPort queryPort;

    @Nested
    @DisplayName("fetchByReviewId() - reviewId로 리뷰 이미지 목록 조회")
    class FetchByReviewIdTest {

        @Test
        @DisplayName("reviewId로 리뷰 이미지 컬렉션을 조회한다")
        void fetchByReviewId_ValidReviewId_ReturnsReviewImages() {
            // given
            long reviewId = 1L;
            ReviewImages expected = ReviewImages.empty();
            given(queryPort.fetchByReviewId(reviewId)).willReturn(expected);

            // when
            ReviewImages result = sut.fetchByReviewId(reviewId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchByReviewId(reviewId);
        }

        @Test
        @DisplayName("이미지가 없는 리뷰에 대해 빈 ReviewImages를 반환한다")
        void fetchByReviewId_NoImages_ReturnsEmptyReviewImages() {
            // given
            long reviewId = 999L;
            ReviewImages emptyImages = ReviewImages.empty();
            given(queryPort.fetchByReviewId(reviewId)).willReturn(emptyImages);

            // when
            ReviewImages result = sut.fetchByReviewId(reviewId);

            // then
            assertThat(result.isEmpty()).isTrue();
        }
    }
}
