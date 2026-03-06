package com.ryuqq.setof.domain.productnotice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNoticeErrorCode 테스트")
class ProductNoticeErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ProductNoticeErrorCode.NOTICE_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("상품고시 관련 에러 코드 테스트")
    class ProductNoticeErrorCodesTest {

        @Test
        @DisplayName("NOTICE_NOT_FOUND 에러 코드를 검증한다")
        void noticeNotFound() {
            // then
            assertThat(ProductNoticeErrorCode.NOTICE_NOT_FOUND.getCode()).isEqualTo("NOTICE-001");
            assertThat(ProductNoticeErrorCode.NOTICE_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(ProductNoticeErrorCode.NOTICE_NOT_FOUND.getMessage())
                    .isEqualTo("상품고시를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductNoticeErrorCode.values())
                    .containsExactly(ProductNoticeErrorCode.NOTICE_NOT_FOUND);
        }

        @Test
        @DisplayName("valueOf()로 에러 코드를 조회한다")
        void valueOfReturnsCorrectCode() {
            // when
            ProductNoticeErrorCode code = ProductNoticeErrorCode.valueOf("NOTICE_NOT_FOUND");

            // then
            assertThat(code).isEqualTo(ProductNoticeErrorCode.NOTICE_NOT_FOUND);
        }
    }
}
