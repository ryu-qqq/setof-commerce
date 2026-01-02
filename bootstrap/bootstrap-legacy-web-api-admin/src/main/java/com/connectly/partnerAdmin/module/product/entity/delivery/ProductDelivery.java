package com.connectly.partnerAdmin.module.product.entity.delivery;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.DeliveryNotice;
import com.connectly.partnerAdmin.module.product.entity.delivery.embedded.RefundNotice;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "PRODUCT_DELIVERY")
@Entity
public class ProductDelivery extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;

    @Setter
    @Embedded
    private DeliveryNotice deliveryNotice;

    @Setter
    @Embedded
    private RefundNotice refundNotice;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null && this.productGroup.equals(productGroup)) {
            return;
        }
        this.productGroup = productGroup;
        if (productGroup != null && !productGroup.getProductDelivery().equals(this)) {
            productGroup.setProductDelivery(this);
        }
    }


}
