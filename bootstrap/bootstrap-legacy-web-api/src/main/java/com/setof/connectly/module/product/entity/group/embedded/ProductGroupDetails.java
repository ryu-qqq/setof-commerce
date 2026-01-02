package com.setof.connectly.module.product.entity.group.embedded;

import com.setof.connectly.module.product.enums.group.ManagementType;
import com.setof.connectly.module.product.enums.option.OptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupDetails {
    @Column(name = "product_group_name")
    private String productGroupName;

    @Column(name = "seller_id")
    private long sellerId;

    @Column(name = "brand_id")
    private long brandId;

    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "option_type")
    @Enumerated(value = EnumType.STRING)
    private OptionType optionType;

    @Enumerated(value = EnumType.STRING)
    private ManagementType managementType;

    @Embedded private Price price;

    @Embedded private ProductStatus productStatus;

    @Embedded private ClothesDetail clothesDetailInfo;
}
