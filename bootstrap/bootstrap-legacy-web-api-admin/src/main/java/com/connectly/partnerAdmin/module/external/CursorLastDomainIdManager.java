package com.connectly.partnerAdmin.module.external;

import com.connectly.partnerAdmin.module.common.service.AbstractRedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CursorLastDomainIdManager extends AbstractRedisService {

    private static final String CURSOR_LAST_DOMAIN_ID = "cursor:lastDomainId:%s";

    public CursorLastDomainIdManager(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public Long getLastDomainId(String sellerId) {
        String key = generateKey(sellerId);
        String value = opsValue().get(key);
        return value != null ? Long.valueOf(value) : null;
    }

    public void saveLastDomainId(String sellerId, Long lastDomainId) {
        String key = generateKey(sellerId);
        opsValue().set(key, String.valueOf(lastDomainId));
    }


    public String generateKey(String value) {
        return String.format(CURSOR_LAST_DOMAIN_ID, value);
    }
}
