package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductInvalidStatusTransitionException 테스트")
class ProductInvalidStatusTransitionExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("from/to 상태로 예외를 생성한다")
        void createWithFromAndToStatus() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.ACTIVE, ProductStatus.ACTIVE);

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.code()).isEqualTo("PRD-003");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("예외 메시지에 from 상태가 포함된다")
        void messageContainsFromStatus() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.DELETED, ProductStatus.ACTIVE);

            // then
            assertThat(exception.getMessage()).contains("DELETED");
        }

        @Test
        @DisplayName("예외 메시지에 to 상태가 포함된다")
        void messageContainsToStatus() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.INACTIVE, ProductStatus.SOLD_OUT);

            // then
            assertThat(exception.getMessage()).contains("SOLD_OUT");
        }

        @Test
        @DisplayName("예외 메시지에 '전이할 수 없습니다' 문구가 포함된다")
        void messageContainsTransitionDescription() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.ACTIVE, ProductStatus.ACTIVE);

            // then
            assertThat(exception.getMessage()).contains("전이할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("ErrorCode는 INVALID_STATUS_TRANSITION이다")
        void errorCodeIsInvalidStatusTransition() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.ACTIVE, ProductStatus.INACTIVE);

            // then
            assertThat(exception.code())
                    .isEqualTo(ProductErrorCode.INVALID_STATUS_TRANSITION.getCode());
            assertThat(exception.httpStatus())
                    .isEqualTo(ProductErrorCode.INVALID_STATUS_TRANSITION.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("다양한 상태 전이 조합 테스트")
    class StatusTransitionCombinationTest {

        @Test
        @DisplayName("DELETED -> ACTIVE 전이 예외를 생성한다")
        void createExceptionForDeletedToActive() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.DELETED, ProductStatus.ACTIVE);

            // then
            assertThat(exception.getMessage()).contains("DELETED");
            assertThat(exception.getMessage()).contains("ACTIVE");
        }

        @Test
        @DisplayName("ACTIVE -> ACTIVE 전이 예외를 생성한다")
        void createExceptionForActiveToActive() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.ACTIVE, ProductStatus.ACTIVE);

            // then
            assertThat(exception.getMessage()).contains("ACTIVE");
        }

        @Test
        @DisplayName("SOLD_OUT -> INACTIVE 전이 예외를 생성한다")
        void createExceptionForSoldOutToInactive() {
            // when
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.SOLD_OUT, ProductStatus.INACTIVE);

            // then
            assertThat(exception.getMessage()).contains("SOLD_OUT");
            assertThat(exception.getMessage()).contains("INACTIVE");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductInvalidStatusTransitionException은 DomainException을 상속한다")
        void extendsDomainException() {
            // given
            ProductInvalidStatusTransitionException exception =
                    new ProductInvalidStatusTransitionException(
                            ProductStatus.DELETED, ProductStatus.ACTIVE);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
