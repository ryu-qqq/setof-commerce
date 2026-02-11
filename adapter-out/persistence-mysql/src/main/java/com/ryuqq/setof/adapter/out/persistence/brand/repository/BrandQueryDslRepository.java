package com.ryuqq.setof.adapter.out.persistence.brand.repository;

import static com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity.brandJpaEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.brand.condition.BrandConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BrandQueryDslRepository - 브랜드 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class BrandQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final BrandConditionBuilder conditionBuilder;

    public BrandQueryDslRepository(
            JPAQueryFactory queryFactory, BrandConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 브랜드 조회.
     *
     * @param id 브랜드 ID
     * @return 브랜드 Optional
     */
    public Optional<BrandJpaEntity> findById(Long id) {
        BrandJpaEntity entity =
                queryFactory
                        .selectFrom(brandJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 브랜드 목록 조회.
     *
     * @param ids 브랜드 ID 목록
     * @return 브랜드 목록
     */
    public List<BrandJpaEntity> findByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(brandJpaEntity)
                .where(conditionBuilder.idIn(ids), conditionBuilder.notDeleted())
                .fetch();
    }

    /**
     * ID 존재 여부 확인.
     *
     * @param id 브랜드 ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(brandJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 브랜드 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 브랜드 목록
     */
    public List<BrandJpaEntity> findByCriteria(BrandSearchCriteria criteria) {
        QueryContext<BrandSortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(brandJpaEntity)
                .where(
                        conditionBuilder.displayedEq(criteria.displayed()),
                        conditionBuilder.searchCondition(criteria),
                        conditionBuilder.notDeleted())
                .orderBy(createOrderSpecifier(qc.sortKey(), qc.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.size())
                .fetch();
    }

    /**
     * 검색 조건으로 브랜드 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 브랜드 개수
     */
    public long countByCriteria(BrandSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(brandJpaEntity.count())
                        .from(brandJpaEntity)
                        .where(
                                conditionBuilder.displayedEq(criteria.displayed()),
                                conditionBuilder.brandNameContains(criteria.searchWord()),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 노출 중인 브랜드 전체 조회.
     *
     * @return 노출 중인 브랜드 목록
     */
    public List<BrandJpaEntity> findAllDisplayed() {
        return queryFactory
                .selectFrom(brandJpaEntity)
                .where(conditionBuilder.displayedEq(true), conditionBuilder.notDeleted())
                .orderBy(brandJpaEntity.displayOrder.asc())
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(BrandSortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc ? brandJpaEntity.createdAt.asc() : brandJpaEntity.createdAt.desc();
            case DISPLAY_ORDER ->
                    isAsc ? brandJpaEntity.displayOrder.asc() : brandJpaEntity.displayOrder.desc();
            case BRAND_NAME ->
                    isAsc ? brandJpaEntity.brandName.asc() : brandJpaEntity.brandName.desc();
        };
    }
}
