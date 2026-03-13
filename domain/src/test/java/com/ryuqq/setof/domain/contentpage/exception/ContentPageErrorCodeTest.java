package com.ryuqq.setof.domain.contentpage.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ContentPageErrorCode 테스트")
class ContentPageErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("콘텐츠 페이지 에러 코드 테스트")
    class ContentPageErrorCodesTest {

        @Test
        @DisplayName("CONTENT_PAGE_NOT_FOUND 에러 코드를 검증한다")
        void contentPageNotFound() {
            // then
            assertThat(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND.getCode()).isEqualTo("CTP-001");
            assertThat(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND.getMessage())
                    .isEqualTo("콘텐츠 페이지를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ContentPageErrorCode.values())
                    .containsExactly(ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND);
        }
    }
}
