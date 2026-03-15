package com.ryuqq.setof.adapter.out.persistence.memberauth.repository;

import static com.ryuqq.setof.adapter.out.persistence.memberauth.entity.QMemberAuthJpaEntity.memberAuthJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.memberauth.condition.MemberAuthConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.memberauth.entity.MemberAuthJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MemberAuthQueryDslRepository - 회원 인증 수단 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class MemberAuthQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final MemberAuthConditionBuilder conditionBuilder;

    public MemberAuthQueryDslRepository(
            JPAQueryFactory queryFactory, MemberAuthConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 회원 ID와 인증 제공자로 단건 조회.
     *
     * @param memberId 회원 ID
     * @param authProvider 인증 제공자 (PHONE, KAKAO)
     * @return MemberAuthJpaEntity Optional
     */
    public Optional<MemberAuthJpaEntity> findByMemberIdAndProvider(
            Long memberId, String authProvider) {
        MemberAuthJpaEntity entity =
                queryFactory
                        .selectFrom(memberAuthJpaEntity)
                        .where(
                                conditionBuilder.memberIdEq(memberId),
                                conditionBuilder.authProviderEq(authProvider),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 제공자 사용자 ID로 단건 조회.
     *
     * @param providerUserId 제공자 사용자 ID
     * @return MemberAuthJpaEntity Optional
     */
    public Optional<MemberAuthJpaEntity> findByProviderUserId(String providerUserId) {
        MemberAuthJpaEntity entity =
                queryFactory
                        .selectFrom(memberAuthJpaEntity)
                        .where(
                                conditionBuilder.providerUserIdEq(providerUserId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 회원 ID로 인증 수단 전체 조회.
     *
     * @param memberId 회원 ID
     * @return MemberAuthJpaEntity 목록
     */
    public List<MemberAuthJpaEntity> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(memberAuthJpaEntity)
                .where(conditionBuilder.memberIdEq(memberId), conditionBuilder.notDeleted())
                .fetch();
    }
}
