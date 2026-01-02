package com.connectly.partnerAdmin.module.order.entity.snapshot.group.embedded;

import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;



@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotProduct {

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;
    @Embedded
    private ProductStatus productStatus;

    public SnapShotProduct(Product product){
        this.productId = product.getId();
        this.productGroupId = product.getProductGroup().getId();
        this.productStatus = product.getProductStatus();
    }

}
