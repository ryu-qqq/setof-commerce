package com.setof.connectly.module.product.dto.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.group.Origin;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ProductNoticeDto {
    @JsonIgnore private long productNoticeId;
    private String material;
    private String color;
    private String size;
    private String maker;
    private String origin;
    private String washingMethod;
    private String yearMonth;
    private String assuranceStandard;
    private String asPhone;

    @QueryProjection
    public ProductNoticeDto(
            long productNoticeId,
            String material,
            String color,
            String size,
            String maker,
            Origin origin,
            String washingMethod,
            String yearMonth,
            String assuranceStandard,
            String asPhone) {
        this.productNoticeId = productNoticeId;
        this.material = material;
        this.color = color;
        this.size = size;
        this.maker = maker;
        this.origin = origin.getDisplayName();
        this.washingMethod = washingMethod;
        this.yearMonth = yearMonth;
        this.assuranceStandard = assuranceStandard;
        this.asPhone = asPhone;
    }

    @Override
    public int hashCode() {
        return String.valueOf(productNoticeId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductNoticeDto) {
            ProductNoticeDto p = (ProductNoticeDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
