package com.connectly.partnerAdmin.module.display.repository.gnb;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.gnb.QGnbResponse;
import com.connectly.partnerAdmin.module.display.dto.gnb.filter.GnbFilter;
import com.connectly.partnerAdmin.module.display.dto.gnb.GnbResponse;
import com.connectly.partnerAdmin.module.display.entity.gnb.Gnb;
import com.connectly.partnerAdmin.module.utils.QueryDslUtil;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.connectly.partnerAdmin.module.display.entity.gnb.QGnb.gnb;


@Repository
@RequiredArgsConstructor
public class GnbFetchRepositoryImpl implements GnbFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GnbResponse> fetchGnbs(GnbFilter filter) {
        return queryFactory.from(gnb)
                .where(isBetweenDisplayPeriod(filter.getStartDate(), filter.getEndDate()), notDelete())
                .distinct()
                .orderBy(gnb.gnbDetails.displayOrder.asc())
                .transform(
                        GroupBy.groupBy(gnb.id).list(
                                new QGnbResponse(
                                        gnb.id,
                                        gnb.gnbDetails
                                )
                        )
                );
    }

    @Override
    public List<Gnb> fetchGnbEntities(List<Long> gnbIds) {
        return queryFactory.selectFrom(gnb)
                .where(gndIdIn(gnbIds))
                .distinct()
                .fetch();
    }


    private BooleanExpression isBetweenDisplayPeriod(LocalDateTime startDate, LocalDateTime endDate){
        return gnb.gnbDetails.displayPeriod.displayStartDate.between(startDate, endDate)
                .or(gnb.gnbDetails.displayPeriod.displayEndDate.between(startDate, endDate))
                .or(gnb.gnbDetails.displayPeriod.displayStartDate.loe(startDate)
                        .and(gnb.gnbDetails.displayPeriod.displayEndDate.goe(endDate)));
    }


    private BooleanExpression notDelete(){
        return QueryDslUtil.notDeleted(gnb.deleteYn, Yn.N);
    }


    private BooleanExpression gndIdIn(List<Long> gnbIds){
        return gnb.id.in(gnbIds);
    }

}
