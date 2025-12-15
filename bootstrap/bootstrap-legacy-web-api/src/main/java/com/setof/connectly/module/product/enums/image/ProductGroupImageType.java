package com.setof.connectly.module.product.enums.image;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductGroupImageType implements EnumType {
    MAIN("대표 이미지"),
    DETAIL("상세 이미지"),
    DESCRIPTION("상세 설명 이미지");

    private final String displayName;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return displayName;
    }
}
