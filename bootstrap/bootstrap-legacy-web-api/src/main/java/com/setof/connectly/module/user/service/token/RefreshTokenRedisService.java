package com.setof.connectly.module.user.service.token;

import com.setof.connectly.module.user.redis.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRedisService {

    void saveRefreshToken(RefreshToken refreshToken);

    Optional<RefreshToken> findByUserId(String userId);
}
