package com.connectly.partnerAdmin.module.mileage.repopsitory;

import com.connectly.partnerAdmin.module.user.entity.UserMileage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.connectly.partnerAdmin.module.user.entity.QUserMileage.userMileage;


@Repository
@RequiredArgsConstructor
public class MileageHistoryFetchRepositoryImpl implements MileageHistoryFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserMileage> fetchUserMileageEntity(long userId) {
        return Optional.ofNullable(
                queryFactory
                .selectFrom(userMileage)
                .where(userIdEq(userId))
                .fetchOne()
        );
    }

    private BooleanExpression userIdEq(long userId){
        return userMileage.users.id.eq(userId);
    }

}
