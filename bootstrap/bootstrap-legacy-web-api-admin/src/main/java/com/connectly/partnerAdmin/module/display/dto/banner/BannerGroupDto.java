package com.connectly.partnerAdmin.module.display.dto.banner;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BannerGroupDto {

    private long bannerId;
    private String title;
    private BannerType bannerType;
    private DisplayPeriod displayPeriod;
    private Yn displayYn;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
    private String insertOperator;
    private String updateOperator;


    @QueryProjection
    public BannerGroupDto(long bannerId, String title, BannerType bannerType, DisplayPeriod displayPeriod, Yn displayYn, LocalDateTime insertDate, LocalDateTime updateDate, String insertOperator, String updateOperator) {
        this.bannerId = bannerId;
        this.title = title;
        this.bannerType = bannerType;
        this.displayPeriod = displayPeriod;
        this.displayYn = displayYn;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.insertOperator = insertOperator;
        this.updateOperator = updateOperator;
    }
}
