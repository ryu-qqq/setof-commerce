package com.connectly.partnerAdmin.module.user.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserGradeEnum implements EnumType {

    GUEST(0.01),
    NORMAL_GRADE(0.01),
    ;

    private final double mileageReserveRate;

    public static UserGradeEnum of(String code) {
        return Arrays.stream(UserGradeEnum.values())
                .filter(r -> r.name().equals(code))
                .findAny()
                .orElse(GUEST);
    }

    public static boolean isRightRole(String inputRole, String findRole){
        return UserGradeEnum.of(findRole).equals(UserGradeEnum.of(inputRole));
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
