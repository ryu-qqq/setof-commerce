package com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.embedded;

import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotProductDelivery {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;
    @Embedded
    private DeliveryNotice deliveryNotice;
    @Embedded
    private RefundNotice refundNotice;

    public SnapShotProductDelivery(ProductDelivery productDelivery) {
        this.productGroupId = productDelivery.getId();
        this.deliveryNotice = productDelivery.getDeliveryNotice();
        this.refundNotice = productDelivery.getRefundNotice();
    }

}
