package com.ryuqq.setof.adapter.in.rest.common.oauth2.mapper;

import com.ryuqq.setof.adapter.in.rest.common.oauth2.dto.OAuth2UserInfo;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * KakaoUserInfoMapper - 카카오 OAuth2 응답 → OAuth2UserInfo 변환.
 *
 * <p>카카오 kakao_account 응답 구조를 파싱하여 내부 표현으로 변환합니다. 전화번호 정규화 (+82 → 010), 기본값 생성 등을 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class KakaoUserInfoMapper {

    private static final String KAKAO = "KAKAO";

    @SuppressWarnings("unchecked")
    public OAuth2UserInfo map(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        return new OAuth2UserInfo(
                attributes.get("id").toString(),
                extractPhoneNumber(account),
                extractOrDefault(account, "email", ""),
                extractName(account),
                extractGender(account),
                extractDateOfBirth(account),
                KAKAO);
    }

    private String extractPhoneNumber(Map<String, Object> account) {
        String phoneNumber = extractOrDefault(account, "phone_number", "");
        if (StringUtils.hasText(phoneNumber)) {
            String formatted = phoneNumber.replace("+82", "").replace("-", "").trim();
            return "0" + formatted;
        }
        return UUID.randomUUID().toString().substring(0, 11);
    }

    private String extractName(Map<String, Object> account) {
        String name = extractOrDefault(account, "name", "");
        if (StringUtils.hasText(name)) {
            return name;
        }
        String nickname = extractOrDefault(account, "profile_nickname", "");
        if (StringUtils.hasText(nickname)) {
            return nickname;
        }
        return "USER" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String extractGender(Map<String, Object> account) {
        String gender = extractOrDefault(account, "gender", "male");
        return "female".equals(gender) ? "W" : "M";
    }

    private String extractDateOfBirth(Map<String, Object> account) {
        String birthyear = extractOrDefault(account, "birthyear", "2024");
        String birthday = extractOrDefault(account, "birthday", "0101");
        return birthyear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2);
    }

    private String extractOrDefault(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.getOrDefault(key, defaultValue);
        return value != null ? value.toString() : defaultValue;
    }
}
