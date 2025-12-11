package com.ryuqq.setof.adapter.out.persistence.brand.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.QBrandJpaEntity;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BrandQueryDslRepository - Brand QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByCode(String code): 브랜드 코드로 조회
 *   <li>findByCondition(condition): 검색 조건으로 목록 조회
 *   <li>findAllActive(): 활성 브랜드 목록 조회
 *   <li>countByCondition(condition): 검색 조건으로 총 개수 조회
 *   <li>existsById(Long id): 존재 여부 확인
 *   <li>existsActiveById(Long id): 활성 브랜드 존재 여부 확인
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
public class BrandQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QBrandJpaEntity qBrand = QBrandJpaEntity.brandJpaEntity;

    public BrandQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Brand 단건 조회
     *
     * @param id Brand ID
     * @return BrandJpaEntity (Optional)
     */
    public Optional<BrandJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qBrand).where(qBrand.id.eq(id)).fetchOne());
    }

    /**
     * 브랜드 코드로 Brand 단건 조회
     *
     * @param code 브랜드 코드
     * @return BrandJpaEntity (Optional)
     */
    public Optional<BrandJpaEntity> findByCode(String code) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qBrand).where(qBrand.code.eq(code)).fetchOne());
    }

    /**
     * 검색 조건으로 Brand 목록 조회 (페이징)
     *
     * @param condition 검색 조건
     * @return BrandJpaEntity 목록
     */
    public List<BrandJpaEntity> findByCondition(BrandSearchCriteria criteria) {
        BooleanBuilder builder = buildCondition(criteria);

        return queryFactory
                .selectFrom(qBrand)
                .where(builder)
                .orderBy(qBrand.nameKo.asc())
                .offset(criteria.offset())
                .limit(criteria.size())
                .fetch();
    }

    /**
     * 활성화된 모든 Brand 목록 조회
     *
     * @return BrandJpaEntity 목록 (nameKo 순)
     */
    public List<BrandJpaEntity> findAllActive() {
        return queryFactory
                .selectFrom(qBrand)
                .where(qBrand.status.eq("ACTIVE"))
                .orderBy(qBrand.nameKo.asc())
                .fetch();
    }

    /**
     * 검색 조건으로 총 개수 조회
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    public long countByCondition(BrandSearchCriteria criteria) {
        BooleanBuilder builder = buildCondition(criteria);

        Long count = queryFactory.select(qBrand.count()).from(qBrand).where(builder).fetchOne();

        return count != null ? count : 0L;
    }

    /**
     * ID로 Brand 존재 여부 확인
     *
     * @param id Brand ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count = queryFactory.selectOne().from(qBrand).where(qBrand.id.eq(id)).fetchFirst();
        return count != null;
    }

    /**
     * 활성 Brand 존재 여부 확인
     *
     * @param id Brand ID
     * @return 활성 존재 여부
     */
    public boolean existsActiveById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qBrand)
                        .where(qBrand.id.eq(id), qBrand.status.eq("ACTIVE"))
                        .fetchFirst();
        return count != null;
    }

    /** 검색 조건 빌더 */
    private BooleanBuilder buildCondition(BrandSearchCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (브랜드명)
        if (criteria.hasKeyword()) {
            builder.and(
                    qBrand.nameKo
                            .containsIgnoreCase(criteria.keyword())
                            .or(qBrand.nameEn.containsIgnoreCase(criteria.keyword())));
        }

        // 상태 필터
        if (criteria.hasStatus()) {
            builder.and(qBrand.status.eq(criteria.status()));
        } else {
            // 기본값: ACTIVE만 조회
            builder.and(qBrand.status.eq("ACTIVE"));
        }

        return builder;
    }
}
