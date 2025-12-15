package com.setof.connectly.module.product.dto.option;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.option.OptionName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionDto {

    private long optionGroupId;
    private long optionDetailId;
    private OptionName optionName;
    private String optionValue;

    @QueryProjection
    public OptionDto(
            long optionGroupId, long optionDetailId, OptionName optionName, String optionValue) {
        this.optionGroupId = optionGroupId;
        this.optionDetailId = optionDetailId;
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    @Override
    public int hashCode() {
        return (optionGroupId + String.valueOf(optionDetailId)).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OptionDto) {
            OptionDto p = (OptionDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
