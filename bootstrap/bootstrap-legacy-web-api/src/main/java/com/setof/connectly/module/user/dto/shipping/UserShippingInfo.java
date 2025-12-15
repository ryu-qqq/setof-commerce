package com.setof.connectly.module.user.dto.shipping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.entity.embedded.ShippingDetails;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserShippingInfo {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long shippingAddressId;

    private ShippingDetails shippingDetails;
    private Yn defaultYn;

    @QueryProjection
    public UserShippingInfo(Long shippingAddressId, ShippingDetails shippingDetails, Yn defaultYn) {
        this.shippingAddressId = shippingAddressId;
        this.shippingDetails = shippingDetails;
        this.defaultYn = defaultYn;
    }

    public Yn getDefaultYn() {
        if (this.defaultYn == null) return Yn.N;
        return defaultYn;
    }

    @JsonIgnore
    public boolean isDefaultShipping() {
        return defaultYn.isYes();
    }
}
