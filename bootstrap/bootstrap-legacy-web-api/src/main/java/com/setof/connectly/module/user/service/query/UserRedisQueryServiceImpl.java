package com.setof.connectly.module.user.service.query;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserRedisQueryServiceImpl extends AbstractRedisService
        implements UserRedisQueryService {

    public UserRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveUser(JoinedDto joinedDto) {
        String key = generateKey(RedisKey.JOINED_USERS, joinedDto.getPhoneNumber());
        String value = JsonUtils.toJson(joinedDto);
        save(key, value, RedisKey.JOINED_USERS.getHourDuration());
    }

    @Override
    public void saveUser(long userId, UserDto userDto) {
        String key = generateKey(RedisKey.USERS, String.valueOf(userId));
        String value = JsonUtils.toJson(userDto);
        save(key, value, RedisKey.USERS.getHourDuration());
    }

    @Override
    public void deleteUser(long userId, String PhoneNumber) {
        String key = generateKey(RedisKey.USERS, String.valueOf(userId));
        String phoneNumber = generateKey(RedisKey.JOINED_USERS, PhoneNumber);
        delete(key);
        delete(phoneNumber);
    }
}
