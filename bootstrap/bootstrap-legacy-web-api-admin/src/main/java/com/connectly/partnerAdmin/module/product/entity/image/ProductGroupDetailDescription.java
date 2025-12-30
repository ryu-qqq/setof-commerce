package com.connectly.partnerAdmin.module.product.entity.image;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.image.embedded.ImageDetail;
import lombok.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "PRODUCT_GROUP_DETAIL_DESCRIPTION")
public class ProductGroupDetailDescription extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    @Setter
    @Embedded
    private ImageDetail imageDetail;

    @Column(name = "IMAGE_URLS", columnDefinition = "TEXT")
    private String imageUrls;

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null && this.productGroup.equals(productGroup)) {
            return;
        }
        this.productGroup = productGroup;
        if (productGroup != null && !productGroup.getDetailDescription().equals(this)) {
            productGroup.setDetailDescription(this);
        }
    }

    public void updateImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Set<String> getImageUrlSet() {
        if (imageUrls == null || imageUrls.isBlank()) {
            return new HashSet<>();
        }
        Set<String> urls = new HashSet<>();
        for (String url : imageUrls.split(",")) {
            String trimmed = url.trim();
            if (!trimmed.isEmpty()) {
                urls.add(trimmed);
            }
        }
        return urls;
    }

}
