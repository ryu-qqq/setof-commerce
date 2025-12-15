package com.setof.connectly.auth.mapper;

import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.redis.RefreshToken;

public interface TokenMapper {

    RefreshToken toRefreshToken(
            String username, String refreshToken, String userGrade, Long remainingMilliSeconds);

    TokenDto toToken(String accessToken, String refreshToken);
}
