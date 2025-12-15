package com.setof.connectly.module.display.service.component.fetch.banner.fetch;

import com.setof.connectly.module.display.dto.banner.BannerFilter;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import com.setof.connectly.module.display.mapper.banner.BannerMapper;
import com.setof.connectly.module.display.repository.component.banner.BannerFindRepository;
import com.setof.connectly.module.display.service.component.fetch.banner.BannerRedisQueryService;
import com.setof.connectly.module.exception.content.BannerNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BannerFindServiceImpl implements BannerFindService {

    private final BannerFindRepository bannerItemFindRepository;
    private final BannerRedisQueryService bannerRedisQueryService;
    private final BannerMapper bannerMapper;

    @Override
    public List<BannerItemDto> fetchBannerGroup(BannerFilter bannerFilter) {
        List<BannerItemDto> bannerItems = bannerItemFindRepository.fetchBannerItems(bannerFilter);
        List<BannerItemDto> processBannerItems = bannerMapper.toBannerItems(bannerItems);
        if (processBannerItems.isEmpty())
            throw new BannerNotFoundException(bannerFilter.getBannerType());
        bannerRedisQueryService.saveBannerItemInRedis(
                bannerFilter.getBannerType(), processBannerItems);
        return processBannerItems;
    }
}
