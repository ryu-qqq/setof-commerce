package com.ryuqq.setof.storage.legacy.brand.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.storage.legacy.brand.condition.LegacyBrandConditionBuilder;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * LegacyBrandQueryDslRepository - 레거시 브랜드 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>레거시 DB는 deletedAt 컬럼이 없으므로 Soft Delete 조건을 적용하지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyBrandQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyBrandConditionBuilder conditionBuilder;

    public LegacyBrandQueryDslRepository(
            JPAQueryFactory queryFactory, LegacyBrandConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 브랜드 조회.
     *
     * @param id 브랜드 ID
     * @return 브랜드 Optional
     */
    public Optional<LegacyBrandEntity> findById(Long id) {
        LegacyBrandEntity entity =
                queryFactory
                        .selectFrom(legacyBrandEntity)
                        .where(conditionBuilder.idEq(id))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 브랜드 목록 조회.
     *
     * @param ids 브랜드 ID 목록
     * @return 브랜드 목록
     */
    public List<LegacyBrandEntity> findByIds(List<Long> ids) {
        return queryFactory.selectFrom(legacyBrandEntity).where(conditionBuilder.idIn(ids)).fetch();
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
                        .from(legacyBrandEntity)
                        .where(conditionBuilder.idEq(id))
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 브랜드 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 브랜드 목록
     */
    public List<LegacyBrandEntity> findByCriteria(BrandSearchCriteria criteria) {
        QueryContext<BrandSortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(legacyBrandEntity)
                .where(
                        conditionBuilder.displayedEq(criteria.displayed()),
                        conditionBuilder.searchCondition(criteria))
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
                        .select(legacyBrandEntity.count())
                        .from(legacyBrandEntity)
                        .where(
                                conditionBuilder.displayedEq(criteria.displayed()),
                                conditionBuilder.searchCondition(criteria))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 노출 중인 브랜드 전체 조회.
     *
     * @return 노출 중인 브랜드 목록
     */
    public List<LegacyBrandEntity> findAllDisplayed() {
        return queryFactory
                .selectFrom(legacyBrandEntity)
                .where(conditionBuilder.displayedEq(true))
                .orderBy(legacyBrandEntity.displayOrder.asc())
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(BrandSortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc
                            ? legacyBrandEntity.insertDate.asc()
                            : legacyBrandEntity.insertDate.desc();
            case DISPLAY_ORDER ->
                    isAsc
                            ? legacyBrandEntity.displayOrder.asc()
                            : legacyBrandEntity.displayOrder.desc();
            case BRAND_NAME ->
                    isAsc ? legacyBrandEntity.brandName.asc() : legacyBrandEntity.brandName.desc();
        };
    }
}
