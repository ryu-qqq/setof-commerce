package com.ryuqq.setof.application.review.validator;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.domain.review.exception.ReviewAlreadyWrittenException;
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
@DisplayName("ReviewRegistrationValidator 단위 테스트")
class ReviewRegistrationValidatorTest {

    @InjectMocks private ReviewRegistrationValidator sut;

    @Mock private ReviewReadManager readManager;

    @Nested
    @DisplayName("validateNoDuplicate() - 중복 리뷰 검증")
    class ValidateNoDuplicateTest {

        @Test
        @DisplayName("활성 리뷰가 없으면 정상적으로 통과한다")
        void validateNoDuplicate_NoExistingReview_PassesValidation() {
            // given
            long orderId = 700L;
            long userId = 100L;
            given(readManager.existsActiveReviewByOrderAndUser(orderId, userId)).willReturn(false);

            // when & then
            assertThatNoException().isThrownBy(() -> sut.validateNoDuplicate(orderId, userId));
            then(readManager).should().existsActiveReviewByOrderAndUser(orderId, userId);
        }

        @Test
        @DisplayName("이미 활성 리뷰가 있으면 ReviewAlreadyWrittenException이 발생한다")
        void validateNoDuplicate_ExistingReview_ThrowsReviewAlreadyWrittenException() {
            // given
            long orderId = 700L;
            long userId = 100L;
            given(readManager.existsActiveReviewByOrderAndUser(orderId, userId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> sut.validateNoDuplicate(orderId, userId))
                    .isInstanceOf(ReviewAlreadyWrittenException.class);
        }

        @Test
        @DisplayName("다른 주문 ID에 대해서는 중복 검증을 통과한다")
        void validateNoDuplicate_DifferentOrderId_PassesValidation() {
            // given
            long orderId = 888L;
            long userId = 100L;
            given(readManager.existsActiveReviewByOrderAndUser(orderId, userId)).willReturn(false);

            // when & then
            assertThatNoException().isThrownBy(() -> sut.validateNoDuplicate(orderId, userId));
        }
    }
}
