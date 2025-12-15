package com.setof.connectly.module.user.repository.mypage;

import static com.setof.connectly.module.user.entity.QUserGrade.userGrade;
import static com.setof.connectly.module.user.entity.QUserMileage.userMileage;
import static com.setof.connectly.module.user.entity.QUsers.users;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.user.dto.mypage.MyPageResponse;
import com.setof.connectly.module.user.dto.mypage.QMyPageResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyPageFindRepositoryImpl implements MyPageFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MyPageResponse> fetchMyPage(long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QMyPageResponse(
                                        users.name,
                                        users.phoneNumber,
                                        users.email,
                                        users.socialLoginType,
                                        users.insertDate,
                                        userGrade.gradeName,
                                        userMileage.currentMileage))
                        .from(users)
                        .innerJoin(userMileage)
                        .on(users.id.eq(userMileage.id))
                        .innerJoin(userGrade)
                        .on(users.userGradeId.eq(userGrade.id))
                        .where(userIdEq(userId))
                        .fetchOne());
    }

    public BooleanExpression userIdEq(long userId) {
        return userMileage.id.eq(userId);
    }
}
