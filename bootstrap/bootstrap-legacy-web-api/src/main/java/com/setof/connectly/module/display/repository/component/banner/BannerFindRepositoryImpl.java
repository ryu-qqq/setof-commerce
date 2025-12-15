package com.setof.connectly.module.display.repository.component.banner;

import static com.setof.connectly.module.display.entity.component.item.QBanner.banner;
import static com.setof.connectly.module.display.entity.component.item.QBannerItem.bannerItem;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.banner.BannerFilter;
import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import com.setof.connectly.module.display.dto.banner.QBannerItemDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BannerFindRepositoryImpl implements BannerFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BannerItemDto> fetchBannerItems(BannerFilter filterDto) {
        return queryFactory
                .from(bannerItem)
                .join(banner)
                .on(banner.id.eq(bannerItem.bannerId))
                .where(bannerTypeEq(filterDto), betweenTime(), deleteYnNo(), displayYn())
                .orderBy(bannerItem.displayOrder.asc())
                .transform(
                        GroupBy.groupBy(bannerItem.id)
                                .list(
                                        new QBannerItemDto(
                                                banner.id,
                                                bannerItem.id,
                                                bannerItem.title,
                                                bannerItem.imageUrl,
                                                bannerItem.linkUrl,
                                                bannerItem.displayPeriod)));
    }

    private BooleanExpression deleteYnNo() {
        return banner.deleteYn.eq(Yn.N).and(bannerItem.deleteYn.eq(Yn.N));
    }

    private BooleanExpression bannerTypeEq(BannerFilter filterDto) {
        return banner.bannerType.eq(filterDto.getBannerType());
    }

    private BooleanExpression displayYn() {
        return banner.displayYn.eq(Yn.Y).and(bannerItem.displayYn.eq(Yn.Y));
    }

    private BooleanExpression betweenTime() {
        LocalDateTime now = LocalDateTime.now();
        return banner.displayPeriod
                .displayStartDate
                .loe(now)
                .and(banner.displayPeriod.displayEndDate.goe(now))
                .and(bannerItem.displayPeriod.displayStartDate.loe(now))
                .and(bannerItem.displayPeriod.displayEndDate.goe(now));
    }
}
