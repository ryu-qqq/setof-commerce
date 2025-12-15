package com.setof.connectly.module.user.service.shipping.query;

import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;

public interface ShippingAddressQueryService {
    UserShippingInfo toCreateUserShippingInfo(UserShippingInfo userShippingInfo);

    UserShippingInfo updateUserShippingInfo(
            long shippingAddressId, UserShippingInfo userShippingInfo);

    ShippingAddress deleteUserShippingInfo(long shippingAddressId);
}
