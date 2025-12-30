package com.connectly.partnerAdmin.module.product.entity.group.embedded;

import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroupDetails {

    @Column(name = "PRODUCT_GROUP_NAME", length = 100, nullable = false)
    private String productGroupName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "OPTION_TYPE", length = 20, nullable = false)
    private OptionType optionType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "MANAGEMENT_TYPE", length = 15, nullable = false)
    private ManagementType managementType;

    @Setter
    @Embedded
    private Price price;

    @Embedded
    private ProductStatus productStatus;

    @Embedded
    private ClothesDetail clothesDetailInfo;

    @Column(name = "SELLER_ID")
    private long sellerId;

    @Column(name = "CATEGORY_ID")
    private long categoryId;

    @Column(name = "BRAND_ID")
    private long brandId;

    @Column(name = "EXTERNAL_PRODUCT_UUID", unique = true, nullable = true, length = 36)
    private String externalProductUuId;

    public void setProductGroupDetails(ProductGroupDetails productGroupDetails){
        Origin origin = productGroupDetails.getClothesDetailInfo().getOrigin();
        if(origin == null) productGroupDetails.getClothesDetailInfo().setOrigin(Origin.OTHER);

        this.productGroupName = productGroupDetails.getProductGroupName();
        this.managementType = productGroupDetails.getManagementType();
        this.clothesDetailInfo = productGroupDetails.getClothesDetailInfo();
        this.categoryId = productGroupDetails.getCategoryId();
        this.brandId = productGroupDetails.getBrandId();
        this.productStatus = productGroupDetails.getProductStatus();
        this.optionType = productGroupDetails.getOptionType();
    }

    @QueryProjection
    public ProductGroupDetails(String productGroupName, OptionType optionType, ManagementType managementType, Price price, ProductStatus productStatus, ClothesDetail clothesDetailInfo, long sellerId, long categoryId, long brandId, String externalProductUuId) {
        this.productGroupName = productGroupName;
        this.optionType = optionType;
        this.managementType = managementType;
        this.price = price;
        this.productStatus = productStatus;
        this.clothesDetailInfo = clothesDetailInfo;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.externalProductUuId = externalProductUuId;
    }
}
