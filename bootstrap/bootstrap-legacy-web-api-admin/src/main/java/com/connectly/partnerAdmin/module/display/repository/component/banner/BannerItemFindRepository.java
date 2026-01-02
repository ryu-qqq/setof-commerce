package com.connectly.partnerAdmin.module.display.repository.component.banner;

import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemFilter;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;

import java.util.List;

public interface BannerItemFindRepository {


    List<BannerItemDto> fetchBannerItems(long bannerId, BannerItemFilter bannerItemFilterDto);
    List<BannerItem> fetchBannerItemEntities(List<Long> bannerItemIds);
    List<BannerItem> fetchBannerItemEntitiesByBannerId(long bannerId);

}
