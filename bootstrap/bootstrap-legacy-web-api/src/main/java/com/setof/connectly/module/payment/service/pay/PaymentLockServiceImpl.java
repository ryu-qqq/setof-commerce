package com.setof.connectly.module.payment.service.pay;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class PaymentLockServiceImpl extends AbstractRedisService implements PaymentLockService {
    public PaymentLockServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public boolean tryLock(List<Long> productIds) {
        ValueOperations<String, String> ops = opsValue();
        Boolean result = ops.setIfAbsent(generateKey(productIds), "locked", 5, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    private String generateKey(List<Long> productIds) {
        String productIdsKey =
                productIds.stream().sorted().map(String::valueOf).collect(Collectors.joining(","));

        String userKEy = generateKey(RedisKey.USERS, String.valueOf(SecurityUtils.currentUserId()));
        String productsKey = generateKey(RedisKey.PAYMENT_LOCK, productIdsKey);

        return userKEy + productsKey;
    }
}
