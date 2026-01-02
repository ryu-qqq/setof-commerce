package com.connectly.partnerAdmin.module.product.enums.group;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCondition implements EnumType {

    NEW("새상품"),
    USED("중고 상품");

    private final String displayName;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}
