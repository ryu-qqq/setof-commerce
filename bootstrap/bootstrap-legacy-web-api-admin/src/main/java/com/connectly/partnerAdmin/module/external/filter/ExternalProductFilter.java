package com.connectly.partnerAdmin.module.external.filter;


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
public class ExternalProductFilter implements LastDomainIdProvider {

    private long siteId;
    private MappingStatus mappingStatus;
    private Yn externalIdIsNull;
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
