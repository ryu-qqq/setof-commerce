package com.connectly.partnerAdmin.module.product.entity.group.embedded;


import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesDetail {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PRODUCT_CONDITION", length = 15, nullable = false)
    private ProductCondition productCondition;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "ORIGIN", length = 20, nullable = false)
    private Origin origin;

    @Column(name = "STYLE_CODE", length = 50)
    private String styleCode;

    @Builder
    public ClothesDetail(ProductCondition productCondition, Origin origin, String styleCode) {
        this.productCondition = productCondition;
        this.origin = origin != null ? origin : Origin.OTHER;
        this.styleCode = styleCode != null ? styleCode : "";
    }

}
