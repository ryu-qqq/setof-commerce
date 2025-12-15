package com.setof.connectly.module.display.repository.component.banner;

import com.setof.connectly.module.display.dto.banner.BannerFilter;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import java.util.List;

public interface BannerFindRepository {
    List<BannerItemDto> fetchBannerItems(BannerFilter filterDto);
}
