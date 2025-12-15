package com.setof.connectly.module.user.service.query;

import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;

public interface UserRedisQueryService {

    void saveUser(JoinedDto joinedDto);

    void saveUser(long userId, UserDto userDto);

    void deleteUser(long userId, String phoneNumber);
}
