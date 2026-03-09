package com.ryuqq.setof.adapter.in.rest.common.oauth2.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * OAuth2UserInfo - OAuth2 인증 사용자 정보.
 *
 * <p>소셜 로그인 제공자의 응답을 내부 표현으로 변환한 객체입니다. OAuth2User를 구현하여 Spring Security와 통합됩니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public class OAuth2UserInfo implements OAuth2User {

    private final String id;
    private final String phoneNumber;
    private final String email;
    private final String name;
    private final String gender;
    private final String dateOfBirth;
    private final String socialLoginType;

    public OAuth2UserInfo(
            String id,
            String phoneNumber,
            String email,
            String name,
            String gender,
            String dateOfBirth,
            String socialLoginType) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.socialLoginType = socialLoginType;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("NORMAL_GRADE"));
    }

    @Override
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSocialLoginType() {
        return socialLoginType;
    }
}
