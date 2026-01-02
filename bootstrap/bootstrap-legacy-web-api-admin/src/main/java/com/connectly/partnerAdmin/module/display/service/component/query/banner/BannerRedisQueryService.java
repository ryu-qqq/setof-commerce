package com.connectly.partnerAdmin.module.display.service.component.query.banner;

import com.connectly.partnerAdmin.module.display.enums.BannerType;

public interface BannerRedisQueryService {

    void deleteBannerItemsInRedis(BannerType bannerType);

}
