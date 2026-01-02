package com.connectly.partnerAdmin.module.display.dto.banner.filter;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BannerFilter extends SearchAndDateFilter {

    private BannerType bannerType;
    private Yn displayYn;
    private Long lastDomainId;


}
