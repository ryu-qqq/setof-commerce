package com.ryuqq.setof.domain.cart.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartException 단위 테스트")
class CartExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            CartException exception = new CartException(CartErrorCode.CART_ITEM_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("장바구니 아이템을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("CART-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            CartException exception =
                    new CartException(CartErrorCode.CART_ITEM_NOT_FOUND, "ID 100 장바구니 아이템 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 100 장바구니 아이템 없음");
            assertThat(exception.code()).isEqualTo("CART-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            CartException exception = new CartException(CartErrorCode.CART_ITEM_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("CART-001");
        }

        @Test
        @DisplayName("CART_ITEM_ALREADY_EXISTS ErrorCode로 예외를 생성한다")
        void createWithAlreadyExistsErrorCode() {
            // when
            CartException exception = new CartException(CartErrorCode.CART_ITEM_ALREADY_EXISTS);

            // then
            assertThat(exception.code()).isEqualTo("CART-002");
            assertThat(exception.httpStatus()).isEqualTo(409);
            assertThat(exception.getMessage()).isEqualTo("이미 장바구니에 존재하는 상품입니다");
        }

        @Test
        @DisplayName("CART_ITEM_LIMIT_EXCEEDED ErrorCode로 예외를 생성한다")
        void createWithLimitExceededErrorCode() {
            // when
            CartException exception = new CartException(CartErrorCode.CART_ITEM_LIMIT_EXCEEDED);

            // then
            assertThat(exception.code()).isEqualTo("CART-003");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("장바구니 아이템 수가 최대 한도를 초과했습니다");
        }
    }

    @Nested
    @DisplayName("CartItemNotFoundException 구체 예외 테스트")
    class CartItemNotFoundExceptionTest {

        @Test
        @DisplayName("기본 생성자로 CartItemNotFoundException을 생성한다")
        void createCartItemNotFoundException() {
            // when
            CartItemNotFoundException exception = new CartItemNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("CART-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("장바구니 아이템을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ID 포함 메시지로 CartItemNotFoundException을 생성한다")
        void createCartItemNotFoundExceptionWithId() {
            // when
            CartItemNotFoundException exception = new CartItemNotFoundException(42L);

            // then
            assertThat(exception.code()).isEqualTo("CART-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("ID가 42인 장바구니 아이템을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("CartException은 DomainException을 상속한다")
        void cartExceptionExtendsDomainException() {
            // given
            CartException exception = new CartException(CartErrorCode.CART_ITEM_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("CartItemNotFoundException은 CartException을 상속한다")
        void cartItemNotFoundExceptionExtendsCartException() {
            // given
            CartItemNotFoundException exception = new CartItemNotFoundException();

            // then
            assertThat(exception).isInstanceOf(CartException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
