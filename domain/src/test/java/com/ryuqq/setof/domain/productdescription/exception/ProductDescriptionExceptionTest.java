package com.ryuqq.setof.domain.productdescription.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductDescriptionException 테스트")
class ProductDescriptionExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ProductDescriptionException exception =
                    new ProductDescriptionException(
                            ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("상세설명을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("DESC-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndCustomMessage() {
            // when
            ProductDescriptionException exception =
                    new ProductDescriptionException(
                            ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND,
                            "productGroupId=100 상세설명 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("productGroupId=100 상세설명 없음");
            assertThat(exception.code()).isEqualTo("DESC-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지, 컨텍스트 정보로 예외를 생성한다")
        void createWithErrorCodeAndMessageAndArgs() {
            // given
            Map<String, Object> args = Map.of("productGroupId", 100L);

            // when
            ProductDescriptionException exception =
                    new ProductDescriptionException(
                            ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND,
                            "상세설명을 찾을 수 없습니다",
                            args);

            // then
            assertThat(exception.code()).isEqualTo("DESC-001");
            assertThat(exception.args()).containsEntry("productGroupId", 100L);
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ProductDescriptionException exception =
                    new ProductDescriptionException(
                            ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("DESC-001");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductDescriptionException은 DomainException을 상속한다")
        void productDescriptionExceptionExtendsDomainException() {
            // given
            ProductDescriptionException exception =
                    new ProductDescriptionException(
                            ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductDescriptionException은 RuntimeException을 상속한다")
        void productDescriptionExceptionExtendsRuntimeException() {
            // given
            ProductDescriptionException exception =
                    new ProductDescriptionException(
                            ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }
}
