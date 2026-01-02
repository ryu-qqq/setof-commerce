package com.setof.connectly.module.product.entity.image;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.entity.group.ProductGroup;
import com.setof.connectly.module.product.entity.image.embedded.ImageDetail;
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
@Entity
@Table(name = "product_group_detail_description")
public class ProductGroupDetailDescription extends BaseEntity {

    @Id
    @Column(name = "product_group_id")
    private Long id;

    @Embedded private ImageDetail imageDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null && this.productGroup.equals(productGroup)) {
            return;
        }
        this.productGroup = productGroup;
        if (productGroup != null && !productGroup.getDetailDescription().equals(this)) {
            productGroup.setDetailDescription(this);
        }
    }
}
