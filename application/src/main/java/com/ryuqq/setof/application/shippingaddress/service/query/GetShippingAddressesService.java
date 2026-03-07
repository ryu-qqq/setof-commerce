package com.ryuqq.setof.application.shippingaddress.service.query;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;
import com.ryuqq.setof.domain.shippingaddress.query.ShippingAddressSearchCondition;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetShippingAddressesService - 배송지 목록 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetShippingAddressesService implements GetShippingAddressesUseCase {

    private final ShippingAddressReadManager readManager;

    public GetShippingAddressesService(ShippingAddressReadManager readManager) {
        this.readManager = readManager;
    }

    @Override
    public List<ShippingAddressResult> execute(Long userId) {
        ShippingAddressSearchCondition condition = ShippingAddressSearchCondition.ofUserId(userId);
        return readManager.fetchShippingAddresses(condition);
    }
}
