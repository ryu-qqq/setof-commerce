package com.setof.connectly.module.user.service.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserFindRedisServiceImpl extends AbstractRedisService implements UserFindRedisService {

    public UserFindRedisServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Optional<JoinedDto> isJoinedUser(String phoneNumber) {
        String key = generateKey(RedisKey.JOINED_USERS, phoneNumber);
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parseJoinedUser(value);
        else return Optional.empty();
    }

    @Override
    public Optional<UserDto> fetchUser(long userId) {
        String key = generateKey(RedisKey.USERS, String.valueOf(userId));
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parsedUser(value);
        return Optional.empty();
    }

    private Optional<UserDto> parsedUser(String value) {
        UserDto userDto = JsonUtils.fromJson(value, UserDto.class);
        return Optional.of(userDto);
    }

    private Optional<JoinedDto> parseJoinedUser(String value) {
        JoinedDto joinedDto1 = JsonUtils.fromJson(value, JoinedDto.class);
        return Optional.of(joinedDto1);
    }
}
