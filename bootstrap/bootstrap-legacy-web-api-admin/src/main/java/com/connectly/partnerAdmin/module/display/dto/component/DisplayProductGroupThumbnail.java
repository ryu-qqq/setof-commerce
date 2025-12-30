package com.connectly.partnerAdmin.module.display.dto.component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.discount.core.PriceHolder;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.querydsl.core.annotations.QueryProjection;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisplayProductGroupThumbnail implements PriceHolder {

    private long productGroupId;
    private long sellerId;
    private String productGroupName;
    private BaseBrandContext brand;
    @Setter
    private String productImageUrl;
    @Setter
    private Price price;
    private int displayOrder;

    @QueryProjection
    public DisplayProductGroupThumbnail(long productGroupId, long sellerId, String productGroupName, BaseBrandContext brand, String productImageUrl, Price price) {
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.productGroupName = productGroupName;
        this.brand = brand;
        this.productImageUrl = productImageUrl;
        this.price = price;
    }

    public String getProductGroupName() {
        return StringUtils.hasText(productGroupName) ? productGroupName : "";
    }

    public String getProductImageUrl() {
        return StringUtils.hasText(productImageUrl) ? productImageUrl : "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayProductGroupThumbnail that = (DisplayProductGroupThumbnail) o;
        return productGroupId == that.productGroupId &&
                displayOrder == that.displayOrder &&
                Objects.equals(productImageUrl, that.productImageUrl) &&
                Objects.equals(productGroupName, that.productGroupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productGroupId, productGroupName, displayOrder, productImageUrl);
    }



}
