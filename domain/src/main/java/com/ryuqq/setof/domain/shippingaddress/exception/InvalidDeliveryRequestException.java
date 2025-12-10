package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 배송 요청사항 예외
 *
 * <p>배송 요청사항이 200자를 초과하는 경우 발생합니다.
 */
public class InvalidDeliveryRequestException extends DomainException {

    public InvalidDeliveryRequestException(String deliveryRequest) {
        super(
                ShippingAddressErrorCode.INVALID_DELIVERY_REQUEST,
                String.format(
                        "배송 요청사항이 최대 길이(200자)를 초과했습니다. 현재 길이: %d",
                        deliveryRequest != null ? deliveryRequest.length() : 0),
                Map.of("currentLength", deliveryRequest != null ? deliveryRequest.length() : 0));
    }
}
