package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductException 테스트")
class ProductExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ProductException exception = new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("상품을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("PRD-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ProductException exception =
                    new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND, "ID 789 상품 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 789 상품 없음");
            assertThat(exception.code()).isEqualTo("PRD-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ProductException exception =
                    new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("PRD-001");
        }
    }

    @Nested
    @DisplayName("ProductNotFoundException 테스트")
    class ProductNotFoundExceptionTest {

        @Test
        @DisplayName("ProductNotFoundException ID 포함 생성")
        void createProductNotFoundExceptionWithId() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(123L);

            // then
            assertThat(exception.code()).isEqualTo("PRD-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("상품을 찾을 수 없습니다: 123");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductException은 DomainException을 상속한다")
        void productExceptionExtendsDomainException() {
            // given
            ProductException exception = new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductNotFoundException은 ProductException을 상속한다")
        void productNotFoundExceptionExtendsProductException() {
            // given
            ProductNotFoundException exception = new ProductNotFoundException(1L);

            // then
            assertThat(exception).isInstanceOf(ProductException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
