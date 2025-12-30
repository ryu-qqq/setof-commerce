package com.connectly.partnerAdmin.module.external.repository.site;

import com.connectly.partnerAdmin.module.external.dto.QSiteResponse;
import com.connectly.partnerAdmin.module.external.dto.SiteResponse;
import com.connectly.partnerAdmin.module.external.entity.Site;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.connectly.partnerAdmin.module.external.entity.QSellerSiteRelation.*;
import static com.connectly.partnerAdmin.module.external.entity.QSite.site;

@RequiredArgsConstructor
@Repository
public class SiteFetchRepositoryImpl implements SiteFetchRepository {

    private final JPAQueryFactory queryFactory;



    @Override
    public List<SiteResponse> fetchSites() {
        return queryFactory.select(new QSiteResponse(site.id, site.siteName))
                .from(site).fetch();
    }

    @Override
    public List<SiteResponse> fetchSitesBySellerId(long sellerId) {
        return queryFactory.select(new QSiteResponse(site.id, site.siteName))
                .from(site)
                .innerJoin(sellerSiteRelation)
                    .on(sellerSiteRelation.siteId.eq(site.id))
                    .on(sellerSiteRelation.sellerId.eq(sellerId))
                .fetch();
    }
}
