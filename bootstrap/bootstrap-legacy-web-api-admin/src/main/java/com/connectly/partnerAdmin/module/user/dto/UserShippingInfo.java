package com.connectly.partnerAdmin.module.user.dto;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.user.entity.embedded.ShippingDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserShippingInfo {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long shippingAddressId;
    private ShippingDetails shippingDetails;
    private Yn defaultYn;

    public UserShippingInfo(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
        this.defaultYn = Yn.N;
    }

    @QueryProjection
    public UserShippingInfo(Long shippingAddressId, ShippingDetails shippingDetails, Yn defaultYn) {
        this.shippingAddressId = shippingAddressId;
        this.shippingDetails = shippingDetails;
        this.defaultYn = defaultYn;
    }

}