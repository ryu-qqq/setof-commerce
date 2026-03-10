package com.ryuqq.setof.domain.review.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewException 테스트")
class ReviewExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ReviewException exception = new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("리뷰를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("RVW-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ReviewException exception =
                    new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND, "ID 999 리뷰 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 999 리뷰 없음");
            assertThat(exception.code()).isEqualTo("RVW-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ReviewException exception =
                    new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("RVW-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("ReviewNotFoundException 기본 생성")
        void createReviewNotFoundException() {
            // when
            ReviewNotFoundException exception = new ReviewNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("RVW-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("리뷰를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ReviewNotFoundException ID 포함 생성")
        void createReviewNotFoundExceptionWithId() {
            // when
            ReviewNotFoundException exception = new ReviewNotFoundException(42L);

            // then
            assertThat(exception.code()).isEqualTo("RVW-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("ID가 42인 리뷰를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ReviewAlreadyWrittenException 기본 생성")
        void createReviewAlreadyWrittenException() {
            // when
            ReviewAlreadyWrittenException exception = new ReviewAlreadyWrittenException();

            // then
            assertThat(exception.code()).isEqualTo("RVW-003");
            assertThat(exception.httpStatus()).isEqualTo(409);
            assertThat(exception.getMessage()).isEqualTo("이미 리뷰를 작성한 주문입니다");
        }

        @Test
        @DisplayName("ReviewAlreadyWrittenException 주문 ID 포함 생성")
        void createReviewAlreadyWrittenExceptionWithOrderId() {
            // when
            ReviewAlreadyWrittenException exception = new ReviewAlreadyWrittenException(700L);

            // then
            assertThat(exception.code()).isEqualTo("RVW-003");
            assertThat(exception.httpStatus()).isEqualTo(409);
            assertThat(exception.getMessage()).isEqualTo("주문 700에 대한 리뷰가 이미 존재합니다");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ReviewException은 DomainException을 상속한다")
        void reviewExceptionExtendsDomainException() {
            // given
            ReviewException exception = new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ReviewNotFoundException은 ReviewException을 상속한다")
        void reviewNotFoundExceptionExtendsReviewException() {
            // given
            ReviewNotFoundException exception = new ReviewNotFoundException();

            // then
            assertThat(exception).isInstanceOf(ReviewException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ReviewAlreadyWrittenException은 ReviewException을 상속한다")
        void reviewAlreadyWrittenExceptionExtendsReviewException() {
            // given
            ReviewAlreadyWrittenException exception = new ReviewAlreadyWrittenException();

            // then
            assertThat(exception).isInstanceOf(ReviewException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
