package com.connectly.partnerAdmin.auth.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType implements EnumType {

    MASTER("MASTER"),
    SELLER("SELLER"),
    GUEST("GUEST")
    ;

    private final String description;

    public boolean isMaster(){
        return this.equals(RoleType.MASTER);
    }

    public boolean isSeller(){
        return this.equals(RoleType.SELLER);
    }

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.name().equals(code))
                .findAny()
                .orElse(GUEST);
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
