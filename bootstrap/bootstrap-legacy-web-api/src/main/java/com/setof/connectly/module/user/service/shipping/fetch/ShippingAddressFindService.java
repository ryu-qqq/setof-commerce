package com.setof.connectly.module.user.service.shipping.fetch;

import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import java.util.List;
import java.util.Optional;

public interface ShippingAddressFindService {

    List<UserShippingInfo> fetchShippingInfo();

    UserShippingInfo fetchShippingInfo(long shippingAddressId);

    ShippingAddress fetchShippingAddressEntity(long shippingAddressId);

    Optional<ShippingAddress> fetchDefaultShippingAddressEntity();
}
