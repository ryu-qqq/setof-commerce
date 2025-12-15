package com.setof.connectly.auth.mapper;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.user.enums.Gender;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class KakaoMapper implements SocialMapper {

    @Override
    public OAuth2UserInfo transFrom(OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2UserInfo.builder()
                .id(attributes.get("id").toString())
                .socialLoginType(SocialLoginType.kakao)
                .name(getName(response))
                .phoneNumber(getPhoneNumber(response))
                .gender(getGender(response))
                .email(response.getOrDefault("email", "").toString())
                .dateOfBirth(getDateOfBirth(response))
                .userGrade(UserGradeEnum.NORMAL_GRADE)
                .build();
    }

    private Gender getGender(Map<String, Object> response) {
        String naverGender = response.getOrDefault("gender", "male").toString();
        return naverGender.equals("female") ? Gender.W : Gender.M;
    }

    private String getName(Map<String, Object> response) {
        UUID uuid = UUID.randomUUID();
        String shortUuid = uuid.toString().substring(0, 8);
        Object orDefault = response.getOrDefault("profile_nickname", "USER" + shortUuid);
        return response.getOrDefault("name", orDefault).toString();
    }

    private String getPhoneNumber(Map<String, Object> response) {
        String phoneNumber = response.getOrDefault("phone_number", "").toString();
        if (StringUtils.hasText(phoneNumber)) {
            String formattedNumber = phoneNumber.replace("+82", "").replace("-", "").trim();
            formattedNumber = "0" + formattedNumber;
            return formattedNumber;
        }

        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 11);
    }

    private LocalDate getDateOfBirth(Map<String, Object> response) {
        String birthyear = response.getOrDefault("birthyear", "2024").toString();
        String birthday = response.getOrDefault("birthday", "0101").toString();
        String fullDate = birthyear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(fullDate, formatter);
    }

    @Override
    public SocialLoginType getSocialLoginType() {
        return SocialLoginType.kakao;
    }
}
