package com.connectly.partnerAdmin.module.product.enums.image;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductGroupImageType implements EnumType {
    MAIN("대표 이미지"),
    DETAIL("상세 이미지"),
    DESCRIPTION("상세 설명 이미지");

    private final String displayName;

    public boolean isMain(){
        return this.equals(MAIN);
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}
