package com.connectly.partnerAdmin.module.display.service.component.fetch.banner;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemFilter;
import com.connectly.partnerAdmin.module.display.dto.banner.filter.BannerFilter;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import com.connectly.partnerAdmin.module.display.exception.ContentErrorConstant;
import com.connectly.partnerAdmin.module.display.exception.ContentNotFoundException;
import com.connectly.partnerAdmin.module.display.mapper.banner.BannerMapper;
import com.connectly.partnerAdmin.module.display.repository.component.banner.BannerFindRepository;
import com.connectly.partnerAdmin.module.display.repository.component.banner.BannerItemFindRepository;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BannerFetchServiceImpl implements BannerFetchService {

    private final BannerFindRepository bannerFindRepository;
    private final BannerItemFindRepository bannerItemFindRepository;
    private final BannerMapper bannerMapper;

    @Override
    public Banner fetchBannerEntity(long bannerId){
        return bannerFindRepository.fetchBannerEntity(bannerId).orElseThrow(() -> new ContentNotFoundException(ContentErrorConstant.BANNER_NOT_FOUND_MSG));
    }

    @Override
    public List<BannerItemDto> fetchBannerGroup(long bannerId, BannerItemFilter bannerItemFilterDto) {
        return bannerItemFindRepository.fetchBannerItems(bannerId, bannerItemFilterDto);
    }

    @Override
    public List<BannerItem> fetchBannerItemEntitiesByBannerId(long bannerId) {
        return bannerItemFindRepository.fetchBannerItemEntitiesByBannerId(bannerId);
    }

    @Override
    public List<BannerItem> fetchBannerItemEntities(List<Long> bannerItemIds) {
        return bannerItemFindRepository.fetchBannerItemEntities(bannerItemIds);
    }

    @Override
    public CustomPageable<BannerGroupDto> fetchBannerGroups(BannerFilter filter, Pageable pageable) {
        List<BannerGroupDto> results = bannerFindRepository.fetchBannerGroups(filter, pageable);
        long totalCount =  fetchBannerCountQuery(filter);
        return bannerMapper.toBannerGroupDtos(results, pageable, totalCount);
    }

    private long fetchBannerCountQuery(BannerFilter filter) {
        JPAQuery<Long> longJPAQuery = bannerFindRepository.fetchBannerGroupCountQuery(filter);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) return 0L;
        return totalCount;
    }

}
