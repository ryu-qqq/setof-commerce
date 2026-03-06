package com.ryuqq.setof.domain.productdescription.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductDescriptionNotFoundException 테스트")
class ProductDescriptionNotFoundExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ProductGroupId로 예외를 생성한다")
        void createWithProductGroupId() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);

            // when
            ProductDescriptionNotFoundException exception =
                    new ProductDescriptionNotFoundException(productGroupId);

            // then
            assertThat(exception.code()).isEqualTo("DESC-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("100");
            assertThat(exception.getMessage()).contains("productGroupId");
        }

        @Test
        @DisplayName("메시지에 productGroupId 값이 포함된다")
        void messageContainsProductGroupIdValue() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(999L);

            // when
            ProductDescriptionNotFoundException exception =
                    new ProductDescriptionNotFoundException(productGroupId);

            // then
            assertThat(exception.getMessage()).contains("999");
        }

        @Test
        @DisplayName("서로 다른 productGroupId로 각각 생성한다")
        void createWithDifferentProductGroupIds() {
            // given
            ProductGroupId id1 = ProductGroupId.of(1L);
            ProductGroupId id2 = ProductGroupId.of(2L);

            // when
            ProductDescriptionNotFoundException ex1 = new ProductDescriptionNotFoundException(id1);
            ProductDescriptionNotFoundException ex2 = new ProductDescriptionNotFoundException(id2);

            // then
            assertThat(ex1.getMessage()).contains("1");
            assertThat(ex2.getMessage()).contains("2");
            assertThat(ex1.code()).isEqualTo(ex2.code());
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductDescriptionNotFoundException은 ProductDescriptionException을 상속한다")
        void notFoundExceptionExtendsProductDescriptionException() {
            // given
            ProductDescriptionNotFoundException exception =
                    new ProductDescriptionNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception).isInstanceOf(ProductDescriptionException.class);
        }

        @Test
        @DisplayName("ProductDescriptionNotFoundException은 DomainException을 상속한다")
        void notFoundExceptionExtendsDomainException() {
            // given
            ProductDescriptionNotFoundException exception =
                    new ProductDescriptionNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductDescriptionNotFoundException은 RuntimeException을 상속한다")
        void notFoundExceptionExtendsRuntimeException() {
            // given
            ProductDescriptionNotFoundException exception =
                    new ProductDescriptionNotFoundException(ProductGroupId.of(1L));

            // then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }
}
