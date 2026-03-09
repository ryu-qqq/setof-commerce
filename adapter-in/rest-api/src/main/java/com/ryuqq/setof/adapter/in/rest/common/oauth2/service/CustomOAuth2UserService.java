package com.ryuqq.setof.adapter.in.rest.common.oauth2.service;

import com.ryuqq.setof.adapter.in.rest.common.oauth2.mapper.KakaoUserInfoMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * CustomOAuth2UserService - OAuth2 사용자 정보 조회 서비스.
 *
 * <p>DefaultOAuth2UserService를 확장하여 소셜 로그인 제공자의 사용자 정보를 내부 OAuth2UserInfo로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final KakaoUserInfoMapper kakaoUserInfoMapper;

    public CustomOAuth2UserService(KakaoUserInfoMapper kakaoUserInfoMapper) {
        this.kakaoUserInfoMapper = kakaoUserInfoMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("kakao".equalsIgnoreCase(registrationId)) {
            return kakaoUserInfoMapper.map(oAuth2User);
        }

        throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
    }
}
