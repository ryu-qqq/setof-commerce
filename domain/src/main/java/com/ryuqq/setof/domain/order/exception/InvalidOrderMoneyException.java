package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/** InvalidOrderMoneyException - 잘못된 주문 금액 예외 */
public class InvalidOrderMoneyException extends DomainException {

    public InvalidOrderMoneyException(BigDecimal value, String message) {
        super(
                OrderErrorCode.INVALID_ORDER_MONEY,
                message,
                Map.of("value", value != null ? value.toString() : "null"));
    }
}
