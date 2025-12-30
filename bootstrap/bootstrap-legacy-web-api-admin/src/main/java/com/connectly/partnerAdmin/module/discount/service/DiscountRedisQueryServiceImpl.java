package com.connectly.partnerAdmin.module.discount.service;

import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisQueryService;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.DiscountInfo;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountRedisQueryServiceImpl extends AbstractRedisQueryService implements DiscountRedisQueryService{


    public DiscountRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveDiscountCache(DiscountInfo baseDiscountInfo){
        String key = generateKey(RedisKey.DISCOUNT, baseDiscountInfo.getIssueType().name() + baseDiscountInfo.getTargetId());
        String value = JsonUtils.toJson(baseDiscountInfo);
        Duration ttl = Duration.between(LocalDateTime.now(), baseDiscountInfo.getPolicyEndDate());
        save(key, value, ttl);
    }

    @Override
    public void updateDiscountCache(DiscountPolicy discountPolicy, List<DiscountTarget> savedDiscountTargets) {
        savedDiscountTargets.forEach(discountTarget -> {
            String key = generateKey(RedisKey.DISCOUNT, discountTarget.getIssueType().name() + discountTarget.getTargetId());
            if(discountTarget.getActiveYn().isNo()){
                delete(key);
            }else{
                long discountPolicyId = discountTarget.getDiscountPolicyId();
                BaseDiscountInfo discountInfo = discountTarget.getDiscountInfo(discountPolicy);

                String value = JsonUtils.toJson(discountInfo);
                Duration ttl = Duration.between(LocalDateTime.now(), discountInfo.getPolicyEndDate());
                save(key, value, ttl);
            }
        });
    }

}
