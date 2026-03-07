package com.ryuqq.setof.application.shippingaddress.port.in.query;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import java.util.List;

/**
 * 배송지 목록 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetShippingAddressesUseCase {

    List<ShippingAddressResult> execute(Long userId);
}
