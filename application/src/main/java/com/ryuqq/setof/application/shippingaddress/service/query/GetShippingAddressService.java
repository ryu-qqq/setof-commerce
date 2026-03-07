package com.ryuqq.setof.application.shippingaddress.service.query;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import org.springframework.stereotype.Service;

/**
 * GetShippingAddressService - 배송지 단건 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetShippingAddressService implements GetShippingAddressUseCase {

    private final ShippingAddressReadManager readManager;

    public GetShippingAddressService(ShippingAddressReadManager readManager) {
        this.readManager = readManager;
    }

    @Override
    public ShippingAddressResult execute(Long userId, Long shippingAddressId) {
        return readManager.fetchShippingAddress(userId, shippingAddressId);
    }
}
