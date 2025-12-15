package com.setof.connectly.module.product.dto.group;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.group.Origin;
import com.setof.connectly.module.product.enums.group.ProductCondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesDetailDto {

    private ProductCondition productCondition;
    private String origin;

    @QueryProjection
    public ClothesDetailDto(ProductCondition productCondition, Origin origin) {
        this.productCondition = productCondition;
        this.origin = origin.getDisplayName();
    }
}
