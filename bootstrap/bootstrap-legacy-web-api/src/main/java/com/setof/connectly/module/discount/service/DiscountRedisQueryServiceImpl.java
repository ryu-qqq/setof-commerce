package com.setof.connectly.module.discount.service;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.enums.IssueType;
import com.setof.connectly.module.exception.discount.DiscountUnValidException;
import com.setof.connectly.module.utils.JsonUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DiscountRedisQueryServiceImpl extends AbstractRedisService
        implements DiscountRedisQueryService {

    public DiscountRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveByIssueTypeAndTargetId(long targetId, DiscountCacheDto discountCacheDto) {
        String key = generateDiscountKey(discountCacheDto.getIssueType(), targetId);

        if (isLatestPolicy(key, discountCacheDto)) {
            String value = JsonUtils.toJson(discountCacheDto);
            Duration ttl =
                    Duration.between(LocalDateTime.now(), discountCacheDto.getPolicyEndDate());
            if (!ttl.isNegative() && !ttl.isZero()) {
                save(key, value, ttl);
            } else {
                throw new DiscountUnValidException();
            }
        }
    }

    private boolean isLatestPolicy(String key, DiscountCacheDto discountCacheDto) {
        String value = getValue(key);
        if (StringUtils.hasText(value)) {
            DiscountCacheDto existingDto = parseDiscount(value);
            return discountCacheDto.getPolicyStartDate().isAfter(existingDto.getPolicyStartDate());
        }
        return true;
    }

    private DiscountCacheDto parseDiscount(String value) {
        return JsonUtils.fromJson(value, DiscountCacheDto.class);
    }

    private String generateDiscountKey(IssueType issueType, long targetId) {
        return generateKey(RedisKey.DISCOUNT, issueType.name() + targetId);
    }
}
