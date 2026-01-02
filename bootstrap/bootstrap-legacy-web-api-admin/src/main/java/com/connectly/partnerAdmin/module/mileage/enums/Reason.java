package com.connectly.partnerAdmin.module.mileage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {

    USE("사용"),
    SAVE("적립");

    private final String displayName;

    public boolean isUse(){
        return this.equals(USE);
    }

}
