package com.setof.connectly.module.product.enums.option;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptionName implements EnumType {
    SIZE("사이즈", 1),
    COLOR("색상", 1),
    DEFAULT_ONE("옵션1", 1),
    DEFAULT_TWO("옵션2", 2),
    NONE("", 3);

    private final String optionName;
    private final int displayOrder;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return optionName;
    }
}
