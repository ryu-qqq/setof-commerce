package com.setof.connectly.module.user.mapper.shipping;

import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import com.setof.connectly.module.utils.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class ShippingAddressMapperImpl implements ShippingAddressMapper {

    @Override
    public ShippingAddress toEntity(UserShippingInfo userShippingInfo) {
        return ShippingAddress.builder()
                .userId(SecurityUtils.currentUserId())
                .shippingDetails(userShippingInfo.getShippingDetails())
                .defaultYn(userShippingInfo.getDefaultYn())
                .build();
    }
}
