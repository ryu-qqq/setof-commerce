package com.setof.connectly.module.display.service.component.fetch.banner.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.display.dto.banner.BannerFilter;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BannerRedisFindServiceImpl extends AbstractRedisService
        implements BannerRedisFindService {

    public BannerRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public List<BannerItemDto> fetchBannerGroup(BannerFilter bannerFilter) {
        String key = generateKey(RedisKey.BANNERS, bannerFilter.getBannerType().getName());
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parseBannerItems(value);
        return new ArrayList<>();
    }

    private List<BannerItemDto> parseBannerItems(String data) {
        return JsonUtils.fromJsonList(data, BannerItemDto.class);
    }
}
