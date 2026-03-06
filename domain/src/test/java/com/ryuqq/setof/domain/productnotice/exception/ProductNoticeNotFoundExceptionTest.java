package com.ryuqq.setof.domain.productnotice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNoticeNotFoundException 테스트")
class ProductNoticeNotFoundExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ProductGroupId로 예외를 생성한다")
        void createWithProductGroupId() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);

            // when
            ProductNoticeNotFoundException exception =
                    new ProductNoticeNotFoundException(productGroupId);

            // then
            assertThat(exception.code()).isEqualTo("NOTICE-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("100");
        }

        @Test
        @DisplayName("예외 메시지에 productGroupId 값이 포함된다")
        void messageContainsProductGroupId() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(999L);

            // when
            ProductNoticeNotFoundException exception =
                    new ProductNoticeNotFoundException(productGroupId);

            // then
            assertThat(exception.getMessage()).contains("999");
            assertThat(exception.getMessage()).contains("상품고시를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductNoticeNotFoundException은 ProductNoticeException을 상속한다")
        void extendsProductNoticeException() {
            // given
            ProductNoticeNotFoundException exception =
                    new ProductNoticeNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception).isInstanceOf(ProductNoticeException.class);
        }

        @Test
        @DisplayName("ProductNoticeNotFoundException은 DomainException을 상속한다")
        void extendsDomainException() {
            // given
            ProductNoticeNotFoundException exception =
                    new ProductNoticeNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductNoticeNotFoundException은 RuntimeException을 상속한다")
        void extendsRuntimeException() {
            // given
            ProductNoticeNotFoundException exception =
                    new ProductNoticeNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("에러 코드 검증 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("NOTICE_NOT_FOUND 에러 코드를 사용한다")
        void usesNoticeNotFoundErrorCode() {
            // given
            ProductNoticeNotFoundException exception =
                    new ProductNoticeNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ProductNoticeErrorCode.NOTICE_NOT_FOUND);
        }
    }
}
