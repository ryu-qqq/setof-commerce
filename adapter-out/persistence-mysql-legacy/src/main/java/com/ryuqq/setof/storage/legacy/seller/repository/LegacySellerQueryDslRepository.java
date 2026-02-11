package com.ryuqq.setof.storage.legacy.seller.repository;

import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSortKey;
import com.ryuqq.setof.storage.legacy.seller.condition.LegacySellerConditionBuilder;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * LegacySellerQueryDslRepository - 레거시 셀러 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>레거시 DB는 deletedAt, active 컬럼이 없으므로 해당 조건을 적용하지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacySellerQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacySellerConditionBuilder conditionBuilder;

    public LegacySellerQueryDslRepository(
            JPAQueryFactory queryFactory, LegacySellerConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 셀러 조회.
     *
     * @param id 셀러 ID
     * @return 셀러 Optional
     */
    public Optional<LegacySellerEntity> findById(Long id) {
        LegacySellerEntity entity =
                queryFactory
                        .selectFrom(legacySellerEntity)
                        .where(conditionBuilder.idEq(id))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 셀러 목록 조회.
     *
     * @param ids 셀러 ID 목록
     * @return 셀러 목록
     */
    public List<LegacySellerEntity> findByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(legacySellerEntity)
                .where(conditionBuilder.idIn(ids))
                .fetch();
    }

    /**
     * ID 존재 여부 확인.
     *
     * @param id 셀러 ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(legacySellerEntity)
                        .where(conditionBuilder.idEq(id))
                        .fetchFirst();
        return result != null;
    }

    /**
     * 셀러명 존재 여부 확인.
     *
     * @param sellerName 셀러명
     * @return 존재 여부
     */
    public boolean existsBySellerName(String sellerName) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(legacySellerEntity)
                        .where(conditionBuilder.sellerNameEq(sellerName))
                        .fetchFirst();
        return result != null;
    }

    /**
     * 셀러명 존재 여부 확인 (특정 ID 제외).
     *
     * @param sellerName 셀러명
     * @param excludeId 제외할 셀러 ID
     * @return 존재 여부
     */
    public boolean existsBySellerNameExcluding(String sellerName, Long excludeId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(legacySellerEntity)
                        .where(
                                conditionBuilder.sellerNameEq(sellerName),
                                conditionBuilder.idNe(excludeId))
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 셀러 목록 조회.
     *
     * <p>레거시 DB에는 active, deletedAt 컬럼이 없으므로 해당 필터를 무시합니다.
     *
     * @param criteria 검색 조건
     * @return 셀러 목록
     */
    public List<LegacySellerEntity> findByCriteria(SellerSearchCriteria criteria) {
        QueryContext<SellerSortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(legacySellerEntity)
                .where(conditionBuilder.searchCondition(criteria))
                .orderBy(createOrderSpecifier(qc.sortKey(), qc.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.size())
                .fetch();
    }

    /**
     * 검색 조건으로 셀러 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 셀러 개수
     */
    public long countByCriteria(SellerSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(legacySellerEntity.count())
                        .from(legacySellerEntity)
                        .where(conditionBuilder.searchCondition(criteria))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    private OrderSpecifier<?> createOrderSpecifier(SellerSortKey sortKey, SortDirection direction) {
        boolean isAsc = direction.isAscending();

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc
                            ? legacySellerEntity.createdAt.asc()
                            : legacySellerEntity.createdAt.desc();
            case SELLER_NAME ->
                    isAsc
                            ? legacySellerEntity.sellerName.asc()
                            : legacySellerEntity.sellerName.desc();
            case DISPLAY_NAME ->
                    isAsc
                            ? legacySellerEntity.sellerName.asc()
                            : legacySellerEntity.sellerName.desc();
        };
    }
}
