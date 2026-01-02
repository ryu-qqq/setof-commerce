package com.connectly.partnerAdmin.module.product.core;


import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseProductThumbnail implements ProductThumbnail {

    private int displayOrder;
    protected long productGroupId;
    protected long sellerId;
    protected String productGroupName;
    protected BaseBrandContext brand;
    protected String productImageUrl;

    @Setter
    protected Price price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime insertDate;
    protected ProductStatus productStatus;

    protected double averageRating;
    protected long reviewCount;
    protected double score;

    @Setter
    protected boolean isFavorite;

    @QueryProjection
    public BaseProductThumbnail(long productGroupId, long sellerId, String productGroupName, BaseBrandContext brand, String productImageUrl, Price price, LocalDateTime insertDate, ProductStatus productStatus, double averageRating, long reviewCount, double score, Long favoriteId) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.productGroupName = productGroupName;
        this.brand = brand;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.insertDate = insertDate;
        this.productStatus = productStatus;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.score = score;
        this.isFavorite = favoriteId != null;
    }

    @Override
    public Long getId() {
        return productGroupId;
    }

    @Override
    public boolean isNoOffsetFetch() {
        return false;
    }

}