package com.ryuqq.setof.domain.qna.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaErrorCode 단위 테스트")
class QnaErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(QnaErrorCode.QNA_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("Q&A 관련 에러 코드 테스트")
    class QnaErrorCodesTest {

        @Test
        @DisplayName("QNA_NOT_FOUND 에러 코드를 검증한다")
        void qnaNotFound() {
            assertThat(QnaErrorCode.QNA_NOT_FOUND.getCode()).isEqualTo("QNA-001");
            assertThat(QnaErrorCode.QNA_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(QnaErrorCode.QNA_NOT_FOUND.getMessage()).isEqualTo("Q&A를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("QNA_ALREADY_ANSWERED 에러 코드를 검증한다")
        void qnaAlreadyAnswered() {
            assertThat(QnaErrorCode.QNA_ALREADY_ANSWERED.getCode()).isEqualTo("QNA-002");
            assertThat(QnaErrorCode.QNA_ALREADY_ANSWERED.getHttpStatus()).isEqualTo(409);
            assertThat(QnaErrorCode.QNA_ALREADY_ANSWERED.getMessage()).isEqualTo("이미 답변이 등록된 Q&A입니다");
        }

        @Test
        @DisplayName("QNA_ALREADY_CLOSED 에러 코드를 검증한다")
        void qnaAlreadyClosed() {
            assertThat(QnaErrorCode.QNA_ALREADY_CLOSED.getCode()).isEqualTo("QNA-003");
            assertThat(QnaErrorCode.QNA_ALREADY_CLOSED.getHttpStatus()).isEqualTo(400);
            assertThat(QnaErrorCode.QNA_ALREADY_CLOSED.getMessage()).isEqualTo("종료된 Q&A에는 작업을 수행할 수 없습니다");
        }

        @Test
        @DisplayName("QNA_IMAGE_LIMIT_EXCEEDED 에러 코드를 검증한다")
        void qnaImageLimitExceeded() {
            assertThat(QnaErrorCode.QNA_IMAGE_LIMIT_EXCEEDED.getCode()).isEqualTo("QNA-004");
            assertThat(QnaErrorCode.QNA_IMAGE_LIMIT_EXCEEDED.getHttpStatus()).isEqualTo(400);
            assertThat(QnaErrorCode.QNA_IMAGE_LIMIT_EXCEEDED.getMessage()).isEqualTo("Q&A 이미지 수가 최대 한도를 초과했습니다");
        }

        @Test
        @DisplayName("QNA_DUPLICATE_PENDING 에러 코드를 검증한다")
        void qnaDuplicatePending() {
            assertThat(QnaErrorCode.QNA_DUPLICATE_PENDING.getCode()).isEqualTo("QNA-005");
            assertThat(QnaErrorCode.QNA_DUPLICATE_PENDING.getHttpStatus()).isEqualTo(409);
            assertThat(QnaErrorCode.QNA_DUPLICATE_PENDING.getMessage()).isEqualTo("동일 대상에 미답변 질문이 존재합니다");
        }

        @Test
        @DisplayName("QNA_IMAGE_NOT_ALLOWED 에러 코드를 검증한다")
        void qnaImageNotAllowed() {
            assertThat(QnaErrorCode.QNA_IMAGE_NOT_ALLOWED.getCode()).isEqualTo("QNA-006");
            assertThat(QnaErrorCode.QNA_IMAGE_NOT_ALLOWED.getHttpStatus()).isEqualTo(400);
            assertThat(QnaErrorCode.QNA_IMAGE_NOT_ALLOWED.getMessage()).isEqualTo("해당 유형의 Q&A에는 이미지 첨부가 불가합니다");
        }

        @Test
        @DisplayName("QNA_ANSWER_NOT_FOUND 에러 코드를 검증한다")
        void qnaAnswerNotFound() {
            assertThat(QnaErrorCode.QNA_ANSWER_NOT_FOUND.getCode()).isEqualTo("QNA-007");
            assertThat(QnaErrorCode.QNA_ANSWER_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(QnaErrorCode.QNA_ANSWER_NOT_FOUND.getMessage()).isEqualTo("Q&A 답변을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(QnaErrorCode.values())
                    .containsExactly(
                            QnaErrorCode.QNA_NOT_FOUND,
                            QnaErrorCode.QNA_ALREADY_ANSWERED,
                            QnaErrorCode.QNA_ALREADY_CLOSED,
                            QnaErrorCode.QNA_IMAGE_LIMIT_EXCEEDED,
                            QnaErrorCode.QNA_DUPLICATE_PENDING,
                            QnaErrorCode.QNA_IMAGE_NOT_ALLOWED,
                            QnaErrorCode.QNA_ANSWER_NOT_FOUND);
        }
    }
}
