package com.connectly.partnerAdmin.module.display.service.component.query.banner;


import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisQueryService;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class BannerRedisQueryServiceImpl extends AbstractRedisQueryService implements BannerRedisQueryService{


    public BannerRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void deleteBannerItemsInRedis(BannerType bannerType) {
        String key = generateKey(RedisKey.BANNERS, bannerType.getName());
        delete(key);
    }

}

