package com.connectly.partnerAdmin.module.product.entity.image.embedded;

import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ImageDetail  {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "PRODUCT_GROUP_IMAGE_TYPE")
    private ProductGroupImageType productGroupImageType;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "ORIGIN_URL")
    private String originUrl;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    public ImageDetail(ProductGroupImageType productGroupImageType, String imageUrl) {
        this.productGroupImageType = productGroupImageType;
        this.imageUrl = imageUrl;
    }

    public ImageDetail(ProductGroupImageType productGroupImageType, String imageUrl, String originUrl, Integer displayOrder) {
        this.productGroupImageType = productGroupImageType;
        this.imageUrl = imageUrl;
        this.originUrl = originUrl;
        this.displayOrder = displayOrder;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void updateOriginUrl(String originUrl) {
        if (this.originUrl == null && originUrl != null) {
            this.originUrl = originUrl;
        }
    }

    public boolean isSameOrigin(String otherOriginUrl) {
        if (this.originUrl == null || otherOriginUrl == null) {
            return false;
        }
        return this.originUrl.equals(otherOriginUrl);
    }
}
