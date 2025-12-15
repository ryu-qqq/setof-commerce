package com.setof.connectly.module.product.enums.group;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ManagementType implements EnumType {
    MENUAL("수동등록"),
    AUTO("크롤링"),
    SABANG("사방넷"),
    SEWON("세원 셀릭");

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
