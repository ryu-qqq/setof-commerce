package com.connectly.partnerAdmin.module.external.repository.site;

import com.connectly.partnerAdmin.module.display.entity.component.sub.product.CategoryComponent;
import com.connectly.partnerAdmin.module.external.dto.SiteResponse;
import com.connectly.partnerAdmin.module.external.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteFetchRepository {
    List<SiteResponse> fetchSites();
    List<SiteResponse> fetchSitesBySellerId(long sellerId);
}
