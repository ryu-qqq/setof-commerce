package com.connectly.partnerAdmin.module.order.entity.snapshot.image.embedded;

import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.embedded.ImageDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapShotProductGroupDetailDescription {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Embedded
    private ImageDetail imageDetail;

    public SnapShotProductGroupDetailDescription(ProductGroupDetailDescription productGroupDetailDescription) {
        this.productGroupId = productGroupDetailDescription.getId();
        this.imageDetail = productGroupDetailDescription.getImageDetail();
    }

}
