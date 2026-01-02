package com.connectly.partnerAdmin.module.external.repository.seller;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.dto.QSellerExternalIntegrationDto;
import com.connectly.partnerAdmin.module.external.dto.SellerExternalIntegrationDto;
import com.connectly.partnerAdmin.module.external.enums.SiteType;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.connectly.partnerAdmin.module.external.entity.QSellerSiteRelation.sellerSiteRelation;

@Repository
@RequiredArgsConstructor
public class InterlockingSellerFetchRepositoryImpl implements InterlockingSellerFetchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<SellerExternalIntegrationDto> fetchInterLockingSites(Set<Long> sellerIds){
        return queryFactory
                .from(sellerSiteRelation)
                .where(sellerIdIn(sellerIds), activeYn(), siteTypeEq())
                .transform(
                        GroupBy.groupBy(sellerSiteRelation.sellerId).list(
                                new QSellerExternalIntegrationDto(
                                        sellerSiteRelation.sellerId,
                                        GroupBy.list(sellerSiteRelation.siteId)
                                )
                        )
                );
    }


    private BooleanExpression activeYn(){
        return sellerSiteRelation.activeYn.eq(Yn.Y);
    }


    private BooleanExpression sellerIdIn(Set<Long> sellerIds){
        return sellerSiteRelation.sellerId.in(sellerIds);
    }

    private BooleanExpression siteTypeEq(){
        return sellerSiteRelation.siteType.eq(SiteType.INTERLOCKING);
    }

}
