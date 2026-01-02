package com.connectly.partnerAdmin.module.product.entity.notice.embedded;


import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class NoticeDetail  {

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

    @Builder
    public NoticeDetail(String material, String color, String size, String maker, Origin origin, String washingMethod, String yearMonth, String assuranceStandard, String asPhone) {
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
