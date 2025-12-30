package com.connectly.partnerAdmin.module.product.entity.score;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "PRODUCT_RATING_STATS")
@Entity
public class ProductRatingStats extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;
    private double averageRating;
    private long reviewCount;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    public ProductRatingStats(double averageRating, long reviewCount) {
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null && this.productGroup.equals(productGroup)) {
            return;
        }
        this.productGroup = productGroup;
        if (productGroup != null && !productGroup.getProductRatingStats().equals(this)) {
            productGroup.setProductRatingStats(this);
        }
    }

}
