package com.setof.connectly.module.user.redis;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {

    @Id private String id;
    private String refreshToken;
    private String userGrade;
    @TimeToLive private Long expiration;
}
