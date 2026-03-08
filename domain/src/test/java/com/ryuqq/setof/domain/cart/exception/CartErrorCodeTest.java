package com.ryuqq.setof.domain.cart.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartErrorCode 단위 테스트")
class CartErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("CartErrorCode는 ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(CartErrorCode.CART_ITEM_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("장바구니 에러 코드 검증")
    class CartErrorCodesTest {

        @Test
        @DisplayName("CART_ITEM_NOT_FOUND 에러 코드를 검증한다")
        void cartItemNotFound() {
            // then
            assertThat(CartErrorCode.CART_ITEM_NOT_FOUND.getCode()).isEqualTo("CART-001");
            assertThat(CartErrorCode.CART_ITEM_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(CartErrorCode.CART_ITEM_NOT_FOUND.getMessage())
                    .isEqualTo("장바구니 아이템을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("CART_ITEM_ALREADY_EXISTS 에러 코드를 검증한다")
        void cartItemAlreadyExists() {
            // then
            assertThat(CartErrorCode.CART_ITEM_ALREADY_EXISTS.getCode()).isEqualTo("CART-002");
            assertThat(CartErrorCode.CART_ITEM_ALREADY_EXISTS.getHttpStatus()).isEqualTo(409);
            assertThat(CartErrorCode.CART_ITEM_ALREADY_EXISTS.getMessage())
                    .isEqualTo("이미 장바구니에 존재하는 상품입니다");
        }

        @Test
        @DisplayName("CART_ITEM_LIMIT_EXCEEDED 에러 코드를 검증한다")
        void cartItemLimitExceeded() {
            // then
            assertThat(CartErrorCode.CART_ITEM_LIMIT_EXCEEDED.getCode()).isEqualTo("CART-003");
            assertThat(CartErrorCode.CART_ITEM_LIMIT_EXCEEDED.getHttpStatus()).isEqualTo(400);
            assertThat(CartErrorCode.CART_ITEM_LIMIT_EXCEEDED.getMessage())
                    .isEqualTo("장바구니 아이템 수가 최대 한도를 초과했습니다");
        }

        @Test
        @DisplayName("INSUFFICIENT_STOCK 에러 코드를 검증한다")
        void insufficientStock() {
            // then
            assertThat(CartErrorCode.INSUFFICIENT_STOCK.getCode()).isEqualTo("CART-004");
            assertThat(CartErrorCode.INSUFFICIENT_STOCK.getHttpStatus()).isEqualTo(400);
            assertThat(CartErrorCode.INSUFFICIENT_STOCK.getMessage()).isEqualTo("재고가 부족합니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(CartErrorCode.values())
                    .containsExactly(
                            CartErrorCode.CART_ITEM_NOT_FOUND,
                            CartErrorCode.CART_ITEM_ALREADY_EXISTS,
                            CartErrorCode.CART_ITEM_LIMIT_EXCEEDED,
                            CartErrorCode.INSUFFICIENT_STOCK);
        }
    }
}
