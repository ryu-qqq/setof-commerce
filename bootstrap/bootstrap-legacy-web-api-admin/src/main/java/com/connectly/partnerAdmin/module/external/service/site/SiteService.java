package com.connectly.partnerAdmin.module.external.service.site;

import com.connectly.partnerAdmin.module.external.dto.SiteResponse;

import java.util.List;

public interface SiteService {
    List<SiteResponse> fetchSites();
    List<SiteResponse> fetchSitesBySellerId(long sellerId);
}
