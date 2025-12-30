package com.connectly.partnerAdmin.module.product.entity.score;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "PRODUCT_SCORE")
@Entity
public class ProductScore extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;

    @Column(name = "SCORE")
    private double score;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    public ProductScore(double score) {
        this.score = score;
    }

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null && this.productGroup.equals(productGroup)) {
            return;
        }
        this.productGroup = productGroup;
        if (productGroup != null && !productGroup.getProductScore().equals(this)) {
            productGroup.setProductScore(this);
        }
    }

}