package com.ryuqq.setof.domain.order.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OrderErrorCode 단위 테스트")
class OrderErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(OrderErrorCode.ORDER_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("주문 에러 코드 값 검증 테스트")
    class OrderErrorCodesTest {

        @Test
        @DisplayName("ORDER_NOT_FOUND 에러 코드를 검증한다")
        void orderNotFound() {
            assertThat(OrderErrorCode.ORDER_NOT_FOUND.getCode()).isEqualTo("ORD-001");
            assertThat(OrderErrorCode.ORDER_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(OrderErrorCode.ORDER_NOT_FOUND.getMessage()).isEqualTo("주문을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("ORDER_ITEM_NOT_FOUND 에러 코드를 검증한다")
        void orderItemNotFound() {
            assertThat(OrderErrorCode.ORDER_ITEM_NOT_FOUND.getCode()).isEqualTo("ORD-002");
            assertThat(OrderErrorCode.ORDER_ITEM_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(OrderErrorCode.ORDER_ITEM_NOT_FOUND.getMessage())
                    .isEqualTo("주문 아이템을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("INVALID_ORDER_STATUS_TRANSITION 에러 코드를 검증한다")
        void invalidOrderStatusTransition() {
            assertThat(OrderErrorCode.INVALID_ORDER_STATUS_TRANSITION.getCode())
                    .isEqualTo("ORD-003");
            assertThat(OrderErrorCode.INVALID_ORDER_STATUS_TRANSITION.getHttpStatus())
                    .isEqualTo(400);
            assertThat(OrderErrorCode.INVALID_ORDER_STATUS_TRANSITION.getMessage())
                    .isEqualTo("유효하지 않은 주문 상태 전이입니다");
        }

        @Test
        @DisplayName("INVALID_ORDER_ITEM_STATUS_TRANSITION 에러 코드를 검증한다")
        void invalidOrderItemStatusTransition() {
            assertThat(OrderErrorCode.INVALID_ORDER_ITEM_STATUS_TRANSITION.getCode())
                    .isEqualTo("ORD-004");
            assertThat(OrderErrorCode.INVALID_ORDER_ITEM_STATUS_TRANSITION.getHttpStatus())
                    .isEqualTo(400);
            assertThat(OrderErrorCode.INVALID_ORDER_ITEM_STATUS_TRANSITION.getMessage())
                    .isEqualTo("유효하지 않은 주문 아이템 상태 전이입니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(OrderErrorCode.values())
                    .containsExactly(
                            OrderErrorCode.ORDER_NOT_FOUND,
                            OrderErrorCode.ORDER_ITEM_NOT_FOUND,
                            OrderErrorCode.INVALID_ORDER_STATUS_TRANSITION,
                            OrderErrorCode.INVALID_ORDER_ITEM_STATUS_TRANSITION);
        }
    }
}
