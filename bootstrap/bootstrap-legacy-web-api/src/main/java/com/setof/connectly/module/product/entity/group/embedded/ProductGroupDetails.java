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
    @Column(name = "PRODUCT_GROUP_NAME")
    private String productGroupName;

    @Column(name = "SELLER_ID")
    private long sellerId;

    @Column(name = "BRAND_ID")
    private long brandId;

    @Column(name = "CATEGORY_ID")
    private long categoryId;

    @Column(name = "OPTION_TYPE")
    @Enumerated(value = EnumType.STRING)
    private OptionType optionType;

    @Enumerated(value = EnumType.STRING)
    private ManagementType managementType;

    @Embedded private Price price;

    @Embedded private ProductStatus productStatus;

    @Embedded private ClothesDetail clothesDetailInfo;
}
