package com.connectly.partnerAdmin.module.product.enums.option;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionType implements EnumType {

    OPTION_ONE,
    OPTION_TWO,
    SINGLE;

    public boolean isSingle(){
        return this.equals(SINGLE);
    }

    public boolean isOneDepth(){
        return this.equals(OPTION_ONE);
    }

    public boolean isTwoDepth(){
        return this.equals(OPTION_TWO);
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
