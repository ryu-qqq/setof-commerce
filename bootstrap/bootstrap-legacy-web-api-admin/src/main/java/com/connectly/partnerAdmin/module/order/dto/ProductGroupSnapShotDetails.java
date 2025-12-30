package com.connectly.partnerAdmin.module.order.dto;

import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupSnapShotDetails {

    private String productGroupName;
    private OptionType optionType;
    private ManagementType managementType;
    @Embedded
    private Price price;
    @Embedded
    private ProductStatus productStatus;

    @Embedded
    private ClothesDetail clothesDetailInfo;

    private long sellerId;

    private long categoryId;

    private long brandId;

    @QueryProjection
    public ProductGroupSnapShotDetails(String productGroupName, OptionType optionType, ManagementType managementType, Price price, ProductStatus productStatus, ClothesDetail clothesDetailInfo, long sellerId, long categoryId, long brandId) {
        this.productGroupName = productGroupName;
        this.optionType = optionType;
        this.managementType = managementType;
        this.price = price;
        this.productStatus = productStatus;
        this.clothesDetailInfo = clothesDetailInfo;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.brandId = brandId;
    }
}
