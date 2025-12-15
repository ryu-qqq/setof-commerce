package com.setof.connectly.module.order.entity.snapshot.group.embedded;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotProduct {
    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Embedded private ProductStatus productStatus;

    public SnapShotProduct(long productId, long productGroupId) {
        this.productId = productId;
        this.productGroupId = productGroupId;
        this.productStatus = new ProductStatus(Yn.N, Yn.Y);
    }
}
