package com.setof.connectly.module.event.repository;

import static com.setof.connectly.module.event.entity.QEvent.event;
import static com.setof.connectly.module.event.entity.QEventMileage.eventMileage;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.dto.QEventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventMileageFindRepositoryImpl implements EventMileageFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<EventMileageDto> fetchEventMileageInfo(EventMileageType eventMileageType) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QEventMileageDto(
                                        eventMileage.mileageAmount,
                                        eventMileage.mileageRate,
                                        eventMileage.expirationDate,
                                        eventMileage.mileageType,
                                        event.eventDetail.displayPeriod))
                        .from(eventMileage)
                        .innerJoin(event)
                        .on(event.id.eq(eventMileage.eventId))
                        .where(displayPeriodBetweenTime(), eventMileageTypeEq(eventMileageType))
                        .limit(1)
                        .distinct()
                        .orderBy(event.eventDetail.displayPeriod.displayStartDate.desc())
                        .distinct()
                        .fetchOne());
    }

    private BooleanExpression displayPeriodBetweenTime() {
        LocalDateTime now = LocalDateTime.now();
        return event.eventDetail
                .displayPeriod
                .displayStartDate
                .loe(now)
                .and(event.eventDetail.displayPeriod.displayEndDate.goe(now));
    }

    private BooleanExpression eventMileageTypeEq(EventMileageType eventMileageType) {
        return eventMileage.mileageType.eq(eventMileageType);
    }
}
