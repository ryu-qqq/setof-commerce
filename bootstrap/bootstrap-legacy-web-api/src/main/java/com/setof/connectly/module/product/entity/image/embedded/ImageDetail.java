package com.setof.connectly.module.product.entity.image.embedded;

import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ImageDetail {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "product_group_image_type")
    private ProductGroupImageType productGroupImageType;

    @Column(name = "image_url")
    private String imageUrl;

    public ImageDetail(ProductGroupImageType productGroupImageType, String imageUrl) {
        this.productGroupImageType = productGroupImageType;
        this.imageUrl = imageUrl;
    }
}
