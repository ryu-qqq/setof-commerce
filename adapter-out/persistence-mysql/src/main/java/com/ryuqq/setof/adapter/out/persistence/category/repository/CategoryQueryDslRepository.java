package com.ryuqq.setof.adapter.out.persistence.category.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.entity.QCategoryJpaEntity;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CategoryQueryDslRepository - Category QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByCode(String code): 카테고리 코드로 조회
 *   <li>findByParentId(Long parentId): 하위 카테고리 목록 조회
 *   <li>findByCondition(condition): 검색 조건으로 목록 조회
 *   <li>findAllActive(): 활성 카테고리 목록 조회
 *   <li>findByIds(List ids): ID 목록으로 조회
 *   <li>existsById(Long id): 존재 여부 확인
 *   <li>existsActiveById(Long id): 활성 카테고리 존재 여부 확인
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class CategoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QCategoryJpaEntity qCategory = QCategoryJpaEntity.categoryJpaEntity;

    public CategoryQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Category 단건 조회
     *
     * @param id Category ID
     * @return CategoryJpaEntity (Optional)
     */
    public Optional<CategoryJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qCategory).where(qCategory.id.eq(id)).fetchOne());
    }

    /**
     * 카테고리 코드로 Category 단건 조회
     *
     * @param code 카테고리 코드
     * @return CategoryJpaEntity (Optional)
     */
    public Optional<CategoryJpaEntity> findByCode(String code) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qCategory).where(qCategory.code.eq(code)).fetchOne());
    }

    /**
     * 부모 ID로 하위 카테고리 목록 조회
     *
     * @param parentId 부모 카테고리 ID
     * @return CategoryJpaEntity 목록 (sortOrder 순)
     */
    public List<CategoryJpaEntity> findByParentId(Long parentId) {
        return queryFactory
                .selectFrom(qCategory)
                .where(qCategory.parentId.eq(parentId), qCategory.status.eq("ACTIVE"))
                .orderBy(qCategory.sortOrder.asc())
                .fetch();
    }

    /**
     * 검색 조건으로 Category 목록 조회
     *
     * @param condition 검색 조건
     * @return CategoryJpaEntity 목록
     */
    public List<CategoryJpaEntity> findByCondition(CategorySearchCriteria criteria) {
        BooleanBuilder builder = buildCondition(criteria);

        return queryFactory
                .selectFrom(qCategory)
                .where(builder)
                .orderBy(qCategory.depth.asc(), qCategory.sortOrder.asc())
                .fetch();
    }

    /**
     * 활성화된 모든 Category 목록 조회
     *
     * @return CategoryJpaEntity 목록 (depth, sortOrder 순)
     */
    public List<CategoryJpaEntity> findAllActive() {
        return queryFactory
                .selectFrom(qCategory)
                .where(qCategory.status.eq("ACTIVE"))
                .orderBy(qCategory.depth.asc(), qCategory.sortOrder.asc())
                .fetch();
    }

    /**
     * ID 목록으로 Category 조회
     *
     * @param ids ID 목록
     * @return CategoryJpaEntity 목록 (depth 순)
     */
    public List<CategoryJpaEntity> findByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(qCategory)
                .where(qCategory.id.in(ids))
                .orderBy(qCategory.depth.asc())
                .fetch();
    }

    /**
     * ID로 Category 존재 여부 확인
     *
     * @param id Category ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory.selectOne().from(qCategory).where(qCategory.id.eq(id)).fetchFirst();
        return count != null;
    }

    /**
     * 활성 Category 존재 여부 확인
     *
     * @param id Category ID
     * @return 활성 존재 여부
     */
    public boolean existsActiveById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qCategory)
                        .where(qCategory.id.eq(id), qCategory.status.eq("ACTIVE"))
                        .fetchFirst();
        return count != null;
    }

    /** 검색 조건 빌더 */
    private BooleanBuilder buildCondition(CategorySearchCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();

        // 부모 ID 필터
        if (criteria.hasParentId()) {
            builder.and(qCategory.parentId.eq(criteria.parentId()));
        } else if (criteria.depth() != null && criteria.depth() == 0) {
            // depth가 0이면 parentId가 null인 최상위 카테고리
            builder.and(qCategory.parentId.isNull());
        }

        // 깊이 필터
        if (criteria.hasDepth()) {
            builder.and(qCategory.depth.eq(criteria.depth()));
        }

        // 상태 필터
        if (criteria.hasStatus()) {
            builder.and(qCategory.status.eq(criteria.status()));
        } else if (!criteria.includeInactive()) {
            // 기본값: ACTIVE만 조회
            builder.and(qCategory.status.eq("ACTIVE"));
        }

        return builder;
    }
}
