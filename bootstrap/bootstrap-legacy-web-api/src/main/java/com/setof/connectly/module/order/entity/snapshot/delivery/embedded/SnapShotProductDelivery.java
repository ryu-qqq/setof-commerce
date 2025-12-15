package com.setof.connectly.module.order.entity.snapshot.delivery.embedded;

import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.delivery.embedded.RefundNotice;
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
public class SnapShotProductDelivery {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Embedded private DeliveryNotice deliveryNotice;
    @Embedded private RefundNotice refundNotice;
}
