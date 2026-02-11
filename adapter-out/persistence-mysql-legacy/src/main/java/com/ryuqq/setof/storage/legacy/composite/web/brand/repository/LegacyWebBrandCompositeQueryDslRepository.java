package com.ryuqq.setof.storage.legacy.composite.web.brand.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.composite.web.brand.condition.LegacyWebBrandCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.brand.dto.LegacyWebBrandQueryDto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebBrandCompositeQueryDslRepository - 레거시 Web 브랜드 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebBrandCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebBrandCompositeConditionBuilder conditionBuilder;

    public LegacyWebBrandCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebBrandCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    // ===== 목록 조회 (fetchBrands) =====

    /**
     * 전체 브랜드 조회.
     *
     * @return 브랜드 목록
     */
    public List<LegacyWebBrandQueryDto> fetchBrands() {
        return queryFactory.select(createBrandProjection()).from(legacyBrandEntity).fetch();
    }

    /**
     * 브랜드 검색 조회.
     *
     * @param condition 검색 조건
     * @return 브랜드 목록
     */
    public List<LegacyWebBrandQueryDto> fetchBrands(String searchWord) {
        return queryFactory
                .select(createBrandProjection())
                .from(legacyBrandEntity)
                .where(conditionBuilder.brandNameLike(searchWord))
                .fetch();
    }

    // ===== 단건 조회 (fetchBrand) =====

    /**
     * 브랜드 단건 조회 (ID).
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 Optional
     */
    public Optional<LegacyWebBrandQueryDto> fetchBrand(Long brandId) {
        LegacyWebBrandQueryDto dto =
                queryFactory
                        .select(createBrandProjection())
                        .from(legacyBrandEntity)
                        .where(conditionBuilder.brandIdEq(brandId))
                        .fetchFirst();
        return Optional.ofNullable(dto);
    }

    // ===== Projection 생성 =====

    /**
     * 브랜드 Projection 생성.
     *
     * <p>Projections.constructor() 사용 (@QueryProjection 금지).
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebBrandQueryDto>
            createBrandProjection() {
        return Projections.constructor(
                LegacyWebBrandQueryDto.class,
                legacyBrandEntity.id,
                legacyBrandEntity.brandName,
                legacyBrandEntity.displayKoreanName,
                legacyBrandEntity.brandIconImageUrl);
    }
}
