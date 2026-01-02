package com.connectly.partnerAdmin.module.display.service.component.fetch.banner;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemDto;
import com.connectly.partnerAdmin.module.display.dto.banner.filter.BannerFilter;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemFilter;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BannerFetchService {

    Banner fetchBannerEntity(long bannerId);
    List<BannerItemDto> fetchBannerGroup(long bannerId, BannerItemFilter bannerItemFilterDto);
    List<BannerItem> fetchBannerItemEntities(List<Long> bannerItemIds);

    List<BannerItem> fetchBannerItemEntitiesByBannerId(long bannerId);
    CustomPageable<BannerGroupDto> fetchBannerGroups(BannerFilter bannerFilterDto, Pageable pageable);

}
