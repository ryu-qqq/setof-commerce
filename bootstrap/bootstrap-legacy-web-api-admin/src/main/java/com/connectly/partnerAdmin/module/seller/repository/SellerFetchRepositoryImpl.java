package com.connectly.partnerAdmin.module.seller.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.external.dto.QSiteResponse;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.seller.core.QBaseSellerContext;
import com.connectly.partnerAdmin.module.seller.core.QBusinessSellerContext;
import com.connectly.partnerAdmin.module.seller.core.SellerContext;
import com.connectly.partnerAdmin.module.seller.dto.BusinessValidation;
import com.connectly.partnerAdmin.module.seller.dto.QSellerDetailResponse;
import com.connectly.partnerAdmin.module.seller.dto.QSellerResponse;
import com.connectly.partnerAdmin.module.seller.dto.SellerDetailResponse;
import com.connectly.partnerAdmin.module.seller.dto.SellerResponse;
import com.connectly.partnerAdmin.module.seller.filter.SellerFilter;
import com.connectly.partnerAdmin.module.utils.QueryDslUtil;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.auth.entity.QAdminAuthGroup.adminAuthGroup;
import static com.connectly.partnerAdmin.auth.entity.QAdministrators.administrators;
import static com.connectly.partnerAdmin.auth.entity.QAuthGroup.authGroup;
import static com.connectly.partnerAdmin.module.external.entity.QSellerSiteRelation.sellerSiteRelation;
import static com.connectly.partnerAdmin.module.external.entity.QSite.site;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;
import static com.connectly.partnerAdmin.module.seller.entity.QSellerBusinessInfo.sellerBusinessInfo;
import static com.connectly.partnerAdmin.module.seller.entity.QSellerShippingInfo.sellerShippingInfo;


@Repository
public class SellerFetchRepositoryImpl extends AbstractCommonRepository implements SellerFetchRepository {


    protected SellerFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public boolean fetchHasSellerExist(long sellerId) {
        Long sellerIdOpt = getQueryFactory().select(
                        seller.id
                )
                .from(seller)
                .where(sellerIdEq(sellerId))
                .fetchOne();

        return sellerIdOpt != null;
    }

    @Override
    public Optional<SellerContext> fetchSellerInfo(String email) {

        return Optional.ofNullable(
                getQueryFactory().select(
                        new QBaseSellerContext(
                                seller.id,
                                administrators.email,
                                administrators.passwordHash,
                                authGroup.authGroupType,
                                seller.approvalStatus
                        )
                ).from(administrators)
                        .innerJoin(adminAuthGroup)
                            .on(adminAuthGroup.id.eq(administrators.id))
                        .innerJoin(authGroup)
                            .on(authGroup.id.eq(adminAuthGroup.authGroupId))
                        .innerJoin(seller)
                            .on(seller.id.eq(administrators.sellerId))
                        .where(emailEq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<SellerDetailResponse> fetchSellerDetail(long sellerId) {
        return Optional.ofNullable(
                getQueryFactory()
                        .from(seller)
                        .innerJoin(sellerBusinessInfo)
                            .on(sellerBusinessInfo.id.eq(seller.id))
                        .innerJoin(sellerShippingInfo)
                            .on(sellerShippingInfo.id.eq(seller.id))
                        .leftJoin(sellerSiteRelation)
                            .on(sellerSiteRelation.sellerId.eq(seller.id), relationActiveYn(Yn.Y))
                        .leftJoin(site)
                            .on(site.id.eq(sellerSiteRelation.siteId))
                        .where(seller.id.eq(sellerId))
                        .transform(
                                GroupBy.groupBy(seller.id).as(
                                        new QSellerDetailResponse(
                                                seller.id,
                                                seller.sellerName,
                                                seller.sellerLogoUrl,
                                                seller.commissionRate,
                                                seller.approvalStatus,
                                                seller.sellerDescription,
                                                sellerBusinessInfo.businessAddressLine1,
                                                sellerBusinessInfo.businessAddressLine2,
                                                sellerBusinessInfo.businessAddressZipCode,
                                                sellerShippingInfo.returnAddressLine1,
                                                sellerShippingInfo.returnAddressLine2,
                                                sellerShippingInfo.returnAddressZipCode,
                                                sellerBusinessInfo.csPhoneNumber,
                                                sellerBusinessInfo.csNumber,
                                                sellerBusinessInfo.csEmail,
                                                sellerBusinessInfo.registrationNumber,
                                                sellerBusinessInfo.saleReportNumber,
                                                sellerBusinessInfo.representative,
                                                sellerBusinessInfo.bankName,
                                                sellerBusinessInfo.accountNumber,
                                                sellerBusinessInfo.accountHolderName,
                                                GroupBy.list(
                                                        new QSiteResponse(
                                                                site.id,
                                                                site.siteName
                                                        )
                                                )
                                        )
                                )
                        )
                        .get(sellerId)
        );
    }

    @Override
    public List<BusinessSellerContext> fetchSellersBusinessInfo(List<Long> sellerIds) {
        return getQueryFactory().select(
                        new QBusinessSellerContext(
                                seller.id,
                                sellerBusinessInfo.csEmail,
                                sellerBusinessInfo.companyName,
                                seller.sellerLogoUrl.coalesce(""),
                                seller.sellerDescription.coalesce(""),
                                sellerShippingInfo.returnAddressLine1
                                        .concat(" " + sellerShippingInfo.returnAddressLine2)
                                        .concat(" " + sellerShippingInfo.returnAddressZipCode),
                                sellerBusinessInfo.csNumber.coalesce(sellerBusinessInfo.csPhoneNumber),
                                sellerBusinessInfo.csPhoneNumber,
                                sellerBusinessInfo.registrationNumber,
                                sellerBusinessInfo.saleReportNumber,
                                sellerBusinessInfo.representative,
                                seller.commissionRate
                        )
                )
                .from(seller)
                .innerJoin(sellerBusinessInfo)
                    .on(sellerBusinessInfo.id.eq(seller.id))
                .innerJoin(sellerShippingInfo)
                    .on(sellerShippingInfo.id.eq(seller.id))
                .where(sellerIdIn(sellerIds))
                .fetch();
    }



    @Override
    public List<SellerResponse> fetchSellers(SellerFilter filter, Pageable pageable) {
        return getQueryFactory().select(
                        new QSellerResponse(
                                seller.id,
                                seller.sellerName,
                                seller.commissionRate,
                                seller.approvalStatus,
                                sellerBusinessInfo.csPhoneNumber,
                                sellerBusinessInfo.csEmail,
                                seller.insertDate
                        )
                )
                .from(seller)
                .innerJoin(sellerBusinessInfo)
                    .on(sellerBusinessInfo.id.eq(seller.id))
                .leftJoin(sellerSiteRelation)
                    .on(sellerSiteRelation.sellerId.eq(seller.id), sellerSiteRelation.activeYn.eq(Yn.Y))
                .where(searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        deleteYn(),
                        approvalStatusEq(filter.getStatus()),
                        siteIdIn(filter.getSiteIds())
                        )
                .groupBy(seller.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public JPAQuery<Long> fetchSellerCountQuery(SellerFilter filter) {
        return getQueryFactory()
                .select(seller.id.countDistinct())
                .from(seller)
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.id.eq(seller.id))
                .leftJoin(sellerSiteRelation).on(sellerSiteRelation.sellerId.eq(seller.id), sellerSiteRelation.activeYn.eq(Yn.Y))
                .where(
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        deleteYn(),
                        approvalStatusEq(filter.getStatus()),
                        siteIdIn(filter.getSiteIds())
                );
    }

    @Override
    public boolean fetchBusinessValidation(BusinessValidation businessValidation) {
        List<Long> fetch = getQueryFactory()
            .select(
                sellerBusinessInfo.id
            )
            .from(seller)
            .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.id.eq(seller.id))
            .where(
                deleteYn(),
                sellerBusinessInfo.registrationNumber.eq(businessValidation.getRegistrationNumber())
            )
            .fetch();

        return !fetch.isEmpty();
    }

    private BooleanExpression sellerIdEq(long sellerId){
        return seller.id.eq(sellerId);
    }

    private BooleanExpression sellerIdIn(List<Long> sellerIds){
        return seller.id.in(sellerIds);
    }

    private BooleanExpression emailEq(String email){
        return administrators.email.eq(email);
    }

    private BooleanExpression deleteYn(){
        return QueryDslUtil.notDeleted(seller.deleteYn, Yn.N);
    }

    private BooleanExpression approvalStatusEq(ApprovalStatus status) {
        return status != null ? seller.approvalStatus.eq(status) : null;
    }

    private BooleanExpression relationActiveYn(Yn yn){
        return sellerSiteRelation.activeYn.eq(yn);
    }

    private BooleanExpression siteIdIn(List<Long> siteIds) {
        return (siteIds != null && !siteIds.isEmpty()) ? sellerSiteRelation.siteId.in(siteIds) : null;
    }

}
