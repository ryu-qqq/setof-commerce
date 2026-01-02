package com.setof.connectly.module.order.entity.snapshot.group.embedded;

import com.setof.connectly.module.product.entity.group.embedded.ClothesDetail;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductGroupDetails;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SnapShotProductGroup {

    @Column(name = "product_group_id")
    private long productGroupId;

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

    @Column(name = "management_type")
    @Enumerated(value = EnumType.STRING)
    private ManagementType managementType;

    @Embedded private Price price;

    @Embedded private ProductStatus productStatus;

    @Embedded private ClothesDetail clothesDetailInfo;

    @Column(name = "commission_rate")
    private double commissionRate;

    @Column(name = "share_ratio")
    private double shareRatio;

    public SnapShotProductGroup(
            long productGroupId, ProductGroupDetails productGroupDetails, double commissionRate) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupDetails.getProductGroupName();
        this.sellerId = productGroupDetails.getSellerId();
        this.brandId = productGroupDetails.getBrandId();
        this.categoryId = productGroupDetails.getCategoryId();
        this.optionType = productGroupDetails.getOptionType();
        this.managementType = productGroupDetails.getManagementType();
        this.price = productGroupDetails.getPrice();
        this.productStatus = productGroupDetails.getProductStatus();
        this.clothesDetailInfo = productGroupDetails.getClothesDetailInfo();
        this.commissionRate = commissionRate;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setShareRatio(double shareRatio) {
        this.shareRatio = shareRatio;
    }
}
