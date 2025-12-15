package com.setof.connectly.module.exception.order;

import com.setof.connectly.module.order.enums.OrderStatus;
import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends OrderException {

    public static final String CODE = "ORDER-400";
    private static final String BASE_MESSAGE = "해당 주문의 주문 상태를 변경 할 수 없습니다.";

    public InvalidOrderStatusException(
            long orderId, OrderStatus pastOrderStatus, OrderStatus toOrderStatus) {
        super(CODE, HttpStatus.BAD_REQUEST, buildMessage(orderId, pastOrderStatus, toOrderStatus));
    }

    private static String buildMessage(
            long orderId, OrderStatus pastOrderStatus, OrderStatus toOrderStatus) {
        return String.format(
                "%s 주문 번호 %d 현재 주문 상태 %s 변경 불가 상태 %s",
                BASE_MESSAGE,
                orderId,
                pastOrderStatus.getDisplayName(),
                toOrderStatus.getDisplayName());
    }
}
