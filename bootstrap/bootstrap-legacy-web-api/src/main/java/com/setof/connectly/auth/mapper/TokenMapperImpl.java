package com.setof.connectly.auth.mapper;

import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.redis.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class TokenMapperImpl implements TokenMapper {

    @Override
    public RefreshToken toRefreshToken(
            String username, String refreshToken, String userGrade, Long remainingMilliSeconds) {
        return RefreshToken.builder()
                .id(username)
                .refreshToken(refreshToken)
                .userGrade(userGrade)
                .expiration(remainingMilliSeconds / 1000)
                .build();
    }

    @Override
    public TokenDto toToken(String accessToken, String refreshToken) {
        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
