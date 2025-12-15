package com.setof.connectly.module.display.service.component.fetch.banner;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import com.setof.connectly.module.display.enums.BannerType;
import com.setof.connectly.module.utils.JsonUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BannerRedisQueryServiceImpl extends AbstractRedisService
        implements BannerRedisQueryService {

    public BannerRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveBannerItemInRedis(
            BannerType bannerType, List<BannerItemDto> processBannerItems) {
        String key = generateKey(RedisKey.BANNERS, bannerType.getName());
        String value = JsonUtils.toJson(processBannerItems);
        LocalDateTime now = LocalDateTime.now();

        Optional<BannerItemDto> earliestEndDateBannerItem =
                getEarliestEndDateBannerItem(processBannerItems);
        if (earliestEndDateBannerItem.isPresent()) {
            BannerItemDto bannerItemDto = earliestEndDateBannerItem.get();
            Duration ttl =
                    Duration.between(now, bannerItemDto.getDisplayPeriod().getDisplayEndDate());
            save(key, value, ttl);
        }
    }

    private Optional<BannerItemDto> getEarliestEndDateBannerItem(
            List<BannerItemDto> processBannerItems) {
        return processBannerItems.stream()
                .min(
                        Comparator.comparing(
                                gnbResponse -> gnbResponse.getDisplayPeriod().getDisplayEndDate()));
    }
}
