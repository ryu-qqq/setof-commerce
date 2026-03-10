package com.ryuqq.setof.domain.qna.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.id.QnaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaException 단위 테스트")
class QnaExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 QnaException을 생성한다")
        void createWithErrorCode() {
            // when
            QnaException exception = new QnaException(QnaErrorCode.QNA_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("Q&A를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("QNA-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 QnaException을 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            QnaException exception = new QnaException(QnaErrorCode.QNA_NOT_FOUND, "ID 1001 Q&A 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 1001 Q&A 없음");
            assertThat(exception.code()).isEqualTo("QNA-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 QnaException을 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            QnaException exception = new QnaException(QnaErrorCode.QNA_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("QNA-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("QnaNotFoundException - QnaId로 생성")
        void createQnaNotFoundExceptionWithQnaId() {
            // given
            QnaId qnaId = QnaId.of("qna-uuid-0001");

            // when
            QnaNotFoundException exception = new QnaNotFoundException(qnaId);

            // then
            assertThat(exception.code()).isEqualTo("QNA-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("qna-uuid-0001");
        }

        @Test
        @DisplayName("QnaNotFoundException - LegacyQnaId로 생성")
        void createQnaNotFoundExceptionWithLegacyQnaId() {
            // given
            LegacyQnaId legacyQnaId = LegacyQnaId.of(1001L);

            // when
            QnaNotFoundException exception = new QnaNotFoundException(legacyQnaId);

            // then
            assertThat(exception.code()).isEqualTo("QNA-001");
            assertThat(exception.getMessage()).contains("1001");
        }

        @Test
        @DisplayName("QnaAlreadyAnsweredException 생성")
        void createQnaAlreadyAnsweredException() {
            // given
            LegacyQnaId legacyQnaId = LegacyQnaId.of(1001L);

            // when
            QnaAlreadyAnsweredException exception = new QnaAlreadyAnsweredException(legacyQnaId);

            // then
            assertThat(exception.code()).isEqualTo("QNA-002");
            assertThat(exception.httpStatus()).isEqualTo(409);
            assertThat(exception.getMessage()).contains("1001");
        }

        @Test
        @DisplayName("QnaAlreadyClosedException 생성")
        void createQnaAlreadyClosedException() {
            // given
            LegacyQnaId legacyQnaId = LegacyQnaId.of(1001L);

            // when
            QnaAlreadyClosedException exception = new QnaAlreadyClosedException(legacyQnaId);

            // then
            assertThat(exception.code()).isEqualTo("QNA-003");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).contains("1001");
        }

        @Test
        @DisplayName("QnaImageLimitExceededException 생성")
        void createQnaImageLimitExceededException() {
            // when
            QnaImageLimitExceededException exception = new QnaImageLimitExceededException();

            // then
            assertThat(exception.code()).isEqualTo("QNA-004");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("Q&A 이미지 수가 최대 한도를 초과했습니다");
        }

        @Test
        @DisplayName("QnaDuplicatePendingException 생성")
        void createQnaDuplicatePendingException() {
            // when
            QnaDuplicatePendingException exception = new QnaDuplicatePendingException(100L);

            // then
            assertThat(exception.code()).isEqualTo("QNA-005");
            assertThat(exception.httpStatus()).isEqualTo(409);
        }

        @Test
        @DisplayName("QnaImageNotAllowedException 생성")
        void createQnaImageNotAllowedException() {
            // when
            QnaImageNotAllowedException exception = new QnaImageNotAllowedException();

            // then
            assertThat(exception.code()).isEqualTo("QNA-006");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("해당 유형의 Q&A에는 이미지 첨부가 불가합니다");
        }

        @Test
        @DisplayName("QnaAnswerNotFoundException 생성")
        void createQnaAnswerNotFoundException() {
            // when
            QnaAnswerNotFoundException exception = new QnaAnswerNotFoundException(2001L);

            // then
            assertThat(exception.code()).isEqualTo("QNA-007");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("2001");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("QnaException은 DomainException을 상속한다")
        void qnaExceptionExtendsDomainException() {
            // given
            QnaException exception = new QnaException(QnaErrorCode.QNA_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("QnaNotFoundException은 QnaException을 상속한다")
        void qnaNotFoundExceptionExtendsQnaException() {
            // given
            QnaNotFoundException exception = new QnaNotFoundException(QnaId.of("qna-uuid-0001"));

            // then
            assertThat(exception).isInstanceOf(QnaException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("QnaAlreadyAnsweredException은 QnaException을 상속한다")
        void qnaAlreadyAnsweredExceptionExtendsQnaException() {
            // given
            QnaAlreadyAnsweredException exception = new QnaAlreadyAnsweredException(LegacyQnaId.of(1001L));

            // then
            assertThat(exception).isInstanceOf(QnaException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("QnaImageLimitExceededException은 QnaException을 상속한다")
        void qnaImageLimitExceededExceptionExtendsQnaException() {
            // given
            QnaImageLimitExceededException exception = new QnaImageLimitExceededException();

            // then
            assertThat(exception).isInstanceOf(QnaException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
