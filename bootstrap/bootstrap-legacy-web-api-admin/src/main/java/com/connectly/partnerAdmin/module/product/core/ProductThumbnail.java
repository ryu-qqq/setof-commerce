package com.connectly.partnerAdmin.module.product.core;

import com.connectly.partnerAdmin.module.brand.core.BrandContext;
import com.connectly.partnerAdmin.module.common.filter.CursorValueProvider;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;

import java.time.LocalDateTime;

public interface ProductThumbnail extends PriceHolder, ScoreContext, ProductRatingContext, CursorValueProvider {

    long getProductGroupId();
    long getSellerId();
    BrandContext getBrand();
    String getProductImageUrl();
    ProductStatus getProductStatus();
    LocalDateTime getInsertDate();

}

