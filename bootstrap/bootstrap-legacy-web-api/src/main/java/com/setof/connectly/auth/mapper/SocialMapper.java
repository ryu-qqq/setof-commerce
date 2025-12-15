package com.setof.connectly.auth.mapper;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.user.enums.SocialLoginType;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface SocialMapper {
    SocialLoginType getSocialLoginType();

    OAuth2UserInfo transFrom(OAuth2User oAuth2User);
}
