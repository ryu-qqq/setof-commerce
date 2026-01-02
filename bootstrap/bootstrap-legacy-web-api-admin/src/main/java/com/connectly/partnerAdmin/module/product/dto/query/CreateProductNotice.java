package com.connectly.partnerAdmin.module.product.dto.query;

import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.product.enums.group.Origin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateProductNotice {

    @Length(max = 200, message = "Material cannot exceed 200 characters.")
    private String material;

    @Length(max = 100, message = "Color cannot exceed 100 characters.")
    private String color;

    @Length(max = 500, message = "Size cannot exceed 500 characters.")
    private String size;

    @Length(max = 50, message = "Maker cannot exceed 50 characters.")
    private String maker;

    @Length(max = 50, message = "Origin cannot exceed 50 characters.")
    private String origin;

    @Length(max = 100, message = "Washing method cannot exceed 100 characters.")
    private String washingMethod;

    @Length(max = 50, message = "Year and month cannot exceed 50 characters.")
    private String yearMonth;

    @Length(max = 50, message = "Assurance standard cannot exceed 50 characters.")
    private String assuranceStandard;

    @Length(max = 50, message = "AS phone number cannot exceed 100 characters.")
    private String asPhone;


    private static final String DEFAULT_VALUE = "상세 설명 참조";

    public @Length(max = 200, message = "Material cannot exceed 200 characters.") String getMaterial() {
        if (!StringUtils.hasText(material)) return DEFAULT_VALUE;
        return material;
    }

    public @Length(max = 100, message = "Color cannot exceed 100 characters.") String getColor() {
        if (!StringUtils.hasText(color)) return DEFAULT_VALUE;
        return color;
    }

    public @Length(max = 500, message = "Size cannot exceed 500 characters.") String getSize() {
        if (!StringUtils.hasText(size)) return DEFAULT_VALUE;
        return size;
    }

    public @Length(max = 50, message = "Maker cannot exceed 50 characters.") String getMaker() {
        if (!StringUtils.hasText(maker)) return DEFAULT_VALUE;
        return maker;
    }

    public @Length(max = 50, message = "Origin cannot exceed 50 characters.") String getOrigin() {
        if (!StringUtils.hasText(origin)) return Origin.OTHER.name();
        return origin;
    }

    public @Length(max = 100, message = "Washing method cannot exceed 100 characters.") String getWashingMethod() {
        if (!StringUtils.hasText(washingMethod)) return DEFAULT_VALUE;
        return washingMethod;
    }

    public @Length(max = 50, message = "Year and month cannot exceed 50 characters.") String getYearMonth() {
        if (!StringUtils.hasText(yearMonth)) return DEFAULT_VALUE;
        return yearMonth;
    }

    public @Length(max = 50, message = "Assurance standard cannot exceed 50 characters.") String getAssuranceStandard() {
        if (!StringUtils.hasText(assuranceStandard)) return DEFAULT_VALUE;
        return assuranceStandard;
    }

    public @Length(max = 50, message = "AS phone number cannot exceed 100 characters.") String getAsPhone() {
        if (!StringUtils.hasText(asPhone)) return DEFAULT_VALUE;
        return asPhone;
    }
}
