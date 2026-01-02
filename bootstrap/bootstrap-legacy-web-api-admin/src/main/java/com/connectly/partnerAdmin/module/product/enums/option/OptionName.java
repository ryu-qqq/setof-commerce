package com.connectly.partnerAdmin.module.product.enums.option;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OptionName implements EnumType {

    SIZE("사이즈", 1),
    COLOR("색상", 1),
    DEFAULT_ONE("옵션1", 1),
    DEFAULT_TWO("옵션2", 2)
    ;

    private final String optionName;
    private final int displayOrder;

    public static OptionName of(String optionName){
        return Arrays.stream(OptionName.values())
                .filter(r -> r.name().equals(optionName))
                .findAny()
                .orElseGet(()->DEFAULT_ONE);
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
