package com.setof.connectly.module.display.service.component.fetch.banner;

import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import com.setof.connectly.module.display.enums.BannerType;
import java.util.List;

public interface BannerRedisQueryService {

    void saveBannerItemInRedis(BannerType bannerType, List<BannerItemDto> processBannerItems);
}
