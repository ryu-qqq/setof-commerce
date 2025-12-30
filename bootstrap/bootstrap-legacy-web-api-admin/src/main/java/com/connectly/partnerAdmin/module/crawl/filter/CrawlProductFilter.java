package com.connectly.partnerAdmin.module.crawl.filter;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.filter.LastDomainIdProvider;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CrawlProductFilter implements LastDomainIdProvider {

    private long siteId;
    private MappingStatus mappingStatus;
    private Yn productGroupIdIsNull;
    private Long lastDomainId;

    @Override
    public Long getId() {
        return lastDomainId;
    }

    @Override
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }

}
