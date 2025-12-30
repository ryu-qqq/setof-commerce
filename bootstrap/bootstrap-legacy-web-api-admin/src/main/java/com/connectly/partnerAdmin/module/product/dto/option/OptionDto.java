package com.connectly.partnerAdmin.module.product.dto.option;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long optionGroupId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long optionDetailId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private OptionName optionName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String optionValue;


    @Builder
    @QueryProjection
    public OptionDto(long optionGroupId, long optionDetailId, OptionName optionName, String optionValue) {
        this.optionGroupId = optionGroupId;
        this.optionDetailId = optionDetailId;
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    @JsonIgnore
    public String getFullOptionName(){
        return optionName.getOptionName() + optionValue;
    }


    @Override
    public int hashCode() {
        return Objects.hash(optionGroupId, optionDetailId, optionName, optionValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OptionDto that = (OptionDto) obj;
        return Objects.equals(optionGroupId, that.optionGroupId) &&
                Objects.equals(optionDetailId, that.optionDetailId) &&
                Objects.equals(optionName, that.optionName) &&
                Objects.equals(optionValue, that.optionValue);
    }

}

