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
    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "MAKER")
    private String maker;

    @Column(name = "ORIGIN")
    @Enumerated(EnumType.STRING)
    private Origin origin;

    @Column(name = "WASHING_METHOD")
    private String washingMethod;

    @Column(name = "YEAR_MONTH_DAY")
    private String yearMonth;

    @Column(name = "ASSURANCE_STANDARD")
    private String assuranceStandard;

    @Column(name = "AS_PHONE")
    private String asPhone;
}
