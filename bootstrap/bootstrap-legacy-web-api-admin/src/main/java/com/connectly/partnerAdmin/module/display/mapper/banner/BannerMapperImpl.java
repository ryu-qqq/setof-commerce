package com.connectly.partnerAdmin.module.display.mapper.banner;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerResponse;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBanner;
import com.connectly.partnerAdmin.module.display.dto.banner.CreateBannerItem;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BannerMapperImpl implements BannerMapper{

    @Override
    public BannerResponse toResponse(Banner banner) {
        return BannerResponse.builder()
                .bannerId(banner.getId())
                .bannerType(banner.getBannerType())
                .displayPeriod(banner.getDisplayPeriod())
                .displayYn(banner.getDisplayYn())
                .title(banner.getTitle())
                .insertDate(banner.getInsertDate())
                .updateDate(banner.getUpdateDate())
                .insertOperator(banner.getInsertOperator())
                .updateOperator(banner.getUpdateOperator())
                .build();
    }

    @Override
    public Banner toEntity(CreateBanner createBanner) {
        return Banner.builder()
                .bannerType(createBanner.getBannerType())
                .displayPeriod(createBanner.getDisplayPeriod())
                .displayYn(createBanner.getDisplayYn())
                .title(createBanner.getTitle())
                .build();
    }

    @Override
    public List<BannerItem> toBannerItems(List<CreateBannerItem> createBannerItems) {
        return createBannerItems.stream().map(bannerItem -> {
            if(bannerItem.getBannerItemId() != null){
                return BannerItem.builder()
                        .id(bannerItem.getBannerItemId())
                        .bannerId(bannerItem.getBannerId())
                        .imageUrl(bannerItem.getImageUrl())
                        .displayOrder(bannerItem.getDisplayOrder())
                        .linkUrl(bannerItem.getLinkUrl())
                        .displayPeriod(bannerItem.getDisplayPeriod())
                        .displayYn(bannerItem.getDisplayYn())
                        .title(bannerItem.getTitle())
                        .imageSize(new ImageSize(bannerItem.getWidth(), bannerItem.getHeight()))
                        .build();
            }

            return BannerItem.builder()
                    .bannerId(bannerItem.getBannerId())
                    .imageUrl(bannerItem.getImageUrl())
                    .displayOrder(bannerItem.getDisplayOrder())
                    .linkUrl(bannerItem.getLinkUrl())
                    .displayPeriod(bannerItem.getDisplayPeriod())
                    .displayYn(bannerItem.getDisplayYn())
                    .title(bannerItem.getTitle())
                    .imageSize(new ImageSize(bannerItem.getWidth(), bannerItem.getHeight()))
                    .build();

        }).collect(Collectors.toList());

    }

    @Override
    public CustomPageable<BannerGroupDto> toBannerGroupDtos(List<BannerGroupDto> bannerGroupDtos, Pageable pageable, long total) {
        Long lastDomainId = bannerGroupDtos.isEmpty() ? null : bannerGroupDtos.getLast().getBannerId();
        return new CustomPageable<>(bannerGroupDtos, pageable, total, lastDomainId);
    }

}
