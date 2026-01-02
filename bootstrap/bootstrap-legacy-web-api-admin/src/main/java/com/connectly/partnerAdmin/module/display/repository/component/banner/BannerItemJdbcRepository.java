package com.connectly.partnerAdmin.module.display.repository.component.banner;

import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;

import java.util.List;

public interface BannerItemJdbcRepository {
    void saveAll(List<BannerItem> bannerItems);
}
