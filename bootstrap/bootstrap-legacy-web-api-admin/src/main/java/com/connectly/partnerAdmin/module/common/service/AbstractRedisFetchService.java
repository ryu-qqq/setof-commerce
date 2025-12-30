package com.connectly.partnerAdmin.module.common.service;

import com.connectly.partnerAdmin.module.common.exception.JsonSerializationException;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public abstract class AbstractRedisFetchService extends AbstractRedisService implements RedisFetchService {

    protected final static String errorJsonMessage = "Failed to parse JSON: {}";

    public AbstractRedisFetchService(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getValue(String key) {
        try {
            return opsValue().get(key);
        } catch (RedisConnectionFailureException e) {
            throw new ServiceException(redisConnectionFailMessage, e);
        } catch (DataAccessException e) {
            throw new ServiceException(errorAccessRedisMessage, e);
        }
    }

    @Override
    public List<String> getValues(List<String> keys) {
        try {
            return opsValue().multiGet(keys);
        } catch (RedisConnectionFailureException e) {
            throw new ServiceException(redisConnectionFailMessage, e);
        } catch (DataAccessException e) {
            throw new ServiceException(errorAccessRedisMessage, e);
        }
    }


    protected <T> Optional<T> parseJson(String value, Class<T> clazz) {
        try {
            return Optional.ofNullable(JsonUtils.fromJson(value, clazz));
        } catch (JsonSerializationException e) {
            log.error(errorJsonMessage, e.getMessage());
            return Optional.empty();
        }
    }

}
