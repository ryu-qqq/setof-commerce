package com.ryuqq.setof.application.review.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.review.ReviewCommandFixtures;
import com.ryuqq.setof.application.review.dto.bundle.ReviewRegistrationBundle;
import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.review.id.ReviewId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewCommandFactory 단위 테스트")
class ReviewCommandFactoryTest {

    private ReviewCommandFactory sut;

    @BeforeEach
    void setUp() {
        sut = new ReviewCommandFactory();
    }

    @Nested
    @DisplayName("createRegistrationBundle() - RegisterReviewCommand → ReviewRegistrationBundle 변환")
    class CreateRegistrationBundleTest {

        @Test
        @DisplayName("커맨드를 ReviewRegistrationBundle로 변환한다")
        void createRegistrationBundle_ValidCommand_ReturnsBundle() {
            // given
            RegisterReviewCommand command = ReviewCommandFixtures.registerCommand();

            // when
            ReviewRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.review()).isNotNull();
            assertThat(bundle.reviewImages()).isNotNull();
        }

        @Test
        @DisplayName("이미지 URL이 있으면 ReviewImages에 이미지가 포함된다")
        void createRegistrationBundle_WithImages_BundleContainsImages() {
            // given
            RegisterReviewCommand command = ReviewCommandFixtures.registerCommand();

            // when
            ReviewRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.reviewImages().isEmpty()).isFalse();
            assertThat(bundle.reviewImages().size()).isEqualTo(command.imageUrls().size());
        }

        @Test
        @DisplayName("이미지 URL이 없으면 ReviewImages가 비어있다")
        void createRegistrationBundle_WithoutImages_BundleHasEmptyImages() {
            // given
            RegisterReviewCommand command = ReviewCommandFixtures.registerCommandWithoutImages();

            // when
            ReviewRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.reviewImages().isEmpty()).isTrue();
        }

        @Test
        @DisplayName("생성된 Review의 평점이 커맨드와 일치한다")
        void createRegistrationBundle_RatingMatchesCommand() {
            // given
            RegisterReviewCommand command =
                    ReviewCommandFixtures.registerCommand(100L, 700L, 500L, 5.0, "최고입니다!");

            // when
            ReviewRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.review()).isNotNull();
        }

        @Test
        @DisplayName("null 이미지 URL 목록으로 빈 ReviewImages를 생성한다")
        void createRegistrationBundle_NullImageUrls_EmptyImages() {
            // given
            RegisterReviewCommand command =
                    new RegisterReviewCommand(100L, 700L, 500L, 4.0, "좋아요", null);

            // when
            ReviewRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.reviewImages().isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("createDeleteContext() - DeleteReviewCommand → UpdateContext 변환")
    class CreateDeleteContextTest {

        @Test
        @DisplayName("DeleteReviewCommand를 UpdateContext로 변환한다")
        void createDeleteContext_ValidCommand_ReturnsUpdateContext() {
            // given
            DeleteReviewCommand command = ReviewCommandFixtures.deleteCommand();

            // when
            UpdateContext<ReviewId, DeletionStatus> context = sut.createDeleteContext(command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.id().value()).isEqualTo(command.reviewId());
            assertThat(context.changedAt()).isNotNull();
        }

        @Test
        @DisplayName("UpdateContext의 id가 커맨드의 reviewId와 일치한다")
        void createDeleteContext_IdMatchesCommandReviewId() {
            // given
            DeleteReviewCommand command = ReviewCommandFixtures.deleteCommand(42L, 100L);

            // when
            UpdateContext<ReviewId, DeletionStatus> context = sut.createDeleteContext(command);

            // then
            assertThat(context.id().value()).isEqualTo(42L);
        }

        @Test
        @DisplayName("UpdateContext의 changedAt이 null이 아니다")
        void createDeleteContext_ChangedAtIsNotNull() {
            // given
            DeleteReviewCommand command = ReviewCommandFixtures.deleteCommand();

            // when
            UpdateContext<ReviewId, DeletionStatus> context = sut.createDeleteContext(command);

            // then
            assertThat(context.changedAt()).isNotNull();
        }
    }
}
