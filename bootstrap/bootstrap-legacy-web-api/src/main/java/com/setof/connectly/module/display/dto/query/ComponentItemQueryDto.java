package com.setof.connectly.module.display.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.mapper.CursorValueProvider;
import com.setof.connectly.module.display.enums.SortType;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.enums.component.OrderType;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComponentItemQueryDto implements CursorValueProvider {

    private long componentId;
    private long componentTargetId;
    private long componentItemId;
    private long productGroupId;
    private long sellerId;
    private BrandDto brand;
    private String productDisplayName;
    private String productImageUrl;
    private Price price;
    private Long displayOrder;
    private ComponentType componentType;
    private long tabId;
    private SortType sortType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    private double averageRating;
    private long reviewCount;
    private double score;
    private OrderType orderType;

    @QueryProjection
    public ComponentItemQueryDto(
            long componentId,
            long componentTargetId,
            long componentItemId,
            long productGroupId,
            long sellerId,
            BrandDto brand,
            String productDisplayName,
            String productImageUrl,
            Price price,
            Long displayOrder,
            ComponentType componentType,
            long tabId,
            SortType sortType,
            LocalDateTime insertDate,
            double averageRating,
            long reviewCount,
            double score,
            OrderType orderType) {
        this.componentId = componentId;
        this.componentTargetId = componentTargetId;
        this.componentItemId = componentItemId;
        this.productGroupId = productGroupId;
        this.sellerId = sellerId;
        this.brand = brand;
        this.productDisplayName = productDisplayName;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.displayOrder = displayOrder;
        this.componentType = componentType;
        this.tabId = tabId;
        this.sortType = sortType;
        this.insertDate = insertDate;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.score = score;
        this.orderType = orderType;
    }

    public long getSalePrice() {
        return this.price.getSalePrice();
    }

    public int getDiscountRate() {
        return this.price.getDiscountRate();
    }

    @Override
    public int hashCode() {
        return (String.valueOf(this.componentTargetId) + String.valueOf(this.componentItemId))
                .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComponentItemQueryDto) {
            ComponentItemQueryDto p = (ComponentItemQueryDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }

    @Override
    public Long getId() {
        return displayOrder;
    }
}
