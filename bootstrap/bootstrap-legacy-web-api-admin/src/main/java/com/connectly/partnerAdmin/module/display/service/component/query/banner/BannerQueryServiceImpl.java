package com.connectly.partnerAdmin.module.display.service.component.query.banner;


import com.connectly.partnerAdmin.module.display.dto.banner.BannerResponse;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBanner;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBannerItem;
import com.connectly.partnerAdmin.module.display.dto.banner.UpdateBannerDisplayYn;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import com.connectly.partnerAdmin.module.display.mapper.banner.BannerMapper;
import com.connectly.partnerAdmin.module.display.repository.component.banner.BannerItemJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.banner.BannerRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.banner.BannerFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BannerQueryServiceImpl implements BannerQueryService{

    private final BannerMapper bannerMapper;
    private final BannerRepository bannerRepository;
    private final BannerItemJdbcRepository bannerItemJdbcRepository;
    private final BannerFetchService bannerFetchService;
    private final BannerRedisQueryService bannerRedisQueryService;

    @Override
    public BannerResponse enrollBanner(CreateBanner createBanner) {
        Banner banner = bannerMapper.toEntity(createBanner);
        bannerRepository.save(banner);
        return bannerMapper.toResponse(banner);
    }

    @Override
    public BannerResponse updateBanner(long bannerId, CreateBanner createBanner) {
        Banner banner = bannerFetchService.fetchBannerEntity(bannerId);
        banner.update(createBanner);
        deleteBannerInRedis(banner.getBannerType());
        return bannerMapper.toResponse(banner);
    }

    @Override
    public List<CreateBannerItem> enrollBannerItems(List<CreateBannerItem> createBannerItems) {

        updateBannerItems(createBannerItems);

        List<CreateBannerItem> newBanners = createBannerItems.stream()
                .filter(bannerItem -> bannerItem.getBannerItemId() == null)
                .collect(Collectors.toList());

        List<BannerItem> bannerItems = bannerMapper.toBannerItems(newBanners);
        bannerItemJdbcRepository.saveAll(bannerItems);


        return createBannerItems;
    }

    @Override
    public BannerResponse updateDisplayYn(long bannerId, UpdateBannerDisplayYn updateBannerDisplayYn) {
        Banner banner = bannerFetchService.fetchBannerEntity(bannerId);
        banner.updateDisplayYn(updateBannerDisplayYn.getDisplayYn());
        deleteBannerInRedis(banner.getBannerType());
        return bannerMapper.toResponse(banner);
    }

    public void updateBannerItems(List<CreateBannerItem> updateBanners){
        long bannerId = updateBanners.get(0).getBannerId();

        List<BannerItem> bannerItems = bannerMapper.toBannerItems(updateBanners);

        Map<Long, BannerItem> bannerItemIdMap = bannerItems.stream()
                .filter(bannerItem -> bannerItem.getId() >0)
                .collect(Collectors.toMap(BannerItem::getId, Function.identity()));

        List<BannerItem> findBannerItems = bannerFetchService.fetchBannerItemEntitiesByBannerId(bannerId);
        List<BannerItem> existingBannerItems = new ArrayList<>();

        findBannerItems.forEach(bannerItem -> {
            BannerItem updateBannerItem = bannerItemIdMap.getOrDefault(bannerItem.getId(), null);
            if(updateBannerItem != null){
                bannerItem.update(updateBannerItem);
                existingBannerItems.add(bannerItem);
            }
        });

        findBannerItems.removeAll(existingBannerItems);
        findBannerItems.forEach(BannerItem::delete);
        deleteBannerItemsInRedis(bannerId);
    }

    private void deleteBannerItemsInRedis(long bannerId){
        Banner banner = bannerFetchService.fetchBannerEntity(bannerId);
        bannerRedisQueryService.deleteBannerItemsInRedis(banner.getBannerType());
    }

    private void deleteBannerInRedis(BannerType bannerType){
        bannerRedisQueryService.deleteBannerItemsInRedis(bannerType);
    }


}
