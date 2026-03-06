package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNotFoundException 테스트")
class ProductNotFoundExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("productId로 예외를 생성한다")
        void createWithProductId() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(123L);

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.code()).isEqualTo("PRD-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("예외 메시지에 상품 ID가 포함된다")
        void messageContainsProductId() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(456L);

            // then
            assertThat(exception.getMessage()).contains("456");
        }

        @Test
        @DisplayName("예외 메시지에 '상품을 찾을 수 없습니다' 문구가 포함된다")
        void messageContainsNotFoundDescription() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(1L);

            // then
            assertThat(exception.getMessage()).contains("상품을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("예외 메시지 형식이 올바르다")
        void messageFormatIsCorrect() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(789L);

            // then
            assertThat(exception.getMessage()).isEqualTo("상품을 찾을 수 없습니다: 789");
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("ErrorCode는 PRODUCT_NOT_FOUND이다")
        void errorCodeIsProductNotFound() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(1L);

            // then
            assertThat(exception.code()).isEqualTo(ProductErrorCode.PRODUCT_NOT_FOUND.getCode());
            assertThat(exception.httpStatus())
                    .isEqualTo(ProductErrorCode.PRODUCT_NOT_FOUND.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductNotFoundException은 ProductException을 상속한다")
        void extendsProductException() {
            // given
            ProductNotFoundException exception = new ProductNotFoundException(1L);

            // then
            assertThat(exception).isInstanceOf(ProductException.class);
        }

        @Test
        @DisplayName("ProductNotFoundException은 DomainException을 상속한다")
        void extendsDomainException() {
            // given
            ProductNotFoundException exception = new ProductNotFoundException(1L);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("다양한 ID 값 테스트")
    class VariousIdTest {

        @Test
        @DisplayName("큰 ID 값으로 예외를 생성할 수 있다")
        void createWithLargeId() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(Long.MAX_VALUE);

            // then
            assertThat(exception.getMessage()).contains(String.valueOf(Long.MAX_VALUE));
        }

        @Test
        @DisplayName("ID 값 0으로 예외를 생성할 수 있다")
        void createWithZeroId() {
            // when
            ProductNotFoundException exception = new ProductNotFoundException(0L);

            // then
            assertThat(exception.getMessage()).contains("0");
        }
    }
}
