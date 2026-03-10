package com.ryuqq.setof.domain.review.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewErrorCode 테스트")
class ReviewErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ReviewErrorCode.REVIEW_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("리뷰 관련 에러 코드 테스트")
    class ReviewErrorCodesTest {

        @Test
        @DisplayName("REVIEW_NOT_FOUND 에러 코드를 검증한다")
        void reviewNotFound() {
            // then
            assertThat(ReviewErrorCode.REVIEW_NOT_FOUND.getCode()).isEqualTo("RVW-001");
            assertThat(ReviewErrorCode.REVIEW_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(ReviewErrorCode.REVIEW_NOT_FOUND.getMessage()).isEqualTo("리뷰를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("REVIEW_ALREADY_WRITTEN 에러 코드를 검증한다")
        void reviewAlreadyWritten() {
            // then
            assertThat(ReviewErrorCode.REVIEW_ALREADY_WRITTEN.getCode()).isEqualTo("RVW-003");
            assertThat(ReviewErrorCode.REVIEW_ALREADY_WRITTEN.getHttpStatus()).isEqualTo(409);
            assertThat(ReviewErrorCode.REVIEW_ALREADY_WRITTEN.getMessage())
                    .isEqualTo("이미 리뷰를 작성한 주문입니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ReviewErrorCode.values())
                    .containsExactly(
                            ReviewErrorCode.REVIEW_NOT_FOUND,
                            ReviewErrorCode.REVIEW_ALREADY_WRITTEN);
        }
    }
}
