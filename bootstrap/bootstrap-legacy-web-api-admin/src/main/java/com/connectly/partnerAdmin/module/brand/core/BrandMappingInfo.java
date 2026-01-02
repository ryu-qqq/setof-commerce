package com.connectly.partnerAdmin.module.brand.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandMappingInfo {

    @JsonProperty("mapping_brand_id")
    private String brandMappingId;
    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("brand_id")
    @Setter
    private Long brandId;

}
