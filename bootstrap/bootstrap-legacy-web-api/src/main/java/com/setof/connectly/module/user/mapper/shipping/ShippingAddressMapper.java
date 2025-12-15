package com.setof.connectly.module.user.mapper.shipping;

import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;

public interface ShippingAddressMapper {

    ShippingAddress toEntity(UserShippingInfo userShippingInfo);
}
