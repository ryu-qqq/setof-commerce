package com.connectly.partnerAdmin.module.order.dto;

import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSnapShotProductGroupQueryDto {
    private ProductGroup productGroup;
    private Product product;
    private ProductDelivery productDelivery;
    private ProductNotice productNotice;
    private ProductGroupDetailDescription productGroupDetailDescription;
    private Set<ProductGroupImage> productGroupImageList;

    @QueryProjection
    public OrderSnapShotProductGroupQueryDto(ProductGroup productGroup, Product product, ProductDelivery productDelivery, ProductNotice productNotice, ProductGroupDetailDescription productGroupDetailDescription, Set<ProductGroupImage> productGroupImageList) {
        this.productGroup = productGroup;
        this.product = product;
        this.productDelivery = productDelivery;
        this.productNotice = productNotice;
        this.productGroupDetailDescription = productGroupDetailDescription;
        this.productGroupImageList = productGroupImageList;
    }

}
