package com.setof.connectly.module.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.mapper.CursorValueProvider;
import com.setof.connectly.module.discount.DiscountOffer;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class ProductGroupThumbnail implements DiscountOffer, CursorValueProvider {

    protected long productGroupId;
    protected long sellerId;
    protected String productGroupName;
    protected BrandDto brand;
    protected String productImageUrl;
    protected Price price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime insertDate;

    protected double averageRating;
    protected long reviewCount;
    protected double score;
    protected boolean isFavorite;
    protected ProductStatus productStatus;

    @QueryProjection
    public ProductGroupThumbnail(
            long productGroupId,
            long sellerId,
            String productGroupName,
            BrandDto brand,
            String productImageUrl,
            Price price,
            LocalDateTime insertDate,
            double averageRating,
            Long reviewCount,
            Double score,
            ProductStatus productStatus) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.productGroupName = productGroupName;
        this.brand = brand;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.insertDate = insertDate;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.score = score;
        this.isFavorite = false;
        this.productStatus = productStatus;
    }

    public ProductGroupThumbnail(
            long productGroupId,
            long sellerId,
            String productGroupName,
            BrandDto brand,
            String productImageUrl,
            Price price,
            LocalDateTime insertDate,
            ProductStatus productStatus) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.productGroupName = productGroupName;
        this.brand = brand;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.insertDate = insertDate;
        this.isFavorite = false;
        this.productStatus = productStatus;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @JsonIgnore
    @Override
    public Long getId() {
        return productGroupId;
    }

    @JsonIgnore
    @Override
    public long getSalePrice() {
        return price.getSalePrice();
    }

    @JsonIgnore
    @Override
    public int getDiscountRate() {
        return price.getDiscountRate();
    }

    @Override
    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public void setShareRatio(double shareRatio) {}

    @Override
    public double getAverageRating() {
        return averageRating;
    }
}
