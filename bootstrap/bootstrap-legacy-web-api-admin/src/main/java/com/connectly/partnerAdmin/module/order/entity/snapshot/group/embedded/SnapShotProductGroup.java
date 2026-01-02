package com.connectly.partnerAdmin.module.order.entity.snapshot.group.embedded;

import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ClothesDetail;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductGroupDetails;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;
import lombok.*;
import jakarta.persistence.*;





@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapShotProductGroup {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

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

    @Embedded
    private Price price;

    @Embedded
    private ProductStatus productStatus;

    @Embedded
    private ClothesDetail clothesDetailInfo;

    public SnapShotProductGroup(ProductGroup productGroup){
        ProductGroupDetails productGroupDetails = productGroup.getProductGroupDetails();
        this.productGroupId = productGroup.getId();
        this.productGroupName = productGroupDetails.getProductGroupName();
        this.sellerId = productGroupDetails.getSellerId();
        this.brandId = productGroupDetails.getBrandId();
        this.categoryId = productGroupDetails.getCategoryId();
        this.optionType = productGroupDetails.getOptionType();
        this.managementType = productGroupDetails.getManagementType();
        this.price = productGroupDetails.getPrice();
        this.productStatus = productGroupDetails.getProductStatus();
        this.clothesDetailInfo = productGroupDetails.getClothesDetailInfo();
    }

}
