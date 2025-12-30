package com.ryuqq.setof.adapter.out.persistence.claim.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.claim.condition.AdminClaimSearchCondition;
import com.ryuqq.setof.adapter.out.persistence.claim.entity.ClaimJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.claim.entity.QClaimJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ClaimQueryDslRepository - Claim QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>동적 쿼리 구성 (BooleanExpression)
 *   <li>단건/목록 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지 (fetch join, left join, inner join)
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Repository
public class ClaimQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QClaimJpaEntity qClaim = QClaimJpaEntity.claimJpaEntity;

    public ClaimQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 클레임 ID로 단건 조회
     *
     * @param claimId 클레임 ID (UUID String)
     * @return ClaimJpaEntity (Optional)
     */
    public Optional<ClaimJpaEntity> findByClaimId(String claimId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qClaim).where(qClaim.claimId.eq(claimId)).fetchOne());
    }

    /**
     * Admin 검색 조건으로 Claim 목록 조회 (커서 기반 페이징)
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param condition Admin 검색 조건
     * @return ClaimJpaEntity 목록
     */
    public List<ClaimJpaEntity> findByAdminCondition(AdminClaimSearchCondition condition) {
        BooleanBuilder builder = buildAdminConditions(condition);

        return queryFactory
                .selectFrom(qClaim)
                .where(builder)
                .orderBy(qClaim.createdAt.desc(), qClaim.id.desc())
                .limit(condition.limit() + 1L)
                .fetch();
    }

    /**
     * 클레임 번호로 단건 조회
     *
     * @param claimNumber 클레임 번호
     * @return ClaimJpaEntity (Optional)
     */
    public Optional<ClaimJpaEntity> findByClaimNumber(String claimNumber) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qClaim)
                        .where(qClaim.claimNumber.eq(claimNumber))
                        .fetchOne());
    }

    /**
     * 주문 ID로 클레임 목록 조회 (생성일시 내림차순)
     *
     * @param orderId 주문 ID
     * @return 클레임 목록
     */
    public List<ClaimJpaEntity> findByOrderIdOrderByCreatedAtDesc(String orderId) {
        return queryFactory
                .selectFrom(qClaim)
                .where(qClaim.orderId.eq(orderId))
                .orderBy(qClaim.createdAt.desc())
                .fetch();
    }

    /**
     * 주문 ID와 상태로 클레임 목록 조회
     *
     * @param orderId 주문 ID
     * @param status 클레임 상태
     * @return 클레임 목록
     */
    public List<ClaimJpaEntity> findByOrderIdAndStatus(String orderId, String status) {
        return queryFactory
                .selectFrom(qClaim)
                .where(qClaim.orderId.eq(orderId), qClaim.status.eq(status))
                .fetch();
    }

    /**
     * 상태로 클레임 목록 조회 (생성일시 내림차순)
     *
     * @param status 클레임 상태
     * @return 클레임 목록
     */
    public List<ClaimJpaEntity> findByStatusOrderByCreatedAtDesc(String status) {
        return queryFactory
                .selectFrom(qClaim)
                .where(qClaim.status.eq(status))
                .orderBy(qClaim.createdAt.desc())
                .fetch();
    }

    /**
     * 주문 ID로 활성 클레임 존재 여부 확인
     *
     * @param orderId 주문 ID
     * @param statuses 활성 상태 목록
     * @return 존재 여부
     */
    public boolean existsByOrderIdAndStatusIn(String orderId, List<String> statuses) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(qClaim)
                        .where(qClaim.orderId.eq(orderId), qClaim.status.in(statuses))
                        .fetchFirst();
        return result != null;
    }

    /**
     * Admin 검색 조건 BooleanBuilder 생성
     *
     * @param condition Admin 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildAdminConditions(AdminClaimSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (condition.hasStatuses()) {
            builder.and(qClaim.status.in(condition.statuses()));
        }

        if (condition.hasClaimTypes()) {
            builder.and(qClaim.claimType.in(condition.claimTypes()));
        }

        if (condition.hasSearchKeyword()) {
            BooleanBuilder keywordBuilder = new BooleanBuilder();
            keywordBuilder.or(qClaim.claimNumber.containsIgnoreCase(condition.searchKeyword()));
            keywordBuilder.or(qClaim.orderId.containsIgnoreCase(condition.searchKeyword()));
            builder.and(keywordBuilder);
        }

        if (condition.hasStartDate()) {
            builder.and(qClaim.createdAt.goe(condition.startDate()));
        }

        if (condition.hasEndDate()) {
            builder.and(qClaim.createdAt.loe(condition.endDate()));
        }

        if (condition.hasCursor()) {
            builder.and(qClaim.claimId.lt(condition.lastClaimId()));
        }

        return builder;
    }
}
