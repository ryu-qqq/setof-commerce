package com.ryuqq.setof.application.review.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.dto.bundle.ReviewRegistrationBundle;
import com.ryuqq.setof.application.review.manager.ReviewCommandManager;
import com.ryuqq.setof.application.review.manager.ReviewImageCommandManager;
import com.ryuqq.setof.domain.review.aggregate.Review;
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
@DisplayName("ReviewPersistFacade 단위 테스트")
class ReviewPersistFacadeTest {

    @InjectMocks private ReviewPersistFacade sut;

    @Mock private ReviewCommandManager reviewCommandManager;
    @Mock private ReviewImageCommandManager reviewImageCommandManager;

    @Mock private Review mockReview;

    @Nested
    @DisplayName("persist() - 리뷰 영속")
    class PersistTest {

        @Test
        @DisplayName("이미지가 있는 번들을 영속하면 리뷰와 이미지 모두 저장하고 reviewId를 반환한다")
        void persist_BundleWithImages_SavesReviewAndImagesAndReturnsId() {
            // given
            ReviewImages imagesWithContent = ReviewImages.empty(); // 빈 이미지 (isEmpty=true)
            ReviewRegistrationBundle bundle =
                    new ReviewRegistrationBundle(mockReview, imagesWithContent);
            Long expectedReviewId = 1L;

            given(reviewCommandManager.persist(mockReview)).willReturn(expectedReviewId);

            // when
            Long result = sut.persist(bundle);

            // then
            assertThat(result).isEqualTo(expectedReviewId);
            then(reviewCommandManager).should().persist(mockReview);
        }

        @Test
        @DisplayName("이미지가 없는 번들을 영속하면 이미지 저장을 건너뛰고 reviewId를 반환한다")
        void persist_BundleWithoutImages_SkipsImageSaveAndReturnsId() {
            // given
            ReviewImages emptyImages = ReviewImages.empty();
            ReviewRegistrationBundle bundle = new ReviewRegistrationBundle(mockReview, emptyImages);
            Long expectedReviewId = 2L;

            given(reviewCommandManager.persist(mockReview)).willReturn(expectedReviewId);

            // when
            Long result = sut.persist(bundle);

            // then
            assertThat(result).isEqualTo(expectedReviewId);
            then(reviewCommandManager).should().persist(mockReview);
            then(reviewImageCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("리뷰 저장 후 반환된 reviewId가 최종 반환값과 일치한다")
        void persist_ReviewIdReturnedFromCommandManager_IsReturnedToCallee() {
            // given
            ReviewImages emptyImages = ReviewImages.empty();
            ReviewRegistrationBundle bundle = new ReviewRegistrationBundle(mockReview, emptyImages);
            Long persistedId = 99L;

            given(reviewCommandManager.persist(mockReview)).willReturn(persistedId);

            // when
            Long result = sut.persist(bundle);

            // then
            assertThat(result).isEqualTo(persistedId);
        }
    }
}
