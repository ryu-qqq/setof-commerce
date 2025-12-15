package com.setof.connectly.module.user.enums;

import com.setof.connectly.module.common.enums.EnumType;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialLoginType implements EnumType {
    kakao,
    naver,
    none;

    public static SocialLoginType of(String name) {
        return Arrays.stream(SocialLoginType.values())
                .filter(r -> r.name().equals(name))
                .findAny()
                .orElse(none);
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }

    public boolean isEmailUser() {
        return this.equals(none);
    }
}
