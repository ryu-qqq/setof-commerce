package com.setof.connectly.module.user.service.query;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.entity.Users;

public interface UserQueryService {
    Users saveUser(Users users);

    JoinedDto integrationUser(JoinedDto joinedDto, OAuth2UserInfo oAuth2User);
}
