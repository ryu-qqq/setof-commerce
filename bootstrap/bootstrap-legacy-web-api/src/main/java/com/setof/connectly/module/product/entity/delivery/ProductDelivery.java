package com.setof.connectly.module.product.entity.delivery;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.delivery.embedded.RefundNotice;
import com.setof.connectly.module.product.entity.group.ProductGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Embedded private DeliveryNotice deliveryNotice;
    @Embedded private RefundNotice refundNotice;

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
