package com.connectly.partnerAdmin.module.external.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerExternalIntegrationDto {

    private long sellerId;
    private List<Long> siteIds;

    @QueryProjection
    public SellerExternalIntegrationDto(long sellerId, List<Long> siteIds) {
        this.sellerId = sellerId;
        this.siteIds = siteIds;
    }
}
