package com.connectly.partnerAdmin.module.product.entity.score;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "product_score")
@Entity
public class ProductScore extends BaseEntity {

    @Id
    @Column(name = "product_group_id")
    private long id;

    @Column(name = "score")
    private double score;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_group_id")
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