package com.ryuqq.setof.application.review.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.review.ReviewCommandFixtures;
import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.factory.ReviewCommandFactory;
import com.ryuqq.setof.application.review.manager.ReviewCommandManager;
import com.ryuqq.setof.application.review.manager.ReviewImageCommandManager;
import com.ryuqq.setof.application.review.manager.ReviewImageReadManager;
import com.ryuqq.setof.application.review.validator.ReviewDeletionValidator;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.ReviewNotFoundException;
import com.ryuqq.setof.domain.review.id.ReviewId;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
import java.time.Instant;
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
@DisplayName("DeleteReviewService 단위 테스트")
class DeleteReviewServiceTest {

    @InjectMocks private DeleteReviewService sut;

    @Mock private ReviewDeletionValidator validator;
    @Mock private ReviewImageReadManager reviewImageReadManager;
    @Mock private ReviewCommandFactory factory;
    @Mock private ReviewCommandManager reviewCommandManager;
    @Mock private ReviewImageCommandManager reviewImageCommandManager;

    @Mock private Review mockReview;

    @Nested
    @DisplayName("execute() - 리뷰 삭제")
    class ExecuteTest {

        @Test
        @DisplayName("이미지가 있는 리뷰를 삭제하면 리뷰와 이미지 모두 소프트 삭제된다")
        void execute_ReviewWithImages_DeletesReviewAndImages() {
            // given
            DeleteReviewCommand command = ReviewCommandFixtures.deleteCommand();
            Instant now = Instant.now();
            UpdateContext<ReviewId, DeletionStatus> context =
                    new UpdateContext<>(
                            ReviewId.of(command.reviewId()), DeletionStatus.deletedAt(now), now);

            ReviewImages imagesWithData = ReviewImages.of(java.util.List.of());

            given(validator.getExistingReview(command.reviewId(), command.userId()))
                    .willReturn(mockReview);
            given(factory.createDeleteContext(command)).willReturn(context);
            given(reviewImageReadManager.fetchByReviewId(command.reviewId()))
                    .willReturn(imagesWithData);

            // when
            sut.execute(command);

            // then
            then(validator).should().getExistingReview(command.reviewId(), command.userId());
            then(factory).should().createDeleteContext(command);
            then(mockReview).should().delete(context.changedAt());
            then(reviewCommandManager).should().persist(mockReview);
            then(reviewImageReadManager).should().fetchByReviewId(command.reviewId());
        }

        @Test
        @DisplayName("존재하지 않는 리뷰를 삭제하면 ReviewNotFoundException이 발생한다")
        void execute_NonExistingReview_ThrowsReviewNotFoundException() {
            // given
            DeleteReviewCommand command = ReviewCommandFixtures.deleteCommand(999L, 100L);
            willThrow(new ReviewNotFoundException(999L))
                    .given(validator)
                    .getExistingReview(command.reviewId(), command.userId());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(ReviewNotFoundException.class);

            then(factory).shouldHaveNoInteractions();
            then(reviewCommandManager).shouldHaveNoInteractions();
            then(reviewImageCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("이미지가 없는 리뷰를 삭제하면 reviewImageCommandManager를 호출하지 않는다")
        void execute_ReviewWithoutImages_DoesNotCallImageCommandManager() {
            // given
            DeleteReviewCommand command = ReviewCommandFixtures.deleteCommand();
            Instant now = Instant.now();
            UpdateContext<ReviewId, DeletionStatus> context =
                    new UpdateContext<>(
                            ReviewId.of(command.reviewId()), DeletionStatus.deletedAt(now), now);

            ReviewImages emptyImages = ReviewImages.empty();

            given(validator.getExistingReview(command.reviewId(), command.userId()))
                    .willReturn(mockReview);
            given(factory.createDeleteContext(command)).willReturn(context);
            given(reviewImageReadManager.fetchByReviewId(command.reviewId()))
                    .willReturn(emptyImages);

            // when
            sut.execute(command);

            // then
            then(reviewCommandManager).should().persist(mockReview);
            then(reviewImageCommandManager).shouldHaveNoInteractions();
        }
    }
}
