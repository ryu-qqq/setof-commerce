package com.setof.connectly.module.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.mapper.LastDomainIdProvider;
import com.setof.connectly.module.discount.DiscountOffer;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CartResponse implements LastDomainIdProvider, DiscountOffer {

    private long brandId;
    private String brandName;
    private String productGroupName;
    private long sellerId;
    private String sellerName;
    private long productId;
    private Price price;
    private int quantity;
    private int stockQuantity;
    private String optionValue;
    private long cartId;
    private String imageUrl;
    private long productGroupId;
    private ProductStatus productStatus;

    private double mileageRate;
    private double expectedMileageAmount;
    @Builder.Default private Set<ProductCategoryDto> categories = new HashSet<>();

    @JsonIgnore private String path;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Set<OptionDto> options = new HashSet<>();

    @QueryProjection
    public CartResponse(
            long brandId,
            String brandName,
            String productGroupName,
            long sellerId,
            String sellerName,
            long productId,
            Price price,
            int quantity,
            int stockQuantity,
            Set<OptionDto> options,
            long cartId,
            String imageUrl,
            long productGroupId,
            ProductStatus productStatus,
            String path) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.productGroupName = productGroupName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.stockQuantity = stockQuantity;
        this.options = options;
        this.cartId = cartId;
        this.imageUrl = imageUrl;
        this.productGroupId = productGroupId;
        this.productStatus = productStatus;
        this.path = path;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public void setShareRatio(double shareRatio) {}

    public void setCategories(Set<ProductCategoryDto> categories) {
        this.categories = categories;
    }

    public void setExpectedMileageAmount(double expectedMileageAmount) {
        this.expectedMileageAmount = expectedMileageAmount;
    }

    public void setMileageRate(double mileageRate) {
        this.mileageRate = mileageRate;
    }

    @Override
    public Long getId() {
        return cartId;
    }
}
