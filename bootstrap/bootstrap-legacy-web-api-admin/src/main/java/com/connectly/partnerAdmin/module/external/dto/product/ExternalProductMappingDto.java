package com.connectly.partnerAdmin.module.external.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalProductMappingDto {
    private long productGroupId;
    private String externalIdx;

    @QueryProjection
    public ExternalProductMappingDto(long productGroupId, String externalIdx) {
        this.productGroupId = productGroupId;
        this.externalIdx = externalIdx;
    }
}
