package com.ryuqq.setof.storage.legacy.composite.web.brand.condition;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBrandCompositeConditionBuilder - 레거시 Web 브랜드 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBrandCompositeConditionBuilder {

    // ===== ID 조건 =====

    /**
     * 브랜드 ID 일치 조건.
     *
     * @param brandId 브랜드 ID
     * @return BooleanExpression
     */
    public BooleanExpression brandIdEq(Long brandId) {
        return brandId != null ? legacyBrandEntity.id.eq(brandId) : null;
    }

    // ===== 검색 조건 =====

    /**
     * 브랜드명 LIKE 검색 조건 (한글명 OR 영문명).
     *
     * <p>레거시 쿼리: displayKoreanName LIKE '%keyword%' OR displayEnglishName LIKE '%keyword%'
     *
     * @param searchWord 검색어
     * @return BooleanExpression
     */
    public BooleanExpression brandNameLike(String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        return legacyBrandEntity
                .displayKoreanName
                .like("%" + searchWord + "%")
                .or(legacyBrandEntity.displayEnglishName.like("%" + searchWord + "%"));
    }
}
