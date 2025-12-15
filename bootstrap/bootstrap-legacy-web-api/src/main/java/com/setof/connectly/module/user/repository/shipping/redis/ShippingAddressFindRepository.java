package com.setof.connectly.module.user.repository.shipping.redis;

import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import java.util.List;
import java.util.Optional;

public interface ShippingAddressFindRepository {

    List<UserShippingInfo> fetchShippingAddress(long userId);

    Optional<ShippingAddress> fetchShippingAddressEntity(long userId, long shippingAddressId);

    Optional<ShippingAddress> fetchDefaultShippingAddressEntity(long userId);
}
