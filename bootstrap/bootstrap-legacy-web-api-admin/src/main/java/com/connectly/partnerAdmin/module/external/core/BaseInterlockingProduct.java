package com.connectly.partnerAdmin.module.external.core;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseInterlockingProduct implements InterlockingProduct {

    private long productGroupId;
    private long sellerId;
    private long externalIdx;

    @QueryProjection
    public BaseInterlockingProduct(long productGroupId, long sellerId) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
    }

}
