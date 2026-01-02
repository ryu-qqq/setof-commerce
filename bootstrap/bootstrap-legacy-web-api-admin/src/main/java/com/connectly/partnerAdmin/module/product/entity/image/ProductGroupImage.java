package com.connectly.partnerAdmin.module.product.entity.image;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.image.embedded.ImageDetail;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "PRODUCT_GROUP_IMAGE")
@Entity
public class ProductGroupImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_GROUP_IMAGE_ID")
    private long id;

    @Embedded
    private ImageDetail imageDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_GROUP_ID", referencedColumnName = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;


    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null) {
            this.productGroup.getImages().remove(this);
        }

        this.productGroup = productGroup;

        if (productGroup != null && !productGroup.getImages().contains(this)) {
            productGroup.getImages().add(this);
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductGroupImage that = (ProductGroupImage) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(imageDetail, that.getImageDetail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageDetail);
    }


    public void delete(){
        this.setDeleteYn(Yn.Y);
    }

}
