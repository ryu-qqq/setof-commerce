package com.setof.connectly.module.event.repository;

import static com.setof.connectly.module.event.entity.QEvent.event;
import static com.setof.connectly.module.event.entity.QEventProduct.eventProduct;
import static com.setof.connectly.module.product.entity.group.QProduct.product;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.event.dto.EventProductStockCheck;
import com.setof.connectly.module.event.dto.QEventProductStockCheck;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventProductFindRepositoryImpl implements EventProductFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventProductStockCheck> fetchEventProducts(List<Long> productGroupIds) {
        return queryFactory
                .from(eventProduct)
                .innerJoin(event)
                .on(event.id.eq(eventProduct.eventId))
                .innerJoin(product)
                .on(product.deleteYn.eq(Yn.N))
                .on(eventProduct.productGroupId.eq(product.productGroup.id))
                .where(productGroupIdIn(productGroupIds))
                .orderBy(event.eventDetail.displayPeriod.displayStartDate.desc())
                .distinct()
                .transform(
                        GroupBy.groupBy(eventProduct.productGroupId)
                                .list(
                                        new QEventProductStockCheck(
                                                eventProduct.productGroupId,
                                                eventProduct.limitYn,
                                                eventProduct.limitQty,
                                                event.eventDetail.displayPeriod,
                                                eventProduct.eventPayType,
                                                eventProduct.eventProductType,
                                                eventProduct.rewardsMileage,
                                                GroupBy.set(product.id))));
    }

    @Override
    public Optional<EventProductStockCheck> fetchEventProduct(long productGroupId) {
        return Optional.ofNullable(
                queryFactory
                        .from(eventProduct)
                        .innerJoin(event)
                        .on(event.id.eq(eventProduct.eventId))
                        .innerJoin(product)
                        .on(product.deleteYn.eq(Yn.N))
                        .on(eventProduct.productGroupId.eq(product.productGroup.id))
                        .where(productGroupIdEq(productGroupId), displayPeriodBetweenTime())
                        .orderBy(event.eventDetail.displayPeriod.displayStartDate.desc())
                        .distinct()
                        .transform(
                                GroupBy.groupBy(eventProduct.productGroupId)
                                        .as(
                                                new QEventProductStockCheck(
                                                        eventProduct.productGroupId,
                                                        eventProduct.limitYn,
                                                        eventProduct.limitQty,
                                                        event.eventDetail.displayPeriod,
                                                        eventProduct.eventPayType,
                                                        eventProduct.eventProductType,
                                                        eventProduct.rewardsMileage,
                                                        GroupBy.set(product.id))))
                        .get(productGroupId));
    }

    private BooleanExpression productGroupIdEq(long productGroupId) {
        return eventProduct.productGroupId.eq(productGroupId);
    }

    private BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        return eventProduct.productGroupId.in(productGroupIds);
    }

    private BooleanExpression displayPeriodBetweenTime() {
        LocalDateTime now = LocalDateTime.now();
        return event.eventDetail
                .displayPeriod
                .displayStartDate
                .loe(now)
                .and(event.eventDetail.displayPeriod.displayEndDate.goe(now));
    }
}
