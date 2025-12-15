package com.setof.connectly.auth.mapper;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.user.enums.Gender;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class NaverMapper implements SocialMapper {

    @Override
    public OAuth2UserInfo transFrom(OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .id(response.get("id").toString())
                .socialLoginType(SocialLoginType.naver)
                .name(response.get("name").toString())
                .phoneNumber(response.get("mobile").toString())
                .gender(getGender(response.get("gender").toString()))
                .email(response.get("email").toString())
                .dateOfBirth(getDateOfBirth(response))
                .userGrade(UserGradeEnum.NORMAL_GRADE)
                .build();
    }

    private Gender getGender(String naverGender) {
        String gender = naverGender.equals("F") ? "W" : "M";
        return Gender.valueOf(gender);
    }

    private LocalDate getDateOfBirth(Map<String, Object> response) {
        String birthyear = response.get("birthyear").toString();
        String birthday = response.get("birthday").toString();

        String fullDate = birthyear + "-" + birthday;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(fullDate, formatter);
    }

    @Override
    public SocialLoginType getSocialLoginType() {
        return SocialLoginType.naver;
    }
}
