package com.connectly.partnerAdmin.module.external.dto.order;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalOrderMappingDto {
    private long orderId;
    private long siteId;
    private long externalIdx;
    private String externalOrderPkId;

    @QueryProjection
    public ExternalOrderMappingDto(long orderId, long siteId, long externalIdx, String externalOrderPkId) {
        this.orderId = orderId;
        this.siteId = siteId;
        this.externalIdx = externalIdx;
        this.externalOrderPkId = externalOrderPkId;
    }
}
