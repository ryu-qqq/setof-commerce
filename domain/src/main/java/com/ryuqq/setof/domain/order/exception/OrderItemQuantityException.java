package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * OrderItemQuantityException - 주문 상품 수량 예외
 *
 * <p>취소/환불 수량이 가용 수량을 초과하는 경우 발생합니다.
 */
public class OrderItemQuantityException extends DomainException {

    /** 취소 수량 초과 예외 */
    public static OrderItemQuantityException cancelExceedsAvailable(int requested, int available) {
        return new OrderItemQuantityException(
                String.format("취소 수량 초과 - 요청: %d, 가용: %d", requested, available),
                requested,
                available);
    }

    /** 환불 수량 초과 예외 */
    public static OrderItemQuantityException refundExceedsAvailable(int requested, int available) {
        return new OrderItemQuantityException(
                String.format("환불 수량 초과 - 요청: %d, 가용: %d", requested, available),
                requested,
                available);
    }

    private OrderItemQuantityException(String message, int requested, int available) {
        super(
                OrderErrorCode.QUANTITY_EXCEEDS_AVAILABLE,
                message,
                Map.of("requested", requested, "available", available));
    }
}
