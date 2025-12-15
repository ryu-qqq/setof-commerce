package com.setof.connectly.module.user.dto.join;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginUser {

    @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
    private String phoneNumber;

    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
            message = "로그인 아이디와 비밀번호를 확인해주세요.")
    private String passwordHash;

    private String referer;

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
