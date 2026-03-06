package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupInvalidStatusTransitionException 테스트")
class ProductGroupInvalidStatusTransitionExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("DELETED에서 ACTIVE 전환 시 예외가 생성된다")
        void createFromDeletedToActive() {
            // when
            ProductGroupInvalidStatusTransitionException exception =
                    new ProductGroupInvalidStatusTransitionException(
                            ProductGroupStatus.DELETED, ProductGroupStatus.ACTIVE);

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("DELETED");
            assertThat(exception.getMessage()).contains("ACTIVE");
        }

        @Test
        @DisplayName("DELETED에서 SOLD_OUT 전환 시 예외가 생성된다")
        void createFromDeletedToSoldOut() {
            // when
            ProductGroupInvalidStatusTransitionException exception =
                    new ProductGroupInvalidStatusTransitionException(
                            ProductGroupStatus.DELETED, ProductGroupStatus.SOLD_OUT);

            // then
            assertThat(exception.getMessage()).contains("DELETED");
            assertThat(exception.getMessage()).contains("SOLD_OUT");
        }

        @Test
        @DisplayName("SOLD_OUT에서 SOLD_OUT 전환 시 예외가 생성된다")
        void createFromSoldOutToSoldOut() {
            // when
            ProductGroupInvalidStatusTransitionException exception =
                    new ProductGroupInvalidStatusTransitionException(
                            ProductGroupStatus.SOLD_OUT, ProductGroupStatus.SOLD_OUT);

            // then
            assertThat(exception.getMessage()).isNotNull();
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("에러 코드는 INVALID_STATUS_TRANSITION이다")
        void hasCorrectErrorCode() {
            // given
            ProductGroupInvalidStatusTransitionException exception =
                    new ProductGroupInvalidStatusTransitionException(
                            ProductGroupStatus.DELETED, ProductGroupStatus.ACTIVE);

            // then
            assertThat(exception.code()).isEqualTo("PG-003");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductGroupException을 상속한다")
        void extendsProductGroupException() {
            // given
            ProductGroupInvalidStatusTransitionException exception =
                    new ProductGroupInvalidStatusTransitionException(
                            ProductGroupStatus.DELETED, ProductGroupStatus.ACTIVE);

            // then
            assertThat(exception).isInstanceOf(ProductGroupException.class);
        }

        @Test
        @DisplayName("args에 currentStatus와 targetStatus를 포함한다")
        void argsContainStatusInfo() {
            // given
            ProductGroupInvalidStatusTransitionException exception =
                    new ProductGroupInvalidStatusTransitionException(
                            ProductGroupStatus.DELETED, ProductGroupStatus.ACTIVE);

            // then
            assertThat(exception.args()).containsKey("currentStatus");
            assertThat(exception.args()).containsKey("targetStatus");
            assertThat(exception.args().get("currentStatus")).isEqualTo("DELETED");
            assertThat(exception.args().get("targetStatus")).isEqualTo("ACTIVE");
        }
    }
}
