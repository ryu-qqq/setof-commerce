package com.setof.connectly.module.product.enums.option;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionType implements EnumType {
    OPTION_ONE,
    OPTION_TWO,
    SINGLE;

    public boolean isNoOption() {
        return this.equals(SINGLE);
    }

    public boolean isOneDepth() {
        return this.equals(OPTION_ONE);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}
