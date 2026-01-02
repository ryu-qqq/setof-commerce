package com.setof.connectly.module.product.entity.notice.embedded;

import com.setof.connectly.module.product.enums.group.Origin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Embeddable
public class NoticeDetail {
    @Column(name = "material")
    private String material;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "maker")
    private String maker;

    @Column(name = "origin")
    @Enumerated(EnumType.STRING)
    private Origin origin;

    @Column(name = "washing_method")
    private String washingMethod;

    @Column(name = "year_month_day")
    private String yearMonth;

    @Column(name = "assurance_standard")
    private String assuranceStandard;

    @Column(name = "as_phone")
    private String asPhone;
}
