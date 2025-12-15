package com.setof.connectly.module.display.dto.banner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class BannerItemDto {

    @JsonIgnore private long bannerId;
    private long bannerItemId;
    private String title;
    private String imageUrl;
    private String linkUrl;

    @JsonIgnore private DisplayPeriod displayPeriod;

    @QueryProjection
    public BannerItemDto(
            long bannerId,
            long bannerItemId,
            String title,
            String imageUrl,
            String linkUrl,
            DisplayPeriod displayPeriod) {
        this.bannerId = bannerId;
        this.bannerItemId = bannerItemId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayPeriod = displayPeriod;
    }
}
