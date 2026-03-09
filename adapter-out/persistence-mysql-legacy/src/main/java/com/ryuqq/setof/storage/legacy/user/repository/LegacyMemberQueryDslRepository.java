package com.ryuqq.setof.storage.legacy.user.repository;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserGradeEntity.legacyUserGradeEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserMileageEntity.legacyUserMileageEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.user.condition.LegacyMemberConditionBuilder;
import com.ryuqq.setof.storage.legacy.user.dto.LegacyMemberProfileQueryDto;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyMemberQueryDslRepository - 레거시 회원 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Repository
public class LegacyMemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyMemberConditionBuilder conditionBuilder;

    public LegacyMemberQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyMemberConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 사용자 ID로 회원 엔티티 조회.
     *
     * @param userId 사용자 ID
     * @return 회원 엔티티
     */
    public Optional<LegacyUserEntity> findByUserId(Long userId) {
        LegacyUserEntity entity =
                queryFactory
                        .selectFrom(legacyUserEntity)
                        .where(conditionBuilder.userIdEq(userId))
                        .fetchFirst();
        return Optional.ofNullable(entity);
    }

    /**
     * 전화번호로 회원 엔티티 조회.
     *
     * @param phoneNumber 전화번호
     * @return 회원 엔티티
     */
    public Optional<LegacyUserEntity> findByPhoneNumber(String phoneNumber) {
        LegacyUserEntity entity =
                queryFactory
                        .selectFrom(legacyUserEntity)
                        .where(conditionBuilder.phoneNumberEq(phoneNumber))
                        .fetchFirst();
        return Optional.ofNullable(entity);
    }

    /**
     * 전화번호로 가입 여부 확인.
     *
     * @param phoneNumber 전화번호
     * @return 가입 여부
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        Long count =
                queryFactory
                        .select(legacyUserEntity.count())
                        .from(legacyUserEntity)
                        .where(conditionBuilder.phoneNumberEq(phoneNumber))
                        .fetchOne();
        return count != null && count > 0;
    }

    /**
     * 사용자 ID로 회원 프로필 조회 (등급 + 마일리지 JOIN).
     *
     * <p>레거시 fetchUser API와 동일한 JOIN 쿼리입니다.
     *
     * @param userId 사용자 ID
     * @return 회원 프로필 DTO
     */
    public Optional<LegacyMemberProfileQueryDto> findProfileByUserId(Long userId) {
        LegacyMemberProfileQueryDto dto =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyMemberProfileQueryDto.class,
                                        legacyUserEntity.id,
                                        legacyUserEntity.phoneNumber,
                                        legacyUserEntity.name,
                                        legacyUserEntity.email,
                                        legacyUserEntity.dateOfBirth,
                                        legacyUserEntity.gender,
                                        legacyUserEntity.socialLoginType,
                                        legacyUserEntity.socialPkId.coalesce(""),
                                        legacyUserEntity.deleteYn,
                                        legacyUserEntity.withdrawalYn,
                                        legacyUserGradeEntity.gradeName,
                                        legacyUserMileageEntity.currentMileage,
                                        legacyUserEntity.insertDate))
                        .from(legacyUserEntity)
                        .join(legacyUserGradeEntity)
                        .on(legacyUserGradeEntity.id.eq(legacyUserEntity.userGradeId))
                        .join(legacyUserMileageEntity)
                        .on(legacyUserMileageEntity.id.eq(legacyUserEntity.id))
                        .where(conditionBuilder.userIdEq(userId))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }
}
