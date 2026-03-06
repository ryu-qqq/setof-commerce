package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerJpaEntity.sellerJpaEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.seller.condition.SellerConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSortKey;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerQueryDslRepository - 셀러 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class SellerQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final SellerConditionBuilder conditionBuilder;

    public SellerQueryDslRepository(
            JPAQueryFactory queryFactory, SellerConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 셀러 조회.
     *
     * @param id 셀러 ID
     * @return 셀러 Optional
     */
    public Optional<SellerJpaEntity> findById(Long id) {
        SellerJpaEntity entity =
                queryFactory
                        .selectFrom(sellerJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 셀러 목록 조회.
     *
     * @param ids 셀러 ID 목록
     * @return 셀러 목록
     */
    public List<SellerJpaEntity> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(sellerJpaEntity)
                .where(sellerJpaEntity.id.in(ids), conditionBuilder.notDeleted())
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
                        .from(sellerJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
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
                        .from(sellerJpaEntity)
                        .where(
                                conditionBuilder.sellerNameEq(sellerName),
                                conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 특정 ID를 제외한 셀러명 존재 여부 확인.
     *
     * @param sellerName 셀러명
     * @param excludeId 제외할 셀러 ID
     * @return 존재 여부
     */
    public boolean existsBySellerNameExcluding(String sellerName, Long excludeId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(sellerJpaEntity)
                        .where(
                                conditionBuilder.sellerNameEq(sellerName),
                                conditionBuilder.idNe(excludeId),
                                conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 셀러 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 셀러 목록
     */
    public List<SellerJpaEntity> findByCriteria(SellerSearchCriteria criteria) {
        QueryContext<SellerSortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(sellerJpaEntity)
                .where(
                        conditionBuilder.activeEq(criteria.active()),
                        conditionBuilder.searchCondition(criteria),
                        conditionBuilder.notDeleted())
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
                        .select(sellerJpaEntity.count())
                        .from(sellerJpaEntity)
                        .where(
                                conditionBuilder.activeEq(criteria.active()),
                                conditionBuilder.searchCondition(criteria),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    private OrderSpecifier<?> createOrderSpecifier(SellerSortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc ? sellerJpaEntity.createdAt.asc() : sellerJpaEntity.createdAt.desc();
            case SELLER_NAME ->
                    isAsc ? sellerJpaEntity.sellerName.asc() : sellerJpaEntity.sellerName.desc();
            case DISPLAY_NAME ->
                    isAsc ? sellerJpaEntity.displayName.asc() : sellerJpaEntity.displayName.desc();
        };
    }
}
