package com.connectly.partnerAdmin.module.display.service.component.query.banner;

import com.connectly.partnerAdmin.module.display.dto.banner.BannerResponse;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBanner;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBannerItem;
import com.connectly.partnerAdmin.module.display.dto.banner.UpdateBannerDisplayYn;


import java.util.List;

public interface BannerQueryService {
    BannerResponse enrollBanner(CreateBanner createBanner);

    BannerResponse updateBanner(long bannerId, CreateBanner createBanner);

    List<CreateBannerItem> enrollBannerItems(List<CreateBannerItem> createBannerItems);

    BannerResponse updateDisplayYn(long bannerId, UpdateBannerDisplayYn updateBannerDisplayYn);

}
