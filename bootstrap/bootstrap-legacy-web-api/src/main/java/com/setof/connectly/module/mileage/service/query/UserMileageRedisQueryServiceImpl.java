package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.service.fetch.UserFindRedisService;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserMileageRedisQueryServiceImpl extends AbstractRedisService
        implements UserMileageRedisQueryService {

    private final UserFindRedisService userFindRedisService;

    public UserMileageRedisQueryServiceImpl(
            StringRedisTemplate redisTemplate, UserFindRedisService userFindRedisService) {
        super(redisTemplate);
        this.userFindRedisService = userFindRedisService;
    }

    @Override
    public void updateUserMileageInCache(long userId, double mileageAmount) {
        Optional<UserDto> userDto = userFindRedisService.fetchUser(userId);
        if (userDto.isPresent()) {
            userDto.get().setCurrentMileage(mileageAmount);
            String key = generateKey(RedisKey.USERS, String.valueOf(userId));
            String value = JsonUtils.toJson(userDto.get());
            save(key, value);
        }
    }
}
