package com.connectly.partnerAdmin.module.display.dto.banner;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class BannerItemDto {

    private long bannerItemId;
    private BannerType bannerType;
    private String title;
    private DisplayPeriod displayPeriod;
    private String imageUrl;
    private String linkUrl;
    private int displayOrder;
    private Yn displayYn;
    private ImageSize imageSize;

    @QueryProjection
    public BannerItemDto(long bannerItemId, BannerType bannerType, String title, DisplayPeriod displayPeriod, String imageUrl, String linkUrl, int displayOrder, Yn displayYn, ImageSize imageSize) {
        this.bannerItemId = bannerItemId;
        this.bannerType = bannerType;
        this.title = title;
        this.displayPeriod = displayPeriod;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayYn = displayYn;
        this.imageSize = imageSize;
    }
}
