package com.ryuqq.setof.adapter.out.persistence.memberconsent.repository;

import static com.ryuqq.setof.adapter.out.persistence.memberconsent.entity.QMemberConsentJpaEntity.memberConsentJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.memberconsent.condition.MemberConsentConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.memberconsent.entity.MemberConsentJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * MemberConsentQueryDslRepository - 회원 동의 정보 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class MemberConsentQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final MemberConsentConditionBuilder conditionBuilder;

    public MemberConsentQueryDslRepository(
            JPAQueryFactory queryFactory, MemberConsentConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 회원 ID로 동의 정보 단건 조회.
     *
     * @param memberId 회원 ID
     * @return MemberConsentJpaEntity Optional
     */
    public Optional<MemberConsentJpaEntity> findByMemberId(Long memberId) {
        MemberConsentJpaEntity entity =
                queryFactory
                        .selectFrom(memberConsentJpaEntity)
                        .where(conditionBuilder.memberIdEq(memberId))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }
}
