package com.connectly.partnerAdmin.module.display.repository.component.banner;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemDto;
import com.connectly.partnerAdmin.module.display.dto.banner.BannerItemFilter;
import com.connectly.partnerAdmin.module.display.dto.banner.QBannerItemDto;
import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.connectly.partnerAdmin.module.display.entity.component.item.QBanner.banner;
import static com.connectly.partnerAdmin.module.display.entity.component.item.QBannerItem.bannerItem;


@Repository
@RequiredArgsConstructor
public class BannerItemFindRepositoryImpl implements BannerItemFindRepository {

    private final JPAQueryFactory queryFactory;



    @Override
    public List<BannerItemDto> fetchBannerItems(long bannerId, BannerItemFilter filterDto) {
        return queryFactory
                .from(bannerItem)
                    .join(banner).on(banner.id.eq(bannerItem.bannerId))
                    .where(bannerIdEq(bannerId), betweenTime(filterDto), bannerItem.deleteYn.eq(Yn.N))
                    .orderBy(bannerItem.displayOrder.asc())
                    .transform(
                        GroupBy.groupBy(bannerItem.id).list(
                                new QBannerItemDto(
                                        bannerItem.id,
                                        banner.bannerType,
                                        bannerItem.title,
                                        bannerItem.displayPeriod,
                                        bannerItem.imageUrl,
                                        bannerItem.linkUrl,
                                        bannerItem.displayOrder,
                                        bannerItem.displayYn,
                                        bannerItem.imageSize
                                )
                        )
                );
    }

    @Override
    public List<BannerItem> fetchBannerItemEntities(List<Long> bannerItemIds) {
        return queryFactory.selectFrom(bannerItem)
                .where(bannerItemIdIn(bannerItemIds))
                .fetch();
    }

    @Override
    public List<BannerItem> fetchBannerItemEntitiesByBannerId(long bannerId) {
        return queryFactory.selectFrom(bannerItem)
                .where(bannerIdEq(bannerId),  bannerItem.deleteYn.eq(Yn.N))
                .fetch();
    }

    private BooleanExpression bannerIdEq(long bannerId){
        return bannerItem.bannerId.eq(bannerId);
    }

    private BooleanExpression bannerItemIdIn(List<Long> bannerItemIds){
        return bannerItem.id.in(bannerItemIds);
    }


    private BooleanExpression betweenTime(BannerItemFilter filterDto){
        if(filterDto.getStartDate() != null && filterDto.getEndDate() != null){
            return bannerItem.displayPeriod.displayStartDate.between(filterDto.getStartDate(), filterDto.getEndDate())
                    .or(bannerItem.displayPeriod.displayEndDate.between(filterDto.getStartDate(), filterDto.getEndDate()))
                    .or(bannerItem.displayPeriod.displayStartDate.loe(filterDto.getStartDate()).and(bannerItem.displayPeriod.displayEndDate.goe(filterDto.getEndDate())));
        }

        else return null;
    }
}
