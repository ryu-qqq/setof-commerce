package com.ryuqq.setof.application.shippingaddress.port.out;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.query.ShippingAddressSearchCondition;
import java.util.List;
import java.util.Optional;

/**
 * ShippingAddressQueryPort - 배송지 조회 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ShippingAddressQueryPort {

    List<ShippingAddressResult> fetchShippingAddresses(ShippingAddressSearchCondition condition);

    Optional<ShippingAddressResult> fetchShippingAddress(ShippingAddressSearchCondition condition);

    Optional<ShippingAddress> findById(Long userId, Long shippingAddressId);

    List<ShippingAddress> findAllByUserId(Long userId);

    int countByUserId(Long userId);
}
