package com.connectly.partnerAdmin.module.display.repository.component.banner;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.QBannerGroupDto;
import com.connectly.partnerAdmin.module.display.dto.banner.filter.BannerFilter;
import com.connectly.partnerAdmin.module.display.entity.component.item.Banner;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import com.connectly.partnerAdmin.module.utils.QueryDslUtil;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.connectly.partnerAdmin.module.display.entity.component.item.QBanner.banner;


@Slf4j
@Repository
public class BannerFindRepositoryImpl extends AbstractCommonRepository implements BannerFindRepository{


    protected BannerFindRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public Optional<Banner> fetchBannerEntity(long bannerId) {
        return Optional.ofNullable(
                getQueryFactory().selectFrom(banner)
                        .where(bannerIdEq(bannerId))
                        .fetchOne()
        );

    }

    @Override
    public List<BannerGroupDto> fetchBannerGroups(BannerFilter filter, Pageable pageable) {

        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable);

        return getQueryFactory().from(banner)
                .where(
                        displayYnEq(filter.getDisplayYn()),
                        bannerTypeEq(filter.getBannerType()),
                        isCursorRead(filter.getLastDomainId()),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        betweenTime(filter),
                        notDeleted()
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .transform(
                        GroupBy.groupBy(banner.id)
                                .list(
                            new QBannerGroupDto(
                                    banner.id,
                                    banner.title,
                                    banner.bannerType,
                                    banner.displayPeriod,
                                    banner.displayYn,
                                    banner.insertDate,
                                    banner.updateDate,
                                    banner.insertOperator,
                                    banner.updateOperator
                            )
                        )
                );
    }

    @Override
    public JPAQuery<Long> fetchBannerGroupCountQuery(BannerFilter filter) {
        return getQueryFactory()
                .select(banner.count())
                .from(banner)
                .where(
                        displayYnEq(filter.getDisplayYn()),
                        bannerTypeEq(filter.getBannerType()),
                        isCursorRead(filter.getLastDomainId()),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        betweenTime(filter),
                        notDeleted()
                )
                .distinct();
    }

    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> allOrderSpecifiers = super.getAllOrderSpecifiers(pageable);
        if(allOrderSpecifiers.isEmpty()){
            allOrderSpecifiers.add(banner.id.asc());
        }
        return allOrderSpecifiers;
    }

    private BooleanExpression bannerIdEq(long bannerId){
       return banner.id.eq(bannerId);
    }

    private BooleanExpression displayYnEq(Yn displayYn){
        if(displayYn != null) return banner.displayYn.eq(displayYn);
        return null;
    }

    private BooleanExpression bannerTypeEq(BannerType bannerType){
        if(bannerType !=null) return banner.bannerType.eq(bannerType);
        return null;
    }

    private BooleanExpression notDeleted() {
        return QueryDslUtil.notDeleted(banner.deleteYn, Yn.N);
    }


    private BooleanExpression isCursorRead(Long lastDomainId){
        if (lastDomainId != null && lastDomainId > 0) {
                return bannerIdLt(lastDomainId);
        }
        return null;
    }

    private BooleanExpression bannerIdLt(long lastDomainId) {
        return lastDomainId > 0 ? banner.id.lt(lastDomainId) : null;
    }


    private BooleanExpression betweenTime(BannerFilter filterDto){
        return banner.displayPeriod.displayStartDate.between(filterDto.getStartDate(), filterDto.getEndDate())
                .or(banner.displayPeriod.displayEndDate.between(filterDto.getStartDate(), filterDto.getEndDate()))
                .or(banner.displayPeriod.displayStartDate.loe(filterDto.getStartDate()).and(banner.displayPeriod.displayEndDate.goe(filterDto.getEndDate())));

    }


}
