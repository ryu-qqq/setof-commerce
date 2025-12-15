package com.setof.connectly.module.user.service.fetch;

import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;
import java.util.Optional;

public interface UserFindRedisService {
    Optional<JoinedDto> isJoinedUser(String phoneNumber);

    Optional<UserDto> fetchUser(long userId);
}
