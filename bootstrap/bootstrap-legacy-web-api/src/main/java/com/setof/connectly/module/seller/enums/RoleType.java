package com.setof.connectly.module.seller.enums;

import com.setof.connectly.module.common.enums.EnumType;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType implements EnumType {
    MASTER,
    SELLER,
    GUEST;

    public static boolean isRightRole(String inputRole, String findRole) {
        return RoleType.of(findRole).equals(RoleType.of(inputRole));
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
        return this.name();
    }
}
