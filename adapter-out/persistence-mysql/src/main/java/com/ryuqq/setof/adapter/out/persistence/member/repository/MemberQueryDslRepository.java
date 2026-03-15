package com.ryuqq.setof.adapter.out.persistence.member.repository;

import static com.ryuqq.setof.adapter.out.persistence.member.entity.QMemberJpaEntity.memberJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.memberauth.entity.QMemberAuthJpaEntity.memberAuthJpaEntity;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.member.condition.MemberConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.memberauth.entity.MemberAuthJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MemberQueryDslRepository - 회원 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class MemberQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final MemberConditionBuilder conditionBuilder;

    public MemberQueryDslRepository(
            JPAQueryFactory queryFactory, MemberConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 회원 ID로 조회.
     *
     * @param memberId 회원 PK
     * @return 회원 Optional
     */
    public Optional<MemberJpaEntity> findByMemberId(Long memberId) {
        MemberJpaEntity entity =
                queryFactory
                        .selectFrom(memberJpaEntity)
                        .where(conditionBuilder.idEq(memberId), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 전화번호로 회원 조회.
     *
     * @param phoneNumber 전화번호
     * @return 회원 Optional
     */
    public Optional<MemberJpaEntity> findByPhoneNumber(String phoneNumber) {
        MemberJpaEntity entity =
                queryFactory
                        .selectFrom(memberJpaEntity)
                        .where(
                                conditionBuilder.phoneNumberEq(phoneNumber),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 전화번호로 가입 여부 확인.
     *
     * @param phoneNumber 전화번호
     * @return 가입 여부
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(memberJpaEntity)
                        .where(
                                conditionBuilder.phoneNumberEq(phoneNumber),
                                conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 전화번호로 회원 + 인증 정보 목록 조회 (로그인용).
     *
     * <p>members LEFT JOIN member_auths ON members.id = member_auths.member_id
     *
     * @param phoneNumber 전화번호
     * @return (MemberJpaEntity, MemberAuthJpaEntity) Tuple 목록
     */
    public List<Tuple> findMemberWithAuthsByPhoneNumber(String phoneNumber) {
        return queryFactory
                .select(memberJpaEntity, memberAuthJpaEntity)
                .from(memberJpaEntity)
                .leftJoin(memberAuthJpaEntity)
                .on(
                        memberJpaEntity.id.eq(memberAuthJpaEntity.memberId),
                        memberAuthJpaEntity.deletedAt.isNull())
                .where(conditionBuilder.phoneNumberEq(phoneNumber), conditionBuilder.notDeleted())
                .fetch();
    }

    /**
     * 회원 ID로 인증 수단 목록 조회.
     *
     * @param memberId 회원 PK
     * @return MemberAuthJpaEntity 목록
     */
    public List<MemberAuthJpaEntity> findAuthsByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(memberAuthJpaEntity)
                .where(
                        memberAuthJpaEntity.memberId.eq(memberId),
                        memberAuthJpaEntity.deletedAt.isNull())
                .fetch();
    }
}
