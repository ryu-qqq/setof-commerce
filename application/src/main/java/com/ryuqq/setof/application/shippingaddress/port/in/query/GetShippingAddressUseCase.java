package com.ryuqq.setof.application.shippingaddress.port.in.query;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;

/**
 * 배송지 단건 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetShippingAddressUseCase {

    ShippingAddressResult execute(Long userId, Long shippingAddressId);
}
