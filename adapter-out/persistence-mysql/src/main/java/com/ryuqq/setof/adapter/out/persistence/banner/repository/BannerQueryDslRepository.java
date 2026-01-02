package com.ryuqq.setof.adapter.out.persistence.banner.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.QBannerJpaEntity;
import com.ryuqq.setof.domain.cms.query.criteria.BannerSearchCriteria;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BannerQueryDslRepository - Banner QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BannerQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QBannerJpaEntity qBanner = QBannerJpaEntity.bannerJpaEntity;

    public BannerQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 Banner 단건 조회 */
    public Optional<BannerJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qBanner)
                        .where(qBanner.id.eq(id), qBanner.deletedAt.isNull())
                        .fetchOne());
    }

    /** 검색 조건으로 Banner 목록 조회 */
    public List<BannerJpaEntity> findByCriteria(BannerSearchCriteria criteria) {
        return queryFactory
                .selectFrom(qBanner)
                .where(
                        bannerTypeEquals(
                                criteria.bannerType() != null
                                        ? criteria.bannerType().name()
                                        : null),
                        statusEquals(criteria.status() != null ? criteria.status().name() : null),
                        displayableAt(criteria.displayableAt()),
                        qBanner.deletedAt.isNull())
                .orderBy(qBanner.createdAt.desc())
                .offset(criteria.offset())
                .limit(criteria.limit())
                .fetch();
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qBanner)
                        .where(qBanner.id.eq(id), qBanner.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }

    // ===== Dynamic Conditions =====

    private BooleanExpression bannerTypeEquals(String bannerType) {
        if (bannerType == null) {
            return null;
        }
        return qBanner.bannerType.eq(bannerType);
    }

    private BooleanExpression statusEquals(String status) {
        if (status == null) {
            return null;
        }
        return qBanner.status.eq(status);
    }

    private BooleanExpression displayableAt(Instant displayableAt) {
        if (displayableAt == null) {
            return null;
        }
        return qBanner.displayStartDate
                .loe(displayableAt)
                .and(qBanner.displayEndDate.goe(displayableAt));
    }
}
