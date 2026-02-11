package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductErrorCode 테스트")
class ProductErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ProductErrorCode.PRODUCT_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("PRODUCT_NOT_FOUND 테스트")
    class ProductNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'PRD-001'이다")
        void hasCorrectCode() {
            assertThat(ProductErrorCode.PRODUCT_NOT_FOUND.getCode()).isEqualTo("PRD-001");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductErrorCode.PRODUCT_NOT_FOUND.getHttpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("메시지는 '상품을 찾을 수 없습니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage()).isEqualTo("상품을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("PRODUCT_ALREADY_DELETED 테스트")
    class ProductAlreadyDeletedTest {

        @Test
        @DisplayName("에러 코드는 'PRD-002'이다")
        void hasCorrectCode() {
            assertThat(ProductErrorCode.PRODUCT_ALREADY_DELETED.getCode()).isEqualTo("PRD-002");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductErrorCode.PRODUCT_ALREADY_DELETED.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '이미 삭제된 상품입니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductErrorCode.PRODUCT_ALREADY_DELETED.getMessage())
                    .isEqualTo("이미 삭제된 상품입니다");
        }
    }

    @Nested
    @DisplayName("INVALID_STATUS_TRANSITION 테스트")
    class InvalidStatusTransitionTest {

        @Test
        @DisplayName("에러 코드는 'PRD-003'이다")
        void hasCorrectCode() {
            assertThat(ProductErrorCode.INVALID_STATUS_TRANSITION.getCode()).isEqualTo("PRD-003");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductErrorCode.INVALID_STATUS_TRANSITION.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '유효하지 않은 상태 전이입니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductErrorCode.INVALID_STATUS_TRANSITION.getMessage())
                    .isEqualTo("유효하지 않은 상태 전이입니다");
        }
    }

    @Nested
    @DisplayName("INSUFFICIENT_STOCK 테스트")
    class InsufficientStockTest {

        @Test
        @DisplayName("에러 코드는 'PRD-004'이다")
        void hasCorrectCode() {
            assertThat(ProductErrorCode.INSUFFICIENT_STOCK.getCode()).isEqualTo("PRD-004");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductErrorCode.INSUFFICIENT_STOCK.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '재고가 부족합니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductErrorCode.INSUFFICIENT_STOCK.getMessage()).isEqualTo("재고가 부족합니다");
        }
    }

    @Nested
    @DisplayName("INVALID_STOCK_QUANTITY 테스트")
    class InvalidStockQuantityTest {

        @Test
        @DisplayName("에러 코드는 'PRD-005'이다")
        void hasCorrectCode() {
            assertThat(ProductErrorCode.INVALID_STOCK_QUANTITY.getCode()).isEqualTo("PRD-005");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductErrorCode.INVALID_STOCK_QUANTITY.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '유효하지 않은 재고 수량입니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductErrorCode.INVALID_STOCK_QUANTITY.getMessage())
                    .isEqualTo("유효하지 않은 재고 수량입니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductErrorCode.values())
                    .containsExactly(
                            ProductErrorCode.PRODUCT_NOT_FOUND,
                            ProductErrorCode.PRODUCT_ALREADY_DELETED,
                            ProductErrorCode.INVALID_STATUS_TRANSITION,
                            ProductErrorCode.INSUFFICIENT_STOCK,
                            ProductErrorCode.INVALID_STOCK_QUANTITY);
        }
    }
}
