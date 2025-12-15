package com.setof.connectly.auth.service;

import com.setof.connectly.auth.mapper.SocialMapper;
import com.setof.connectly.auth.mapper.SocialMapperProvider;
import com.setof.connectly.module.user.enums.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    private final SocialMapperProvider socialMapperProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialLoginType socialLoginType = SocialLoginType.of(registrationId);
        SocialMapper service = socialMapperProvider.getService(socialLoginType);
        return service.transFrom(oAuth2User);
    }
}
