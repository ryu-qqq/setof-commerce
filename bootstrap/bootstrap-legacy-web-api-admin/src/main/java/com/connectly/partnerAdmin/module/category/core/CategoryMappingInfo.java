package com.connectly.partnerAdmin.module.category.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryMappingInfo {

    @JsonProperty("mapping_category_id")
    private String categoryMappingId;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("category_id")
    @Setter
    private Long categoryId;

}
