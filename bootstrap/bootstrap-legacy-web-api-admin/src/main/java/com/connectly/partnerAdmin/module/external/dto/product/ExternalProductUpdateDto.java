package com.connectly.partnerAdmin.module.external.dto.product;

import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalProductUpdateDto extends ExternalProductMappingDto{
    private MappingStatus mappingStatus;


    public ExternalProductUpdateDto(long productGroupId, String externalIdx, MappingStatus mappingStatus) {
        super(productGroupId, externalIdx);
        this.mappingStatus = mappingStatus;
    }
}
