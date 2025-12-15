package com.setof.connectly.module.mileage.repository.history.fetch;

import static com.setof.connectly.module.mileage.entity.QMileage.mileage;
import static com.setof.connectly.module.mileage.entity.QMileageHistory.mileageHistory;
import static com.setof.connectly.module.order.entity.snapshot.mileage.QOrderSnapShotMileage.orderSnapShotMileage;
import static com.setof.connectly.module.order.entity.snapshot.mileage.QOrderSnapShotMileageDetail.orderSnapShotMileageDetail;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.mileage.dto.filter.MileageFilter;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.Reason;
import com.setof.connectly.module.user.dto.mileage.QUserMileageHistoryDto;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MileageHistoryFindRepositoryImpl implements MileageHistoryFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserMileageHistoryDto> fetchMileageHistories(
            MileageFilter filter, long userId, Pageable pageable) {
        return queryFactory
                .select(
                        new QUserMileageHistoryDto(
                                mileageHistory.id,
                                mileage.id,
                                mileage.title,
                                orderSnapShotMileage.paymentId,
                                orderSnapShotMileage.orderId,
                                mileageHistory.changeAmount,
                                mileageHistory.reason,
                                mileageHistory.insertDate,
                                mileage.expirationDate))
                .from(mileage)
                .innerJoin(mileageHistory)
                .on(mileage.id.eq(mileageHistory.mileageId))
                .leftJoin(orderSnapShotMileageDetail)
                .on(orderSnapShotMileageDetail.mileageId.eq(mileage.id))
                .leftJoin(orderSnapShotMileage)
                .on(orderSnapShotMileage.id.eq(orderSnapShotMileageDetail.orderSnapShotMileageId))
                .on(orderSnapShotMileage.orderId.eq(mileageHistory.targetId))
                .where(
                        userIdEq(userId),
                        reasonEq(filter),
                        targetIdIsZero()
                                .or(hasValidOrderIdAndPaymentId().or(noOrderIdAndPaymentId())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .orderBy(mileageHistory.insertDate.desc())
                .groupBy(
                        mileageHistory.id,
                        mileage.id,
                        mileage.title,
                        orderSnapShotMileage.paymentId,
                        orderSnapShotMileage.orderId,
                        mileageHistory.changeAmount,
                        mileageHistory.reason,
                        mileageHistory.insertDate,
                        mileage.expirationDate)
                .fetch();
    }

    @Override
    public JPAQuery<Long> fetchMileageHistoryCount(MileageFilter filter, long userId) {
        return queryFactory
                .select(mileageHistory.count())
                .from(mileageHistory)
                .where(mileageHistory.userId.eq(userId), reasonEq(filter))
                .distinct();
    }

    private BooleanExpression userIdEq(long userId) {
        return mileage.userId.eq(userId);
    }

    private BooleanExpression reasonEq(MileageFilter filter) {
        if (filter.getReasons() != null) {
            if (!filter.getReasons().isEmpty())
                return mileageHistory.reason.in(filter.getReasons());
        }
        return null;
    }

    private BooleanExpression issueTypeEq() {
        return mileageHistory.issueType.eq(MileageIssueType.ORDER);
    }

    private BooleanExpression hasValidOrderIdAndPaymentId() {
        return orderSnapShotMileage.orderId.gt(0L).and(orderSnapShotMileage.paymentId.gt(0L));
    }
    ;

    private BooleanExpression noOrderIdAndPaymentId() {
        return orderSnapShotMileage.orderId.loe(0L).or(orderSnapShotMileage.paymentId.loe(0L));
    }
    ;

    private BooleanExpression targetIdIsZero() {
        return mileageHistory.targetId.eq(0L);
    }
    ;

    private BooleanExpression isUseReason() {
        return mileageHistory.reason.eq(Reason.USE).or(mileageHistory.reason.eq(Reason.REFUND));
    }
    ;
}
