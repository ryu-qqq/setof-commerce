package com.ryuqq.setof.domain.productnotice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNoticeException 테스트")
class ProductNoticeExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ProductNoticeException exception =
                    new ProductNoticeException(ProductNoticeErrorCode.NOTICE_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("상품고시를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("NOTICE-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ProductNoticeException exception =
                    new ProductNoticeException(
                            ProductNoticeErrorCode.NOTICE_NOT_FOUND, "productGroupId=100 고시 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("productGroupId=100 고시 없음");
            assertThat(exception.code()).isEqualTo("NOTICE-001");
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지, 컨텍스트 정보로 예외를 생성한다")
        void createWithErrorCodeMessageAndArgs() {
            // given
            Map<String, Object> args = Map.of("productGroupId", 100L);

            // when
            ProductNoticeException exception =
                    new ProductNoticeException(
                            ProductNoticeErrorCode.NOTICE_NOT_FOUND, "고시 없음", args);

            // then
            assertThat(exception.getMessage()).isEqualTo("고시 없음");
            assertThat(exception.args()).containsEntry("productGroupId", 100L);
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ProductNoticeException exception =
                    new ProductNoticeException(ProductNoticeErrorCode.NOTICE_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("NOTICE-001");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductNoticeException은 DomainException을 상속한다")
        void productNoticeExceptionExtendsDomainException() {
            // given
            ProductNoticeException exception =
                    new ProductNoticeException(ProductNoticeErrorCode.NOTICE_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductNoticeException은 RuntimeException을 상속한다")
        void productNoticeExceptionExtendsRuntimeException() {
            // given
            ProductNoticeException exception =
                    new ProductNoticeException(ProductNoticeErrorCode.NOTICE_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("에러 코드 조회 테스트")
    class ErrorCodeAccessTest {

        @Test
        @DisplayName("getErrorCode()로 에러 코드 객체를 조회한다")
        void getErrorCodeReturnsCorrectCode() {
            // given
            ProductNoticeException exception =
                    new ProductNoticeException(ProductNoticeErrorCode.NOTICE_NOT_FOUND);

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ProductNoticeErrorCode.NOTICE_NOT_FOUND);
        }

        @Test
        @DisplayName("args()는 기본적으로 빈 Map을 반환한다")
        void argsReturnsEmptyMapByDefault() {
            // given
            ProductNoticeException exception =
                    new ProductNoticeException(ProductNoticeErrorCode.NOTICE_NOT_FOUND);

            // then
            assertThat(exception.args()).isEmpty();
        }
    }
}
