package com.setof.connectly.module.mileage.repository.fetch;

import static com.setof.connectly.module.mileage.entity.QMileage.mileage;
import static com.setof.connectly.module.order.entity.snapshot.mileage.QOrderSnapShotMileage.orderSnapShotMileage;
import static com.setof.connectly.module.order.entity.snapshot.mileage.QOrderSnapShotMileageDetail.orderSnapShotMileageDetail;
import static com.setof.connectly.module.payment.entity.snapshot.QPaymentSnapShotMileage.paymentSnapShotMileage;
import static com.setof.connectly.module.user.entity.QUserMileage.userMileage;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.mileage.dto.*;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MileageFindRepositoryImpl implements MileageFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MileageQueryDto> fetchMileages(long userId) {
        return Optional.ofNullable(
                queryFactory
                        .from(userMileage)
                        .leftJoin(mileage)
                        .on(mileage.userId.eq(userMileage.id))
                        .where(
                                userIdEq(userId),
                                isBiggerThanZero(),
                                isActiveYn(),
                                isLoeExpireDate())
                        .orderBy(mileage.expirationDate.asc())
                        .transform(
                                GroupBy.groupBy(userMileage.id)
                                        .as(
                                                new QMileageQueryDto(
                                                        userMileage.currentMileage,
                                                        GroupBy.set(
                                                                new QMileageDto(
                                                                        mileage.id,
                                                                        mileage.mileageAmount,
                                                                        mileage.usedMileageAmount,
                                                                        mileage.issuedDate,
                                                                        mileage.expirationDate,
                                                                        mileage.activeYn)))))
                        .get(userId));
    }

    @Override
    public Optional<MileageRefundQueryDto> fetchUsedMileage(
            long paymentId, long orderId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .from(orderSnapShotMileage)
                        .innerJoin(orderSnapShotMileageDetail)
                        .on(
                                orderSnapShotMileageDetail.orderSnapShotMileageId.eq(
                                        orderSnapShotMileage.id))
                        .innerJoin(paymentSnapShotMileage)
                        .on(paymentSnapShotMileage.paymentId.eq(orderSnapShotMileage.paymentId))
                        .innerJoin(mileage)
                        .on(mileage.id.eq(orderSnapShotMileageDetail.mileageId))
                        .innerJoin(userMileage)
                        .on(userMileage.id.eq(paymentSnapShotMileage.userId))
                        .where(paymentIdEq(paymentId), orderIdEq(orderId), userIdEq(userId))
                        .orderBy(mileage.expirationDate.asc())
                        .transform(
                                GroupBy.groupBy(userMileage.id)
                                        .as(
                                                new QMileageRefundQueryDto(
                                                        userMileage.currentMileage,
                                                        GroupBy.set(
                                                                new QMileageRefundDto(
                                                                        mileage.id,
                                                                        mileage.mileageAmount,
                                                                        mileage.usedMileageAmount,
                                                                        mileage.issuedDate,
                                                                        mileage.expirationDate,
                                                                        mileage.activeYn,
                                                                        orderSnapShotMileageDetail
                                                                                .usedAmount)))))
                        .get(userId));
    }

    private BooleanExpression userIdEq(long userId) {
        return userMileage.id.eq(userId);
    }

    private BooleanExpression isBiggerThanZero() {
        return mileage.mileageAmount.subtract(mileage.usedMileageAmount).gt(0);
    }

    private BooleanExpression isActiveYn() {
        return mileage.activeYn.eq(Yn.Y);
    }

    private BooleanExpression isLoeExpireDate() {
        return mileage.expirationDate.goe(LocalDateTime.now());
    }

    private BooleanExpression paymentIdEq(long paymentId) {
        return orderSnapShotMileage.paymentId.eq(paymentId);
    }

    private BooleanExpression orderIdEq(long orderId) {
        return orderSnapShotMileage.orderId.eq(orderId);
    }
}
