package com.setof.connectly.module.product.entity.group.embedded;

import com.setof.connectly.module.product.enums.group.Origin;
import com.setof.connectly.module.product.enums.group.ProductCondition;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesDetail {

    @Enumerated(value = EnumType.STRING)
    private ProductCondition productCondition;

    @Enumerated(value = EnumType.STRING)
    private Origin origin;

    @Builder
    public ClothesDetail(ProductCondition productCondition, Origin origin) {
        this.productCondition = productCondition;
        this.origin = origin;
    }
}
