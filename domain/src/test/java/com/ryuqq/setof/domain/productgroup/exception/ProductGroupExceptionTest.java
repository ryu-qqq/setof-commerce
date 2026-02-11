package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupException 테스트")
class ProductGroupExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ProductGroupException exception =
                    new ProductGroupException(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("상품그룹을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("PG-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ProductGroupException exception =
                    new ProductGroupException(
                            ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND, "ID 789 상품그룹 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 789 상품그룹 없음");
            assertThat(exception.code()).isEqualTo("PG-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ProductGroupException exception =
                    new ProductGroupException(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("PG-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("ProductGroupNotFoundException ID 포함 생성")
        void createProductGroupNotFoundExceptionWithId() {
            // when
            ProductGroupNotFoundException exception = new ProductGroupNotFoundException(123L);

            // then
            assertThat(exception.code()).isEqualTo("PG-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("상품그룹을 찾을 수 없습니다: 123");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductGroupException은 DomainException을 상속한다")
        void productGroupExceptionExtendsDomainException() {
            // given
            ProductGroupException exception =
                    new ProductGroupException(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductGroupNotFoundException은 ProductGroupException을 상속한다")
        void productGroupNotFoundExceptionExtendsProductGroupException() {
            // given
            ProductGroupNotFoundException exception = new ProductGroupNotFoundException(1L);

            // then
            assertThat(exception).isInstanceOf(ProductGroupException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
