package com.connectly.partnerAdmin.module.display.mapper.banner;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerResponse;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBanner;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBannerItem;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BannerMapper {

    BannerResponse toResponse(Banner banner);
    Banner toEntity(CreateBanner createBanner);
    List<BannerItem> toBannerItems(List<CreateBannerItem> createBannerItems);
    CustomPageable<BannerGroupDto> toBannerGroupDtos(List<BannerGroupDto> bannerGroupDtos, Pageable pageable, long total);
}
