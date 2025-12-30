package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 주문 할인 정보 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>DomainException 상속
 * </ul>
 */
public class InvalidOrderDiscountException extends DomainException {

    /**
     * 예외 생성자
     *
     * @param message 예외 메시지
     */
    public InvalidOrderDiscountException(String message) {
        super(OrderErrorCode.INVALID_ORDER_DISCOUNT, message);
    }

    /**
     * 예외 생성자 - 상세 정보 포함
     *
     * @param fieldName 필드명
     * @param message 예외 메시지
     */
    public InvalidOrderDiscountException(String fieldName, String message) {
        super(
                OrderErrorCode.INVALID_ORDER_DISCOUNT,
                String.format("%s: %s", fieldName, message),
                Map.of("field", fieldName));
    }
}
