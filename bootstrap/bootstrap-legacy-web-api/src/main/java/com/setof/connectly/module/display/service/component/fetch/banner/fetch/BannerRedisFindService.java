package com.setof.connectly.module.display.service.component.fetch.banner.fetch;

import com.setof.connectly.module.display.dto.banner.BannerFilter;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import java.util.List;

public interface BannerRedisFindService {
    List<BannerItemDto> fetchBannerGroup(BannerFilter bannerFilterDto);
}
