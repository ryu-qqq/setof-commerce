package com.setof.connectly.module.user.repository.fetch;

import static com.setof.connectly.module.user.entity.QUserGrade.userGrade;
import static com.setof.connectly.module.user.entity.QUserMileage.userMileage;
import static com.setof.connectly.module.user.entity.QUsers.users;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.user.dto.JoinedDto;
import com.setof.connectly.module.user.dto.QJoinedDto;
import com.setof.connectly.module.user.dto.QUserDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.entity.Users;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFindRepositoryImpl implements UserFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Users> fetchUserEntity(String phoneNumber) {
        return Optional.ofNullable(
                queryFactory.selectFrom(users).where(phoneNumberEq(phoneNumber)).fetchFirst());
    }

    @Override
    public Optional<Users> fetchUserEntity(long userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(users).where(userIdEq(userId)).fetchFirst());
    }

    @Override
    public Optional<JoinedDto> isJoinedMember(String phoneNumber) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QJoinedDto(
                                        users.name,
                                        users.id,
                                        users.socialLoginType,
                                        users.phoneNumber,
                                        users.socialPkId.coalesce(""),
                                        userMileage.currentMileage,
                                        users.insertDate,
                                        users.deleteYn))
                        .from(users)
                        .innerJoin(userMileage)
                        .on(users.id.eq(userMileage.id))
                        .where(phoneNumberEq(phoneNumber))
                        .fetchFirst());
    }

    @Override
    public Optional<JoinedDto> isJoinedMemberByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QJoinedDto(
                                        users.name,
                                        users.id,
                                        users.socialLoginType,
                                        users.phoneNumber,
                                        users.socialPkId.coalesce(""),
                                        userMileage.currentMileage,
                                        users.insertDate,
                                        users.deleteYn))
                        .from(users)
                        .innerJoin(userMileage)
                        .on(users.id.eq(userMileage.id))
                        .where(emailEq(email))
                        .fetchFirst());
    }

    @Override
    public Optional<UserDto> fetchUser(long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QUserDto(
                                        users.id,
                                        users.socialLoginType,
                                        users.phoneNumber,
                                        users.name,
                                        userGrade.gradeName,
                                        userMileage.currentMileage,
                                        users.insertDate))
                        .from(users)
                        .join(userGrade)
                        .on(userGrade.id.eq(users.userGradeId))
                        .join(userMileage)
                        .on(userMileage.id.eq(users.id))
                        .where(userIdEq(userId))
                        .fetchOne());
    }

    private BooleanExpression phoneNumberEq(String phoneNumber) {
        return users.phoneNumber.eq(phoneNumber);
    }

    private BooleanExpression emailEq(String email) {
        return users.email.eq(email);
    }

    private BooleanExpression userIdEq(long userId) {
        return users.id.eq(userId);
    }
}
