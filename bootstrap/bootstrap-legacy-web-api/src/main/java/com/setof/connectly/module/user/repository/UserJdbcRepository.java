package com.setof.connectly.module.user.repository;

import com.setof.connectly.auth.dto.OAuth2UserInfo;

public interface UserJdbcRepository {

    void updateUser(long userId, OAuth2UserInfo oAuth2User);
}
