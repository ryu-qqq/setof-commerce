package com.connectly.partnerAdmin.module.external.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteResponse {
    private long siteId;
    private String siteName;

    @QueryProjection
    public SiteResponse(long siteId, String siteName) {
        this.siteId = siteId;
        this.siteName = siteName;
    }
}
