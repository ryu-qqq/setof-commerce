package com.connectly.partnerAdmin.module.display.repository.component.banner;

import com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.filter.BannerFilter;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BannerFindRepository {

    List<BannerGroupDto> fetchBannerGroups(BannerFilter filterDto, Pageable pageable);

    JPAQuery<Long> fetchBannerGroupCountQuery(BannerFilter filterDto);


    Optional<Banner> fetchBannerEntity(long bannerId);

}
