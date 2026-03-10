package com.ryuqq.setof.application.review.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.review.ReviewCommandFixtures;
import com.ryuqq.setof.application.review.dto.bundle.ReviewRegistrationBundle;
import com.ryuqq.setof.application.review.dto.command.RegisterReviewCommand;
import com.ryuqq.setof.application.review.factory.ReviewCommandFactory;
import com.ryuqq.setof.application.review.internal.ReviewPersistFacade;
import com.ryuqq.setof.application.review.validator.ReviewRegistrationValidator;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.ReviewAlreadyWrittenException;
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
@DisplayName("RegisterReviewService 단위 테스트")
class RegisterReviewServiceTest {

    @InjectMocks private RegisterReviewService sut;

    @Mock private ReviewRegistrationValidator validator;
    @Mock private ReviewCommandFactory factory;
    @Mock private ReviewPersistFacade persistFacade;

    @Mock private Review mockReview;
    @Mock private ReviewImages mockReviewImages;

    @Nested
    @DisplayName("execute() - 리뷰 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 리뷰를 등록하고 리뷰 ID를 반환한다")
        void execute_ValidCommand_ReturnsReviewId() {
            // given
            RegisterReviewCommand command = ReviewCommandFixtures.registerCommand();
            ReviewRegistrationBundle bundle =
                    new ReviewRegistrationBundle(mockReview, mockReviewImages);
            Long expectedId = 1L;

            given(factory.createRegistrationBundle(command)).willReturn(bundle);
            given(persistFacade.persist(bundle)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(validator).should().validateNoDuplicate(command.orderId(), command.userId());
            then(factory).should().createRegistrationBundle(command);
            then(persistFacade).should().persist(bundle);
        }

        @Test
        @DisplayName("이미지 없이 등록해도 정상 처리된다")
        void execute_CommandWithoutImages_ReturnsReviewId() {
            // given
            RegisterReviewCommand command = ReviewCommandFixtures.registerCommandWithoutImages();
            ReviewRegistrationBundle bundle =
                    new ReviewRegistrationBundle(mockReview, mockReviewImages);
            Long expectedId = 2L;

            given(factory.createRegistrationBundle(command)).willReturn(bundle);
            given(persistFacade.persist(bundle)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(validator).should().validateNoDuplicate(command.orderId(), command.userId());
        }

        @Test
        @DisplayName("중복 리뷰가 존재하면 ReviewAlreadyWrittenException이 발생한다")
        void execute_DuplicateReview_ThrowsReviewAlreadyWrittenException() {
            // given
            RegisterReviewCommand command = ReviewCommandFixtures.registerCommand();
            willThrow(new ReviewAlreadyWrittenException(command.orderId()))
                    .given(validator)
                    .validateNoDuplicate(command.orderId(), command.userId());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(ReviewAlreadyWrittenException.class);

            then(factory).shouldHaveNoInteractions();
            then(persistFacade).shouldHaveNoInteractions();
        }
    }
}
