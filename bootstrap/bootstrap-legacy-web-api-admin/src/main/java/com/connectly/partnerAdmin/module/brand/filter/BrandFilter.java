package com.connectly.partnerAdmin.module.brand.filter;

import com.connectly.partnerAdmin.module.brand.enums.MainDisplayNameType;
import com.connectly.partnerAdmin.module.common.filter.LastDomainIdProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandFilter implements LastDomainIdProvider {

    private Long lastDomainId;
    private Long siteId;
    private String brandName;
    private List<String> brandNames;
    private MainDisplayNameType mainDisplayType;

    @Override
    public Long getId() {
        return lastDomainId;
    }

    @Override
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }

}
