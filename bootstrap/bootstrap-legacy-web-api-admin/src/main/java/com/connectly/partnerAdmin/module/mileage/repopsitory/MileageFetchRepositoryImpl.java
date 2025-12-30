package com.connectly.partnerAdmin.module.mileage.repopsitory;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.mileage.entity.Mileage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.connectly.partnerAdmin.module.mileage.entity.QMileage.mileage;
import static com.connectly.partnerAdmin.module.mileage.entity.QMileageHistory.mileageHistory;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.QOrderSnapShotMileage.orderSnapShotMileage;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.QOrderSnapShotMileageDetail.orderSnapShotMileageDetail;
import static com.connectly.partnerAdmin.module.user.entity.QUserMileage.userMileage;


@Repository
@RequiredArgsConstructor
public class MileageFetchRepositoryImpl implements MileageFetchRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Mileage> fetchMileages(List<Long> mileageIds) {
        return queryFactory
                .selectFrom(mileage)
                .innerJoin(mileage.userMileage, userMileage).fetchJoin()
                .where(mileageIdIn(mileageIds))
                .fetch();
    }


    @Override
    public List<Mileage> fetchMileages(long orderId) {
        return queryFactory
                .select(mileage)
                .from(orderSnapShotMileage)
                .innerJoin(orderSnapShotMileage.mileageDetails, orderSnapShotMileageDetail)
                .innerJoin(mileage)
                    .on(orderSnapShotMileageDetail.mileageId.eq(mileage.id))
                .where(orderSnapShotMileage.order.id.eq(orderId))
                .fetch();
    }

    @Override
    public List<Mileage> fetchExpireMileageEntities() {
        return queryFactory
                .selectFrom(mileage)
                .innerJoin(mileage.histories, mileageHistory).fetchJoin()
                .innerJoin(mileage.userMileage, userMileage).fetchJoin()
                .where(beforeExpirationDate(), activeYEq())
                .fetch();
    }

    private BooleanExpression mileageIdIn(List<Long> mileageIds){
        return mileage.id.in(mileageIds);
    }

    private BooleanExpression beforeExpirationDate(){
        return mileage.expirationDate.before(LocalDateTime.now());
    }
    private BooleanExpression activeYEq(){
        return mileage.activeYn.eq(Yn.Y);
    }


}
