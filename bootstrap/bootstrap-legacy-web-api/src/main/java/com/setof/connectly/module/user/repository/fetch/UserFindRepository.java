package com.setof.connectly.module.user.repository.fetch;

import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.entity.Users;
import java.util.Optional;

public interface UserFindRepository {

    Optional<Users> fetchUserEntity(String phoneNumber);

    Optional<Users> fetchUserEntity(long userId);

    Optional<JoinedDto> isJoinedMember(String phoneNumber);

    Optional<JoinedDto> isJoinedMemberByEmail(String email);

    Optional<UserDto> fetchUser(long userId);
}
