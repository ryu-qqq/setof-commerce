package com.setof.connectly.module.user.repository.mileage;

import static com.setof.connectly.module.mileage.entity.QMileage.mileage;
import static com.setof.connectly.module.mileage.entity.QMileageTransaction.mileageTransaction;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.mileage.dto.query.PendingMileageDto;
import com.setof.connectly.module.mileage.dto.query.QPendingMileageDto;
import com.setof.connectly.module.mileage.dto.query.QUserMileageQueryDto;
import com.setof.connectly.module.mileage.dto.query.UserMileageQueryDto;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMileageFindRepositoryImpl implements UserMileageFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserMileageQueryDto> fetchUserMileageQueryInMyPage(long userId) {
        return queryFactory
                .from(mileage)
                .where(userIdEq(userId), isBiggerThanZero(), activeYn())
                .transform(
                        GroupBy.groupBy(mileage.id)
                                .list(
                                        new QUserMileageQueryDto(
                                                mileage.userId,
                                                mileage.id,
                                                mileage.mileageAmount,
                                                mileage.usedMileageAmount,
                                                mileage.activeYn,
                                                mileage.issuedDate,
                                                mileage.expirationDate)));
    }

    @Override
    public List<PendingMileageDto> fetchUserPendingMileages(long userId) {
        return queryFactory
                .from(mileageTransaction)
                .where(mileageTransaction.userId.eq(userId), isMileagePendingStatus())
                .transform(
                        GroupBy.groupBy(mileageTransaction.id)
                                .list(
                                        new QPendingMileageDto(
                                                mileageTransaction.expectedMileageAmount,
                                                mileageTransaction.issueType,
                                                mileageTransaction.status)));
    }

    private BooleanExpression userIdEq(long userId) {
        return mileage.userId.eq(userId);
    }

    private BooleanExpression activeYn() {
        return mileage.activeYn.eq(Yn.Y);
    }

    private BooleanExpression isBiggerThanZero() {
        return mileage.mileageAmount.subtract(mileage.usedMileageAmount).gt(0);
    }

    private BooleanExpression isMileagePendingStatus() {
        return mileageTransaction.status.eq(MileageStatus.PENDING);
    }
}
