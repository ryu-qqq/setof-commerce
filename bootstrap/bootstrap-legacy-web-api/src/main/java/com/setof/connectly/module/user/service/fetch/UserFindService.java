package com.setof.connectly.module.user.service.fetch;

import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.join.IsJoinedUser;
import com.setof.connectly.module.user.dto.join.JoinedUser;
import com.setof.connectly.module.user.entity.Users;
import java.util.Optional;

public interface UserFindService {

    Users fetchUserEntity(String phoneNumber);

    Users fetchUserEntity(long userId);

    JoinedUser fetchJoinedUser(IsJoinedUser isJoinedUser);

    Optional<JoinedDto> isJoinedUser(String phoneNumber);

    Optional<JoinedDto> isJoinedUserByEmail(String email);

    UserDto fetchUser(long userId);

    JoinedUser fetchJoinedUser();
}
