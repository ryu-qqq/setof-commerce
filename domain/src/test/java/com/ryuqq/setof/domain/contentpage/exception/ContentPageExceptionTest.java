package com.ryuqq.setof.domain.contentpage.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ContentPageException 테스트")
class ContentPageExceptionTest {

    @Nested
    @DisplayName("ContentPageException 생성 테스트")
    class ContentPageExceptionCreationTest {

        @Test
        @DisplayName("DomainException을 상속한다")
        void extendsDomainException() {
            // when
            ContentPageException exception =
                    new ContentPageException(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ContentPageException exception =
                    new ContentPageException(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND);

            // then
            assertThat(exception.getErrorCode())
                    .isEqualTo(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND);
        }

        @Test
        @DisplayName("커스텀 메시지로 예외를 생성한다")
        void createWithCustomMessage() {
            // given
            String customMessage = "커스텀 에러 메시지";

            // when
            ContentPageException exception =
                    new ContentPageException(
                            ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND, customMessage);

            // then
            assertThat(exception.getMessage()).contains(customMessage);
        }

        @Test
        @DisplayName("cause로 예외를 생성한다")
        void createWithCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ContentPageException exception =
                    new ContentPageException(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("ContentPageNotFoundException 테스트")
    class ContentPageNotFoundExceptionTest {

        @Test
        @DisplayName("ContentPageException을 상속한다")
        void extendsContentPageException() {
            // when
            ContentPageNotFoundException exception = new ContentPageNotFoundException();

            // then
            assertThat(exception).isInstanceOf(ContentPageException.class);
        }

        @Test
        @DisplayName("기본 생성자로 생성 시 CONTENT_PAGE_NOT_FOUND 코드를 사용한다")
        void defaultConstructorUsesNotFoundCode() {
            // when
            ContentPageNotFoundException exception = new ContentPageNotFoundException();

            // then
            assertThat(exception.getErrorCode())
                    .isEqualTo(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND);
        }

        @Test
        @DisplayName("detail 메시지와 함께 예외를 생성한다")
        void createWithDetail() {
            // when
            ContentPageNotFoundException exception = new ContentPageNotFoundException("상세 정보");

            // then
            assertThat(exception.getMessage()).contains("해당 컨텐츠가 존재하지 않습니다");
            assertThat(exception.getMessage()).contains("상세 정보");
        }
    }
}
