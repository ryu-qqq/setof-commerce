package com.setof.connectly.module.display.repository.gnb;

import static com.setof.connectly.module.display.entity.gnb.QGnb.gnb;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.gnb.GnbResponse;
import com.setof.connectly.module.display.dto.gnb.QGnbResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GnbFindRepositoryImpl implements GnbFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GnbResponse> fetchGnbs() {
        return queryFactory
                .selectFrom(gnb)
                .where(betweenTime(), deleteYnNo())
                .distinct()
                .orderBy(gnb.gnbDetails.displayOrder.asc())
                .transform(
                        GroupBy.groupBy(gnb.id)
                                .list(
                                        new QGnbResponse(
                                                gnb.id,
                                                gnb.gnbDetails.title,
                                                gnb.gnbDetails.linkUrl,
                                                gnb.gnbDetails.displayOrder,
                                                gnb.gnbDetails.displayPeriod)));
    }

    private BooleanExpression betweenTime() {
        LocalDateTime now = LocalDateTime.now();
        return gnb.gnbDetails
                .displayPeriod
                .displayStartDate
                .loe(now)
                .and(gnb.gnbDetails.displayPeriod.displayEndDate.goe(now));
    }

    private BooleanExpression deleteYnNo() {
        return gnb.deleteYn.eq(Yn.N);
    }
}
