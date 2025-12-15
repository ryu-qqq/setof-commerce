package com.setof.connectly.module.user.dto.join;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.annotation.ConsentValid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUser extends LoginUser {

    @NotNull(message = "이름은 빈칸일 수 없습니다.")
    @Size(min = 2, max = 5, message = "이름은 2~5자 사이여야 합니다.")
    private String name;

    @NotNull(message = "서비스 이용약관 값은 필수 입니다.")
    @ConsentValid
    private Yn privacyConsent;

    @NotNull(message = "개인 정보 수집 동의 값은 필수 입니다.")
    @ConsentValid
    private Yn serviceTermsConsent;

    @NotNull(message = "광고 수신 동의 필드 값은 필수 입니다.")
    private Yn adConsent;

    public CreateUser(
            @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.") String phoneNumber,
            @Pattern(
                            regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
                            message = "로그인 아이디와 비밀번호를 확인해주세요.")
                    String passwordHash,
            String referer,
            String name,
            Yn privacyConsent,
            Yn serviceTermsConsent,
            Yn adConsent) {
        super(phoneNumber, passwordHash, referer);
        this.name = name;
        this.privacyConsent = privacyConsent;
        this.serviceTermsConsent = serviceTermsConsent;
        this.adConsent = adConsent;
    }

    public void setPasswordHash(String password) {
        super.setPasswordHash(password);
    }
}
