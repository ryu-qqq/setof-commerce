package com.setof.connectly.module.user.mapper;

import com.setof.connectly.auth.dto.OAuth2UserInfo;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.join.CreateUser;
import com.setof.connectly.module.user.dto.mypage.MyPageResponse;
import com.setof.connectly.module.user.entity.Users;
import java.util.Map;

public interface UserMapper {
    Users toEntity(OAuth2UserInfo oAuth2UserInfo);

    Users toEntity(CreateUser createUser);

    MyPageResponse toMyPageResponse(
            MyPageResponse myPageResponse, Map<OrderStatus, Long> orderStatusLongMap);

    UserDto toDto(Users users);
}
