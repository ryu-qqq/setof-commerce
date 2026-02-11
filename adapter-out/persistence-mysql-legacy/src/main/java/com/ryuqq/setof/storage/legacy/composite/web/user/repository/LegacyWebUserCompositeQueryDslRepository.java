package com.ryuqq.setof.storage.legacy.composite.web.user.repository;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserGradeEntity.legacyUserGradeEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserMileageEntity.legacyUserMileageEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyMyPageSearchCondition;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyUserSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.condition.LegacyWebUserCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebJoinedUserQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebMyPageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebUserQueryDto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 사용자 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebUserCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebUserCompositeConditionBuilder conditionBuilder;

    public LegacyWebUserCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebUserCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 사용자 정보 조회 (fetchUser용).
     *
     * <p>users + user_grade + user_mileage 조인.
     *
     * @param condition 검색 조건
     * @return 사용자 정보 Optional
     */
    public Optional<LegacyWebUserQueryDto> fetchUser(LegacyUserSearchCondition condition) {
        LegacyWebUserQueryDto dto =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebUserQueryDto.class,
                                        legacyUserEntity.id,
                                        legacyUserEntity.name,
                                        legacyUserEntity.phoneNumber,
                                        legacyUserEntity.socialLoginType.stringValue(),
                                        legacyUserGradeEntity.gradeName.stringValue(),
                                        legacyUserMileageEntity.currentMileage,
                                        legacyUserEntity.insertDate))
                        .from(legacyUserEntity)
                        .join(legacyUserGradeEntity)
                        .on(conditionBuilder.userGradeJoinCondition())
                        .join(legacyUserMileageEntity)
                        .on(conditionBuilder.userMileageJoinCondition())
                        .where(conditionBuilder.userIdEq(condition.userId()))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    /**
     * 가입 사용자 조회 (isExistUser용).
     *
     * <p>users + user_mileage 조인, phoneNumber로 검색.
     *
     * @param condition 검색 조건
     * @return 가입 사용자 정보 Optional
     */
    public Optional<LegacyWebJoinedUserQueryDto> fetchJoinedUser(
            LegacyUserSearchCondition condition) {
        LegacyWebJoinedUserQueryDto dto =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebJoinedUserQueryDto.class,
                                        legacyUserEntity.name,
                                        legacyUserEntity.id,
                                        legacyUserEntity.socialLoginType.stringValue(),
                                        legacyUserEntity.phoneNumber,
                                        legacyUserEntity.socialPkId.coalesce(""),
                                        legacyUserMileageEntity.currentMileage,
                                        legacyUserEntity.insertDate,
                                        legacyUserEntity.deleteYn.stringValue()))
                        .from(legacyUserEntity)
                        .innerJoin(legacyUserMileageEntity)
                        .on(conditionBuilder.userMileageJoinCondition())
                        .where(conditionBuilder.phoneNumberEq(condition.phoneNumber()))
                        .fetchFirst();
        return Optional.ofNullable(dto);
    }

    /**
     * 마이페이지 정보 조회.
     *
     * <p>users + user_mileage + user_grade 조인.
     *
     * @param condition 검색 조건
     * @return 마이페이지 정보 Optional
     */
    public Optional<LegacyWebMyPageQueryDto> fetchMyPage(LegacyMyPageSearchCondition condition) {
        LegacyWebMyPageQueryDto dto =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebMyPageQueryDto.class,
                                        legacyUserEntity.name,
                                        legacyUserEntity.phoneNumber,
                                        legacyUserEntity.email,
                                        legacyUserEntity.socialLoginType.stringValue(),
                                        legacyUserEntity.insertDate,
                                        legacyUserGradeEntity.gradeName.stringValue(),
                                        legacyUserMileageEntity.currentMileage))
                        .from(legacyUserEntity)
                        .innerJoin(legacyUserMileageEntity)
                        .on(conditionBuilder.userMileageJoinCondition())
                        .innerJoin(legacyUserGradeEntity)
                        .on(conditionBuilder.userGradeJoinCondition())
                        .where(conditionBuilder.userIdEq(condition.userId()))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }
}
