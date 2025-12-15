package com.setof.connectly.module.user.dto.join;

import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.entity.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinedUser {
    private boolean isJoined;
    private JoinedDto joinedUser;

    public JoinedUser(boolean isJoined, Users user) {
        this.isJoined = isJoined;
        this.joinedUser = new JoinedDto(user);
    }

    public JoinedUser(boolean isJoined, JoinedDto joinedUser) {
        this.isJoined = isJoined;
        this.joinedUser = joinedUser;
    }

    public JoinedUser(boolean isJoined) {
        this.isJoined = isJoined;
        this.joinedUser = null;
    }
}
