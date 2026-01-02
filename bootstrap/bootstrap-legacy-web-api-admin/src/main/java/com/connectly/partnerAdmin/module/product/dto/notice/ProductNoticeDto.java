package com.connectly.partnerAdmin.module.product.dto.notice;

import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductNoticeDto {
    private String material;
    private String color;
    private String size;
    private String maker;
    private Origin origin;
    private String washingMethod;
    private String yearMonth;
    private String assuranceStandard;
    private String asPhone;

    @QueryProjection
    public ProductNoticeDto(String material, String color, String size, String maker, Origin origin, String washingMethod, String yearMonth, String assuranceStandard, String asPhone) {
        this.material = material;
        this.color = color;
        this.size = size;
        this.maker = maker;
        this.origin = origin;
        this.washingMethod = washingMethod;
        this.yearMonth = yearMonth;
        this.assuranceStandard = assuranceStandard;
        this.asPhone = asPhone;
    }


}
