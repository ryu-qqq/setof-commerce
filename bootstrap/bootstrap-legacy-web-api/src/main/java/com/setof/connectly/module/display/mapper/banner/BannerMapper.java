package com.setof.connectly.module.display.mapper.banner;

import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import java.util.List;

public interface BannerMapper {

    List<BannerItemDto> toBannerItems(List<BannerItemDto> bannerItems);
}
